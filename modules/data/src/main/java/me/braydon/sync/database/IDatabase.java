package me.braydon.sync.database;

import lombok.NonNull;

/**
 * @author Braydon
 */
public interface IDatabase<T> {
    /**
     * Get the name of this database.
     *
     * @return the database name
     */
    @NonNull String getName();

    /**
     * Initialize a connection to this database.
     */
    void connect();

    /**
     * Get a connection object to this database.
     *
     * @return the connection object
     */
    @NonNull T getConnection();

    /**
     * Get the ping to this database.
     *
     * @return the ping in milliseconds
     */
    long getPing();

    /**
     * Cleanup any resources used by this database.
     */
    void cleanup();
}