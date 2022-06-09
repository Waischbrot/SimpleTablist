package de.waischbrot.simpletablist.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Pattern;

public class StringUtil {

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

    public static String replaceColorCodes(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
