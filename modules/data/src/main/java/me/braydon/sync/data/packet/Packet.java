package me.braydon.sync.data.packet;

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
public interface Packet { }