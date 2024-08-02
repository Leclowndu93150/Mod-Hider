package com.leclowndu93150.modhider.util;

import net.minecraft.util.text.ITextComponent;

public class TextUtils {
    public static String stripTextFormatting(ITextComponent textComponent) {
        return stripTextFormatting(textComponent.getFormattedText());
    }

    public static String stripTextFormatting(String text) {
        // Remove formatting codes (e.g., §r, §7, §c, etc.)
        return text.replaceAll("(?i)§[0-9a-fk-or]", "");
    }
}
