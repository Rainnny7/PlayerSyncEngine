package me.braydon.sync.common;

import lombok.*;
import lombok.experimental.UtilityClass;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author Braydon
 */
@UtilityClass
public final class TimeUtils {
    /**
     * Format a time in millis to a readable time format.
     *
     * @param millis the millis to format
     * @return the formatted time
     */
    @NonNull
    public static String format(long millis) {
        return format(millis, WildTimeUnit.FIT);
    }

    /**
     * Format a time in millis to a readable time format.
     *
     * @param millis the millis to format
     * @param timeUnit the time unit to format the millis to
     * @return the formatted time
     */
    @NonNull
    public static String format(long millis, @NonNull WildTimeUnit timeUnit) {
        return format(millis, timeUnit, false);
    }

    /**
     * Format a time in millis to a readable time format.
     *
     * @param millis the millis to format
     * @param timeUnit the time unit to format the millis to
     * @param compact whether to use a compact display
     * @return the formatted time
     */
    @NonNull
    public static String format(long millis, @NonNull WildTimeUnit timeUnit, boolean compact) {
        return format(millis, timeUnit, true, compact);
    }

    /**
     * Format a time in millis to a readable time format.
     *
     * @param millis the millis to format
     * @param timeUnit the time unit to format the millis to
     * @param decimals whether to include decimals
     * @param compact whether to use a compact display
     * @return the formatted time
     */
    @NonNull
    public static String format(long millis, @NonNull WildTimeUnit timeUnit, boolean decimals, boolean compact) {
        if (millis == -1L) { // Format permanent
            return "Perm" + (compact ? "" : "anent");
        }
        // Format the time to the best fitting time unit
        if (timeUnit == WildTimeUnit.FIT) {
            for (WildTimeUnit otherTimeUnit : WildTimeUnit.VALUES) {
                if (otherTimeUnit != WildTimeUnit.FIT && millis >= otherTimeUnit.getMillis()) {
                    timeUnit = otherTimeUnit;
                    break;
                }
            }
        }
        double time = MathUtils.round((double) millis / timeUnit.getMillis(), 1); // Format the time
        if (!decimals) { // Remove decimals
            time = (int) time;
        }
        String formatted = time + (compact ? timeUnit.getSuffix() : " " + timeUnit.getDisplay()); // Append the time unit
        if (time != 1.0 && !compact) { // Pluralize the time unit
            formatted+= "s";
        }
        return formatted;
    }

    /**
     * Represents a unit of time.
     */
    @NoArgsConstructor @AllArgsConstructor
    @Getter(AccessLevel.PRIVATE) @ToString
    public enum WildTimeUnit {
        FIT,
        YEARS("Year", "y", TimeUnit.DAYS.toMillis(365L)),
        MONTHS("Month", "mo", TimeUnit.DAYS.toMillis(30L)),
        WEEKS("Week", "w", TimeUnit.DAYS.toMillis(7L)),
        DAYS("Day", "d", TimeUnit.DAYS.toMillis(1L)),
        HOURS("Hour", "h", TimeUnit.HOURS.toMillis(1L)),
        MINUTES("Minute", "m", TimeUnit.MINUTES.toMillis(1L)),
        SECONDS("Second", "s", TimeUnit.SECONDS.toMillis(1L)),
        MILLISECONDS("Millisecond", "ms", 1L);

        /**
         * Our cached unit values.
         */
        public static final WildTimeUnit[] VALUES = values();

        /**
         * Our cached suffix pattern.
         */
        public static final Pattern SUFFIX_PATTERN = Pattern.compile("(\\d+)(mo|ms|[ywdhms])");

        /**
         * The display of this time unit.
         */
        private String display;

        /**
         * The suffix of this time unit.
         */
        private String suffix;

        /**
         * The amount of millis in this time unit.
         */
        private long millis;

        /**
         * Get the time unit with the given suffix.
         *
         * @param suffix the time unit suffix
         * @return the time unit, null if not found
         */
        public static WildTimeUnit fromSuffix(@NonNull String suffix) {
            for (WildTimeUnit unit : VALUES) {
                if (unit != FIT && unit.getSuffix().equals(suffix)) {
                    return unit;
                }
            }
            return null;
        }
    }
}