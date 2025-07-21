package me.braydon.sync.database.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import io.lettuce.core.support.ConnectionPoolSupport;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import me.braydon.sync.Environment;
import me.braydon.sync.common.Constants;
import me.braydon.sync.common.ThreadPoolBuilder;
import me.braydon.sync.database.IDatabase;
import me.braydon.sync.database.IMessageBus;
import me.braydon.sync.packet.Packet;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.time.Duration;
import java.util.concurrent.ExecutorService;

/**
 * @author Braydon
 */
@Slf4j(topic = "PlayerSyncEngine - Redis")
public final class RedisDatabase implements IDatabase<StatefulRedisConnection<String, String>>, IMessageBus {
    private RedisClient client;
    private StatefulRedisConnection<String, String> standardConnection;

    private GenericObjectPool<StatefulRedisConnection<String, String>> publishingPool;
    private StatefulRedisPubSubConnection<String, String> receivingConnection;
    private RedisPubSubAsyncCommands<String, String> receivingCommands;

    private ExecutorService publishingThreadPool;
    private ExecutorService receivingThreadPool;

    /**
     * Get the name of this database.
     *
     * @return the database name
     */
    @Override @NonNull
    public String getName() {
        return "Redis";
    }

    /**
     * Initialize a connection to this database.
     */
    @Override
    public void connect() {
        log.info("Connecting...");
        long before = System.currentTimeMillis();
        client = RedisClient.create(Environment.REDIS_URI.getValue());
        standardConnection = client.connect(); // Create a standard connection, used for persistent storage

        // Setup publishing, first create a connection pool to utilize, as well as the thread pool
        GenericObjectPoolConfig<StatefulRedisConnection<String, String>> config = buildPublishingPool();
        publishingPool = ConnectionPoolSupport.createGenericObjectPool(() -> client.connect(), config);
        publishingThreadPool = new ThreadPoolBuilder()
                .poolSize(Environment.REDIS_PUBLISHING_THREADS.getValue())
                .threadName("Publishing-Thread")
                .build();

        // Now setup receiving, create a dedicated connection and thread pool
        receivingConnection = client.connectPubSub();
        receivingCommands = receivingConnection.async();
        receivingThreadPool = new ThreadPoolBuilder()
                .poolSize(Environment.REDIS_RECEIVING_THREADS.getValue())
                .threadName("Receiving-Thread")
                .build();
        initializeSubscriber();

        log.info("Connected in {} ms", System.currentTimeMillis() - before);
    }

    /**
     * Get a connection object to this database.
     *
     * @return the connection object
     */
    @Override @NonNull
    public StatefulRedisConnection<String, String> getConnection() {
        return standardConnection;
    }

    /**
     * Initialize the subscriber to handle receiving of messages across the network.
     */
    private void initializeSubscriber() {
        receivingConnection.addListener(new RedisPubSubListener<>() {
            @Override
            public void message(@NonNull String channel, @NonNull String message) { }

            @Override
            public void message(@NonNull String pattern, @NonNull String channel, @NonNull String message) {
                receivingThreadPool.submit(() -> {
                    System.out.println("channel = " + channel);
                    System.out.println("message = " + message);
                });
            }

            @Override
            public void subscribed(@NonNull String channel, long count) { }

            @Override
            public void psubscribed(@NonNull String pattern, long count) { }

            @Override
            public void unsubscribed(@NonNull String channel, long count) { }

            @Override
            public void punsubscribed(@NonNull String pattern, long count) { }
        });
        receivingCommands.psubscribe("server:*", "player:*");
    }

    /**
     * Cleanup any resources used by this database.
     */
    @Override
    public void cleanup() {
        // Cleanup all threads
        publishingThreadPool.shutdownNow();
        receivingThreadPool.shutdownNow();

        // Cleanup all connections
        publishingPool.close();
        receivingConnection.close();
        receivingCommands.shutdown(false);
        standardConnection.close();
        client.shutdown();

        log.info("All clean (:");
    }

    /**
     * Build a connection pool for publishing.
     *
     * @return the connection pool
     */
    @NonNull
    private static GenericObjectPoolConfig<StatefulRedisConnection<String, String>> buildPublishingPool() {
        GenericObjectPoolConfig<StatefulRedisConnection<String, String>> config =
                new GenericObjectPoolConfig<>();
        config.setMaxTotal(20); // More connections for high throughput
        config.setMaxIdle(10);
        config.setMinIdle(5);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(false); // Skip for performance
        config.setTestWhileIdle(true);
        config.setBlockWhenExhausted(false); // Don't block, fail fast
        config.setMaxWait(Duration.ofMillis(100));
        return config;
    }

    /**
     * Publish a packet to the message bus.
     *
     * @param channelPrefix the channel prefix to use
     * @param packet        the packet to publish
     */
    @Override
    public void publish(@NonNull String channelPrefix, @NonNull Packet packet) {
        String channel = channelPrefix + ":" + packet.getClass().getName();
        publishingThreadPool.submit(() -> {
            StatefulRedisConnection<String, String> connection = null;
            try {
                connection = publishingPool.borrowObject();
                RedisAsyncCommands<String, String> commands = connection.async();
                commands.publish(channelPrefix + ":" + packet.getClass().getName(), Constants.GSON.toJson(packet));
            } catch (Exception ex) {
                log.error("Error publishing packet {}:", channel, ex);
            } finally {
                if (connection != null) {
                    try {
                        publishingPool.returnObject(connection);
                    } catch (Exception ignored) { }
                }
            }
        });
    }
}