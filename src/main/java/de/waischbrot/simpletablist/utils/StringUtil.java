package de.waischbrot.simpletablist.utils;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtil {

    public static String getLegacyColour(String text) {

        if (text.contains("#")) {
            Pattern pattern = Pattern.compile("#[a-fA-F\\d]{6}");
            Matcher match = pattern.matcher(text);

            while (match.find()) {
                String color = text.substring(match.start(), match.end());
                text = text.replace(color, ChatColor.of(color) + "");

                match = pattern.matcher(text);
            }
        }

        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static TextComponent getAdventureColour(String text) {

        LegacyComponentSerializer serializer = LegacyComponentSerializer.builder()
                .character('&')
                .hexCharacter('#')
                .hexColors()
                .build();

        return serializer.deserialize(text);
    }
}
