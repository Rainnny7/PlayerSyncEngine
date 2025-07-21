package me.braydon.sync;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.braydon.sync.server.Region;

/**
 * @author Braydon
 */
@AllArgsConstructor @RequiredArgsConstructor
public final class Environment<T> {
    // Redis
    public static final Environment<String> REDIS_URI = new Environment<>("REDIS_URI", String.class);
    public static final Environment<Integer> REDIS_PUBLISHING_THREADS = new Environment<>("REDIS_PUBLISHING_THREADS", Integer.class, 8);
    public static final Environment<Integer> REDIS_RECEIVING_THREADS = new Environment<>("REDIS_PUBLISHING_THREADS", Integer.class, 4);

    // Server
    public static final Environment<Region> SERVER_REGION = new Environment<>("SERVER_REGION", Region.class, Region.NA);

    /**
     * The name of this environment variable.
     */
    @NonNull private final String variable;

    /**
     * The type of this environment variable.
     */
    @NonNull private final Class<?> clazz;

    /**
     * The default value of this environment variable.
     */
    private T defaultValue;

    /**
     * Get the value of this environment variable.
     *
     * @return the value of this environment variable, null if not set
     */
    @SuppressWarnings("unchecked")
    public T getValue() {
        String value = System.getenv(variable);
        return value == null ? defaultValue : (T) clazz.cast(value);
    }
}