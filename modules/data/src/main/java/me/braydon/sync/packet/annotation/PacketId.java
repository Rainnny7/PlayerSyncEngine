package me.braydon.sync.packet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Braydon
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PacketId {
    /**
     * The unique id of this packet.
     *
     * @return the packet id
     */
    String value();
}