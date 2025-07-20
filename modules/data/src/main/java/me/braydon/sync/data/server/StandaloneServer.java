package me.braydon.sync.data.server;

import lombok.NonNull;

import java.util.UUID;

/**
 * A standalone Minecraft server, usually running Paper or Spigot.
 *
 * @author Braydon
 */
public final class StandaloneServer extends MinecraftServer {
    public StandaloneServer(@NonNull UUID uuid, @NonNull ServerType type, @NonNull Region region) {
        super(uuid, type, region);
    }
}