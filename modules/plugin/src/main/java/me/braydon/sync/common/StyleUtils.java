package me.braydon.sync.common;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

/**
 * @author Braydon
 */
@UtilityClass
public final class StyleUtils {
    /**
     * Style the given text.
     *
     * @param text the text to style
     * @return the styled text
     */
    @NonNull
    public static Component style(@NonNull String text) {
        return MiniMessage.miniMessage().deserialize(text);
    }

    /**
     * Highlight the given text with the primary color.
     *
     * @param text the text to highlight
     * @return the highlighted text
     */
    @NonNull
    public static String primary(@NonNull String text) {
        return "<gold>" + text + "</gold>";
    }

    /**
     * Highlight the given text with the secondary color.
     *
     * @param text the text to highlight
     * @return the highlighted text
     */
    @NonNull
    public static String secondary(@NonNull String text) {
        return "<gray>" + text + "</gray>";
    }
}