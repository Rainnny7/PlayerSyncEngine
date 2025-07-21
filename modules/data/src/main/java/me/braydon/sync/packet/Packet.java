package me.braydon.sync.packet;

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
public interface Packet {
    class ChannelPrefix {
        public static final String SERVER = "server";
        public static final String PLAYER = "player";
    }
}