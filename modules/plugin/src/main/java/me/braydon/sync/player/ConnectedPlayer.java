package me.braydon.sync.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import me.braydon.sync.server.MinecraftServer;

import javax.xml.stream.Location;
import java.util.UUID;

/**
 * @author Braydon
 */
@Data
public final class ConnectedPlayer {
    /**
     * The UUID of this player.
     */
    @NonNull private final UUID uniqueId;

    /**
     * The name of this player.
     */
    @NonNull private final String name;

    /**
     * The UUID of the {@link MinecraftServer} this player is connected to.
     */
    @NonNull private final UUID serverId;

    /**
     * The location of this player.
     */
    @NonNull private final Location location;

    /**
     * THe player's ping to the server they're connected to.
     */
    private final long ping;
}