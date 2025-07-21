package me.braydon.sync.common;

import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple builder for creating thread pools.
 *
 * @author Braydon
 */
@Setter @Accessors(fluent = true, chain = true)
public final class ThreadPoolBuilder {
    /**
     * The size of the thread pool.
     */
    private int poolSize;

    /**
     * The name of the threads within the pool.
     */
    private String threadName;

    /**
     * Build the thread pool.
     *
     * @return the thread pool
     */
    @NonNull
    public ExecutorService build() {
        AtomicInteger threadId = new AtomicInteger(0);
        return Executors.newFixedThreadPool(poolSize, task -> {
            Thread thread = new Thread(task, threadName + "-" + threadId.incrementAndGet());
            thread.setDaemon(true);
            return thread;
        });
    }
}