package me.braydon.sync.data.database;

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
     * Cleanup any resources used by this database.
     */
    void cleanup();
}