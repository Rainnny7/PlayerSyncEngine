package me.braydon.sync.event;

import lombok.NonNull;
import me.braydon.sync.PlayerSyncEngine;
import me.braydon.sync.packet.impl.player.PlayerConnectPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

/**
 * @author Braydon
 */
public final class ConnectionEvents implements Listener {
    @NonNull private final PlayerSyncEngine plugin;

    public ConnectionEvents(@NonNull PlayerSyncEngine plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    private void onJoin(@NonNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getRedis().publish("player", new PlayerConnectPacket(player.getUniqueId(), player.getName(), UUID.randomUUID()));
    }
}