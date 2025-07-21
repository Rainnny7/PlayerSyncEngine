package me.braydon.sync.player;

import lombok.NonNull;
import me.braydon.sync.PlayerSyncEngine;
import me.braydon.sync.packet.Packet;
import me.braydon.sync.packet.impl.player.PlayerConnectPacket;
import me.braydon.sync.packet.impl.player.PlayerDisconnectPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Braydon
 */
public final class PlayerManager implements Listener {
    @NonNull private final PlayerSyncEngine plugin;

    private final ConcurrentHashMap<UUID, ConnectedPlayer> players = new ConcurrentHashMap<>();

    public PlayerManager(@NonNull PlayerSyncEngine plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    private void onJoin(@NonNull PlayerJoinEvent event) {
        // Notify other servers that a player has connected
        Player player = event.getPlayer();
        plugin.getRedis().publish(Packet.ChannelPrefix.PLAYER, new PlayerConnectPacket(player.getUniqueId(),
                player.getName(), plugin.getServerManager().getThisServer().getUniqueId()));
    }

    @EventHandler
    private void onQuit(@NonNull PlayerQuitEvent event) {
        // Notify other servers that a player has disconnected
        plugin.getRedis().publish(Packet.ChannelPrefix.PLAYER, new PlayerDisconnectPacket(event.getPlayer().getUniqueId(),
                plugin.getServerManager().getThisServer().getUniqueId()));
    }
}