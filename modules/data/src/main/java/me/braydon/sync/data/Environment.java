package me.braydon.sync.data;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * @author Braydon
 */
@AllArgsConstructor
public final class Environment<T> {
    public static final Environment<String> REDIS_URI = new Environment<>("REDIS_URI", String.class);

    /**
     * The name of this environment variable.
     */
    @NonNull private final String variable;

    /**
     * The type of this environment variable.
     */
    @NonNull private final Class<?> clazz;

    /**
     * Get the value of this environment variable.
     *
     * @return the value of this environment variable, null if not set
     */
    @SuppressWarnings("unchecked")
    public T getValue() {
        String value = System.getenv(variable);
        return value == null ? null : (T) clazz.cast(value);
    }
}