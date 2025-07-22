package me.braydon.sync.packet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Braydon
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PacketListener {
    /**
     * The unique id of the packet to listen for.
     *
     * @return the packet id
     */
    String value();

    /**
     * The priority of this packet listener.
     *
     * @return the priority
     */
    Priority priority() default Priority.NORMAL;

    /**
     * Priorities for packet listeners.
     * <p>
     * The lower the priority, the earlier
     * the packet listener will be called.
     * </p>
     */
    enum Priority {
        LOWEST, LOW, NORMAL, HIGH, HIGHEST
    }
}