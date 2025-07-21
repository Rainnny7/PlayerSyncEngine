package me.braydon.sync.common;

import lombok.experimental.UtilityClass;

/**
 * @author Braydon
 */
@UtilityClass
public final class MathUtils {
    /**
     * Round a number to the given number of decimal places.
     *
     * @param value the value to round
     * @param places the number of decimal places to round to
     * @return the rounded value
     */
    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        double factor = Math.pow(10, places);
        return Math.round(value * factor) / factor;
    }
}