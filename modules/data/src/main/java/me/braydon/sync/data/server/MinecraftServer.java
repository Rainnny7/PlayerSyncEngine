package me.braydon.sync.data.server;

import lombok.*;

import java.util.UUID;

/**
 * @author Braydon
 */
@Data
public abstract class MinecraftServer {
    /**
     * The UUID of this server.
     */
    @NonNull private final UUID uniqueId;

    /**
     * The type of this server.
     */
    @NonNull private final ServerType type;

    /**
     * The region this server is in.
     */
    @NonNull private final Region region;
}