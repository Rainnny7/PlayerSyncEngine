package me.braydon.sync.database;

import lombok.NonNull;
import me.braydon.sync.packet.Packet;

import java.util.UUID;

/**
 * A message bus that can be used to send and receive messages.
 *
 * @author Braydon
 */
public interface IMessageBus {
    /**
     * Publish a packet to the message bus.
     *
     * @param channelPrefix the channel prefix to use
     * @param serverId the id of the server that sent the packet
     * @param packet the packet to publish
     */
    void publish(@NonNull String channelPrefix, @NonNull UUID serverId,  @NonNull Packet packet);
}