package me.braydon.sync.packet.impl.player;

import lombok.Data;
import lombok.NonNull;
import me.braydon.sync.packet.Packet;
import me.braydon.sync.server.MinecraftServer;

import java.util.UUID;

/**
 * A packet sent when a player connects
 * to either a {@link MinecraftServer}.
 *
 * @author Braydon
 */
@Data
public final class PlayerConnectPacket implements Packet {
    /**
     * The UUID of the player connecting.
     */
    @NonNull private final UUID uniqueId;

    /**
     * The name of the player connecting.
     */
    @NonNull private final String name;

    /**
     * The UUID of the {@link MinecraftServer}
     * this player player is connecting to.
     */
    @NonNull private final UUID serverId;
}