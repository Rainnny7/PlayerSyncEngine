package me.braydon.sync.packet.impl.server;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import me.braydon.sync.packet.Packet;
import me.braydon.sync.packet.annotation.PacketId;
import me.braydon.sync.server.MinecraftServer;

/**
 * This packet is sent when a {@link MinecraftServer} has changed to its status.
 *
 * @author Braydon
 */
@PacketId(Packet.ServerPacket.STATUS_UPDATE) @Data @EqualsAndHashCode(callSuper = true)
public final class ServerStatusUpdatePacket extends Packet {
    /**
     * The server that was updated, null if the server is going offline.
     */
    private final MinecraftServer server;
}