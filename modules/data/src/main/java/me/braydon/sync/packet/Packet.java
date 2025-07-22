package me.braydon.sync.packet;

import lombok.*;
import me.braydon.sync.server.MinecraftServer;

import java.util.UUID;

/**
 * A packet that is sent across the message bus.
 * <p>
 * Packets are sent using a simple key-value
 * format, where the key is the class name of
 * the packet, and the value is the packet payload.
 * </p>
 *
 * @author Braydon
 */
@Getter
public abstract class Packet {
    /**
     * The unique id of the {@link MinecraftServer} that sent this packet.
     */
    @Setter private UUID serverId;

    /**
     * The channel prefix for a packet.
     */
    public static class ChannelPrefix {
        public static final String SERVER = "server";
        public static final String PLAYER = "player";
    }

    /**
     * {@link MinecraftServer} related packet IDs.
     */
    public static class ServerPacket {
        public static final String STATUS_UPDATE = "server.status_update";
    }

    /**
     * Player related packet IDs.
     */
    public static class PlayerPacket {
        public static final String CONNECT = "player.connect";
        public static final String DISCONNECT = "player.disconnect";
    }
}