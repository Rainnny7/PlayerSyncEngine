package me.braydon.sync.data.server;

import lombok.*;

import java.util.UUID;

/**
 * A representation of another
 * Minecraft server to sync with.
 *
 * @author Braydon
 */
@Data
public final class SyncServer {
    /**
     * The UUID of this server.
     */
    @NonNull private final UUID uuid;
}