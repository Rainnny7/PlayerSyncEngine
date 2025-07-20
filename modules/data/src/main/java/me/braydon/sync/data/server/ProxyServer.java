package me.braydon.sync.data.server;

import lombok.NonNull;

import java.util.UUID;

/**
 * A proxy Minecraft server, usually running Velocity or BungeeCord.
 *
 * @author Braydon
 */
public final class ProxyServer extends MinecraftServer {
    public ProxyServer(@NonNull UUID uuid, @NonNull ServerType type, @NonNull Region region) {
        super(uuid, type, region);
    }
}