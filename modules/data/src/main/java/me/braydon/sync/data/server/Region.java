package me.braydon.sync.data.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author Braydon
 */
@AllArgsConstructor @Getter
public enum Region {
    NA("North America"),
    EU("Europe"),
    AS("Asia"),
    OC("Oceania"),
    SA("South America");

    /**
     * The name of this region.
     */
    @NonNull private final String name;
}