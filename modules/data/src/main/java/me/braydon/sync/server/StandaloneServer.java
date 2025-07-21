package me.braydon.sync.server;

import lombok.NonNull;

import java.util.UUID;

/**
 * A standalone Minecraft server, usually running <a href="https://papermc.io">Paper</a>.
 *
 * @author Braydon
 */
public final class StandaloneServer extends MinecraftServer {
    public StandaloneServer(@NonNull UUID uuid, @NonNull ServerType type, @NonNull Region region) {
        super(uuid, type, region);
    }
}