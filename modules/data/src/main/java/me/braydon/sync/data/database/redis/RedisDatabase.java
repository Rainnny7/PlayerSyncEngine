package me.braydon.sync.data.database.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import me.braydon.sync.data.Environment;
import me.braydon.sync.data.database.IDatabase;
import me.braydon.sync.data.database.IMessageBus;

/**
 * @author Braydon
 */
@Slf4j(topic = "Redis")
public final class RedisDatabase implements IDatabase<StatefulRedisConnection<String, String>>, IMessageBus {
    private RedisClient client;
    private StatefulRedisConnection<String, String> connection;

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
        connection = client.connect();
        log.info("Connected in {} ms", System.currentTimeMillis() - before);
    }

    /**
     * Get a connection object to this database.
     *
     * @return the connection object
     */
    @Override @NonNull
    public StatefulRedisConnection<String, String> getConnection() {
        return connection;
    }

    /**
     * Cleanup any resources used by this database.
     */
    @Override
    public void cleanup() {
        if (connection != null) {
            connection.close();
            connection = null;
        }
        if (client != null) {
            client.shutdown();
            client = null;
        }
        log.info("All clean (:");
    }
}