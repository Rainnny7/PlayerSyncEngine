package me.braydon.sync.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import me.braydon.sync.data.server.ServerManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Braydon
 */
public final class PlayerSyncEngine extends JavaPlugin {
    @Getter private static final Gson gson = new GsonBuilder().serializeNulls().create();

    @Override
    public void onEnable() {
        new ServerManager(this);
    }
}