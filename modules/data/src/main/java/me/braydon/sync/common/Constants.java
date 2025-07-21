package me.braydon.sync.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;

/**
 * @author Braydon
 */
@UtilityClass
public final class Constants {
    public static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .create();
}