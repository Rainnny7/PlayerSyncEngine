package me.braydon.sync;

import lombok.Getter;
import me.braydon.sync.data.database.redis.RedisDatabase;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Braydon
 */
@Getter
public final class PlayerSyncEngine extends JavaPlugin {
    @Getter private static PlayerSyncEngine instance;

    private RedisDatabase redis;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Firing up...");

        // Initialize databases
        (redis = new RedisDatabase()).connect();
    }

    @Override
    public void onDisable() {
        // Cleanup resources on shutdown
        getLogger().info("Shutting down...");
        redis.cleanup();
        getLogger().info("Done, goodbye!");
    }
}