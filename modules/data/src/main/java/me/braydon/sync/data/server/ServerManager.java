package me.braydon.sync.data.server;

import com.google.common.reflect.TypeToken;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import me.braydon.sync.data.PlayerSyncEngine;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;
import java.util.UUID;

/**
 * @author Braydon
 */
@Log4j2(topic = "Server Manager") @Getter
public final class ServerManager {
    /**
     * The instance of the plugin.
     */
    @NonNull private final PlayerSyncEngine plugin;

    /**
     * The server this plugin is running on.
     */
    private SyncServer thisServer;

    public ServerManager(@NonNull PlayerSyncEngine plugin) {
        this.plugin = plugin;
        identifyServer();
    }

    @SneakyThrows
    private void identifyServer() {
        File serverDataFile = new File(plugin.getDataFolder(), ".server");
        if (!serverDataFile.exists()) { // New server
            serverDataFile.mkdirs();
            thisServer = new SyncServer(UUID.randomUUID());
            try (FileWriter writer = new FileWriter(serverDataFile)) {
                PlayerSyncEngine.getGson().toJson(Map.of("uuid", thisServer.getUuid().toString()), writer);
            }
        } else { // Existing server
            try (FileReader reader = new FileReader(serverDataFile)) {
                Map<String, UUID> data = PlayerSyncEngine.getGson().fromJson(reader, new TypeToken<Map<String, String>>() {}.getType());
                thisServer = new SyncServer(data.get("uuid"));
            }
        }
        log.info("Identified server as {}", thisServer.getUuid());
    }
}