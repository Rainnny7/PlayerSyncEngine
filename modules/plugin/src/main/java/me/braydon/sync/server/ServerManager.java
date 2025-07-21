package me.braydon.sync.server;

import com.google.common.reflect.TypeToken;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import me.braydon.sync.Environment;
import me.braydon.sync.PlayerSyncEngine;
import me.braydon.sync.common.Constants;
import me.braydon.sync.common.MathUtils;
import me.braydon.sync.common.MinecraftVersion;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;
import java.util.UUID;

/**
 * @author Braydon
 */
public final class ServerManager {
    @NonNull private final PlayerSyncEngine plugin;
    @Getter private MinecraftServer thisServer;

    public ServerManager(@NonNull PlayerSyncEngine plugin) {
        this.plugin = plugin;
        identifyServer();

        // Schedule a task to periodically update the server's status in Redis
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::updateStatus, 7L, 3L * 20L);
    }

    /**
     * Update this server's status in Redis.
     */
    private void updateStatus() {
        long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        thisServer.setPlayers(Bukkit.getOnlinePlayers().size());
        thisServer.setWhitelisted(Bukkit.hasWhitelist());
        thisServer.setTps(MathUtils.round(Bukkit.getServer().getTPS()[0], 2));
        thisServer.setRamUsage(MathUtils.round(usedMemory / 1024D / 1024D, 2));
        thisServer.setHeartbeat(System.currentTimeMillis());
        plugin.getRedis().getConnection().sync().set("server:" + thisServer.getUniqueId(), Constants.GSON.toJson(thisServer));
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
        thisServer = new MinecraftServer(serverId, Environment.SERVER_REGION.getValue(),
                MinecraftVersion.byDecimalVersion(Bukkit.getMinecraftVersion()), 0,
                true, 0D, 0D, now, now
        );
        plugin.getLogger().info("%sIdentified server as %s located in %s running Minecraft %s".formatted(
                newServer ? "(new) " : "", thisServer.getUniqueId(), thisServer.getRegion().getName(), thisServer.getVersion().getName()
        ));
    }
}