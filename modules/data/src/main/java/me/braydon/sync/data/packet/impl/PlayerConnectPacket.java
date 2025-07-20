package me.braydon.sync.data.packet.impl;

import lombok.Data;
import lombok.NonNull;
import me.braydon.sync.data.packet.Packet;
import me.braydon.sync.data.server.ProxyServer;
import me.braydon.sync.data.server.StandaloneServer;

import java.util.UUID;

/**
 * A packet sent when a player connects to either
 * a {@link ProxyServer} or {@link StandaloneServer}.
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
     * The UUID of the {@link ProxyServer} this player
     * is connecting to, null if this is a standalone server.
     */
    private final UUID proxyId;

    /**
     * The UUID of the {@link StandaloneServer} this
     * player is connecting to, null if this is a proxy server.
     */
    private final UUID serverId;
}