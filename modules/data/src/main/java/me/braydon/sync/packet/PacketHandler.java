package me.braydon.sync.packet;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import me.braydon.sync.common.Constants;
import me.braydon.sync.packet.annotation.PacketId;
import me.braydon.sync.packet.annotation.PacketListener;
import me.braydon.sync.packet.impl.player.PlayerConnectPacket;
import me.braydon.sync.packet.impl.player.PlayerDisconnectPacket;
import me.braydon.sync.packet.impl.server.ServerStatusUpdatePacket;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Braydon
 */
@Slf4j(topic = "Packet Handler")
public final class PacketHandler {
    /**
     * The unique id of this server.
     */
    private static UUID serverId;

    /**
     * A registry of all packet types, indexed by their unique id.
     */
    private static final ConcurrentHashMap<String, Class<? extends Packet>> packetTypes = new ConcurrentHashMap<>();
    static {
        // Player
        registerPacket(PlayerConnectPacket.class);
        registerPacket(PlayerDisconnectPacket.class);

        // Server
        registerPacket(ServerStatusUpdatePacket.class);
    }

    /**
     * A registry of all packet listeners.
     */
    private static final ConcurrentHashMap<String, LinkedList<RegisteredListener>> listeners = new ConcurrentHashMap<>();

    /**
     * Initialize the packet handler.
     *
     * @param serverId the id of this server
     */
    public static void initialize(@NonNull UUID serverId) {
        PacketHandler.serverId = serverId;
    }

    /**
     * Register the given packet.
     *
     * @param clazz the class of the packet
     */
    public static void registerPacket(@NonNull Class<? extends Packet> clazz) {
        if (!clazz.isAnnotationPresent(PacketId.class)) {
            throw new IllegalArgumentException("Packet " + clazz.getName() + " is missing the @PacketId annotation!");
        }
        packetTypes.put(Objects.requireNonNull(clazz.getAnnotation(PacketId.class)).value(), clazz);
    }

    /**
     * Register packet listeners in the given class.
     *
     * @param instance the instance of the class to register
     */
    public static void registerListeners(@NonNull Object instance) {
        List<Method> listenerMethods = Arrays.stream(instance.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(PacketListener.class))
                .toList();

        // Register all listeners in the class
        for (Method method : listenerMethods) {
            PacketListener listener = method.getAnnotation(PacketListener.class);
            assert listener != null;

            // Register the listener
            LinkedList<RegisteredListener> packetListeners = listeners.computeIfAbsent(listener.value(), $ -> new LinkedList<>());
            packetListeners.add(new RegisteredListener(instance, method, listener));

            // Sort the listeners by priority (lowest is first)
            packetListeners.sort(Comparator.comparingInt((RegisteredListener registered) -> registered.getAnnotation().priority().ordinal()));
        }
    }

    /**
     * Fire off listeners for the packet with the given id and payload.
     *
     * @param packetId the id of the packet
     * @param payload the payload of the packet
     */
    public static void firePacket(@NonNull String packetId, @NonNull String payload) {
        LinkedList<RegisteredListener> listeners = PacketHandler.listeners.get(packetId);
        if (listeners == null) {
            return;
        }
        Class<? extends Packet> packetType = packetTypes.get(packetId);
        if (packetType == null) { // This instance doesn't know about this packet
            return;
        }
        Packet packet = Constants.GSON.fromJson(payload, packetType);

        // This is the server that sent the packet, ignore it
        if (packet.getServerId().equals(serverId)) {
            return;
        }

        // Invoke each listener for the packet
        listeners.parallelStream().forEach(listener -> {
            try {
                listener.getMethod().invoke(listener.getParent(), packet);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                log.error("Failed to fire packet listener {}:", listener.getMethod(), ex);
            }
        });
    }
}