package me.braydon.sync;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import me.braydon.sync.command.PlayerSyncEngineCommand;
import me.braydon.sync.database.redis.RedisDatabase;
import me.braydon.sync.packet.PacketHandler;
import me.braydon.sync.player.PlayerManager;
import me.braydon.sync.server.ServerManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Braydon
 */
@Getter
public final class PlayerSyncEngine extends JavaPlugin {
    @Getter private static PlayerSyncEngine instance;

    private RedisDatabase redis;
    private ServerManager serverManager;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Firing up...");

        // Initialize databases
        (redis = new RedisDatabase()).connect();

        serverManager = new ServerManager(this);
        PacketHandler.initialize(serverManager.getThisServer().getUniqueId());
        new PlayerManager(this);

        // Register commands
        new PaperCommandManager(this).registerCommand(new PlayerSyncEngineCommand(this));

        getLogger().info("Welcome!");
    }

    @Override
    public void onDisable() {
        // Cleanup resources on shutdown
        getLogger().info("Shutting down...");
        redis.cleanup();
        getLogger().info("Done, goodbye!");
    }
}