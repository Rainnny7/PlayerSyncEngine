package me.braydon.sync.server;

import lombok.*;
import me.braydon.sync.common.MinecraftVersion;

import java.util.UUID;

/**
 * A standalone Minecraft server, usually running <a href="https://papermc.io">Paper</a>.
 *
 * @author Braydon
 */
@AllArgsConstructor @Data
public final class MinecraftServer {
    /**
     * The UUID of this server.
     */
    @NonNull private final UUID uniqueId;

    /**
     * The region this server is in.
     */
    @NonNull private final Region region;

    /**
     * The version of this server.
     */
    @NonNull private final MinecraftVersion version;

    /**
     * The number of players online on this server.
     */
    private int players;

    /**
     * Whether this server is whitelisted.
     */
    private boolean whitelisted;

    /**
     * The TPS of this server.
     */
    private double tps;

    /**
     * THe amount of ram used by this server.
     */
    private double ramUsage;

    /**
     * The unix time of the last heartbeat from this server.
     */
    private long heartbeat;

    /**
     * The unix time of the first heartbeat from this server.
     */
    private final long firstSeen;
}