package me.braydon.sync.packet.impl.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import me.braydon.sync.packet.Packet;
import me.braydon.sync.packet.annotation.PacketId;
import me.braydon.sync.server.MinecraftServer;

import java.util.UUID;

/**
 * A packet sent when a player connects
 * to a {@link MinecraftServer}.
 *
 * @author Braydon
 */
@PacketId(Packet.PlayerPacket.CONNECT) @Data @EqualsAndHashCode(callSuper = true)
public final class PlayerConnectPacket extends Packet {
    /**
     * The unique id of the player connecting.
     */
    @NonNull private final UUID uniqueId;

    /**
     * The name of the player connecting.
     */
    @NonNull private final String name;
}