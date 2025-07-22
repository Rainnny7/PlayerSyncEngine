package me.braydon.sync.server;

import com.destroystokyo.paper.event.server.WhitelistToggleEvent;
import com.google.common.reflect.TypeToken;
import io.papermc.paper.event.server.WhitelistStateUpdateEvent;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import me.braydon.sync.Environment;
import me.braydon.sync.PlayerSyncEngine;
import me.braydon.sync.common.Constants;
import me.braydon.sync.common.MathUtils;
import me.braydon.sync.common.MinecraftVersion;
import me.braydon.sync.packet.Packet;
import me.braydon.sync.packet.PacketHandler;
import me.braydon.sync.packet.annotation.PacketListener;
import me.braydon.sync.packet.impl.server.ServerStatusUpdatePacket;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Braydon
 */
public final class ServerManager implements Listener {
    private static final long SERVER_HEARTBEAT_INTERVAL = TimeUnit.SECONDS.toMillis(10L);

    @NonNull private final PlayerSyncEngine plugin;
    @Getter private MinecraftServer thisServer;

    /**
     * A registry of all {@link MinecraftServer}'s, indexed by their unique id.
     */
    @Getter private final ConcurrentHashMap<UUID, MinecraftServer> servers = new ConcurrentHashMap<>();

    public ServerManager(@NonNull PlayerSyncEngine plugin) {
        this.plugin = plugin;
        PacketHandler.registerListeners(this);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        identifyServer();

        // Schedule a task to periodically update the server's status in Redis every 10 seconds
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::updateStatus, 7L, SERVER_HEARTBEAT_INTERVAL / 50L);
    }

    @PacketListener(Packet.ServerPacket.STATUS_UPDATE)
    public void onServerStatusUpdate(@NonNull ServerStatusUpdatePacket packet) {
        UUID serverId = packet.getServerId();

        // A server is present in the update, store it in our local registry
        if (packet.getServer() != null) {
            servers.put(serverId, packet.getServer());
        } else { // The server is going offline, remove it from our local registry
            servers.remove(serverId);
        }
    }

    @EventHandler
    private void onWhitelistChange(@NonNull WhitelistToggleEvent event) {
        // Make other servers aware of the new whitelist state immediately
        updateStatus();
    }

    @EventHandler
    private void onPluginDisable(@NonNull PluginDisableEvent event) {
        // Publish the server status to the message bus for other servers to see
        if (event.getPlugin().equals(plugin)) {
            plugin.getRedis().publish(Packet.ChannelPrefix.SERVER, thisServer.getUniqueId(), new ServerStatusUpdatePacket(null));
        }
    }

    /**
     * Update this server's status in Redis.
     */
    private void updateStatus() {
        // Update this server's status and notify all other servers of its changes
        long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        thisServer.setPlayers(Bukkit.getOnlinePlayers().size());
        thisServer.setWhitelisted(Bukkit.hasWhitelist());
        thisServer.setTps(MathUtils.round(Bukkit.getServer().getTPS()[0], 2));
        thisServer.setRamUsage(MathUtils.round(usedMemory / 1024D / 1024D, 2));
        thisServer.setHeartbeat(System.currentTimeMillis());
        plugin.getRedis().publish(Packet.ChannelPrefix.SERVER, thisServer.getUniqueId(), new ServerStatusUpdatePacket(thisServer));

        // Afterwards, cleanup our local registry of servers that haven't sent changes in a while
        long now = System.currentTimeMillis();
        servers.values().removeIf(server -> now - server.getHeartbeat() > SERVER_HEARTBEAT_INTERVAL + TimeUnit.SECONDS.toMillis(5L));
    }

    /**
     * Identify this server.
     */
    @SneakyThrows
    private void identifyServer() {
        File serverDataFile = new File(plugin.getDataFolder(), ".server");
        UUID serverId;
        boolean newServer = false;

        // The server doesn't have a local data file, it's a new server
        if (!serverDataFile.exists()) {
            newServer = true;
            serverDataFile.getParentFile().mkdirs();
            serverId = UUID.randomUUID();
            try (FileWriter writer = new FileWriter(serverDataFile)) {
                Constants.GSON.toJson(Map.of("uuid", serverId), writer);
            }
        } else { // Existing server, load the UUID from the data file
            try (FileReader reader = new FileReader(serverDataFile)) {
                Map<String, String> data = Constants.GSON.fromJson(reader, new TypeToken<Map<String, String>>() {}.getType());
                serverId = UUID.fromString(data.get("uuid"));
            }
        }

        // At this point, the server has been identified
        long now = System.currentTimeMillis();
        thisServer = new MinecraftServer(serverId, Environment.SERVER_GROUP.getValue(),Environment.SERVER_REGION.getValue(),
                MinecraftVersion.byDecimalVersion(Bukkit.getMinecraftVersion()), 0, true, 0D,
                0D, now, now
        );
        servers.put(thisServer.getUniqueId(), thisServer); // Cache this server

        // Finally publish the server status to the message bus for other servers to see
        plugin.getRedis().publish(Packet.ChannelPrefix.SERVER, thisServer.getUniqueId(), new ServerStatusUpdatePacket(thisServer));

        plugin.getLogger().info("%sIdentified server as %s (group=%s, region=%s) running Minecraft %s".formatted(
                newServer ? "(new) " : "", thisServer.getUniqueId(), thisServer.getGroup(),
                thisServer.getRegion().getName(), thisServer.getVersion().getName()
        ));
    }

    /**
     * Get a server by its unique id.
     *
     * @param uniqueId the id of the server
     * @return the server, null if not found
     */
    public MinecraftServer getServer(@NonNull UUID uniqueId) {
        return servers.get(uniqueId);
    }
}