package me.braydon.sync.packet.impl.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import me.braydon.sync.packet.Packet;
import me.braydon.sync.packet.annotation.PacketId;
import me.braydon.sync.server.MinecraftServer;

import java.util.UUID;

/**
 * A packet sent when a player disconnects
 * from a {@link MinecraftServer}.
 *
 * @author Braydon
 */
@PacketId(Packet.PlayerPacket.DISCONNECT) @Data @EqualsAndHashCode(callSuper = true)
public final class PlayerDisconnectPacket extends Packet {
    /**
     * The unique id of the player connecting.
     */
    @NonNull private final UUID uniqueId;
}