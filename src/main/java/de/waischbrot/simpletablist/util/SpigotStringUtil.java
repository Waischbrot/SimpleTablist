package de.waischbrot.simpletablist.util;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;

import java.util.regex.Pattern;

public class SpigotStringUtil {

    public static String format(String s) {
        var pattern = Pattern.compile("#[0-9A-Fa-f]{6}");
        var matcher = pattern.matcher(s);
        while (matcher.find()) {
            var colorCode = s.substring(matcher.start(), matcher.end());
            s = s.replace(colorCode, net.md_5.bungee.api.ChatColor.of(colorCode) + "");
            matcher = pattern.matcher(s);
        }
        return replaceColorCodes(s);
    }

    public static TextComponent getAdventureColour(String text) {

        LegacyComponentSerializer serializer = LegacyComponentSerializer.builder()
                .character('&')
                .hexCharacter('#')
                .hexColors()
                .build();

        return serializer.deserialize(text);
    }

    public static String replaceColorCodes(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
