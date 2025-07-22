package me.braydon.sync.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import me.braydon.sync.packet.annotation.PacketListener;

import java.lang.reflect.Method;

/**
 * A registered packet listener.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter
public final class RegisteredListener {
    /**
     * The instance of the class that this listener is registered to.
     */
    @NonNull private final Object parent;

    /**
     * The method to invoke when this listener is called.
     */
    @NonNull private final Method method;

    /**
     * The information about this listener.
     */
    @NonNull private final PacketListener annotation;
}