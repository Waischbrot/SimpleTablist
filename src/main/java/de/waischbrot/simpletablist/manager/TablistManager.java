package de.waischbrot.simpletablist.manager;

import de.leonhard.storage.Yaml;
import de.waischbrot.simpletablist.Main;
import de.waischbrot.simpletablist.utils.StringUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class TablistManager {

    private final List<String> header;
    private final List<String> footer;

    public TablistManager(Main plugin) {

        Yaml config = plugin.getFileHandler().getConfig();
        header = config.getStringList("tablist.header");
        footer = config.getStringList("tablist.footer");
        int runningDelay = config.getOrSetDefault("tablist.updateTab", 60);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            for (Player player : Bukkit.getOnlinePlayers()) {
                sendHeader(player, header);
                sendFooter(player, footer);
            }

        }, 0, runningDelay);
    }

    private void sendHeader(Player player, List<String> content) {

        Component header = Component.text("");
        for (String text : content) {
            text = PlaceholderAPI.setPlaceholders(player, text);
            StringUtil.getMessageColour(text);
            header = header.append(Component.text(text)).append(Component.newline());
        }
        player.sendPlayerListHeader(header);

    }

    private void sendFooter(Player player, List<String> content) {

        Component footer = Component.text("");
        for (String text : content) {
            text = PlaceholderAPI.setPlaceholders(player, text);
            StringUtil.getMessageColour(text);
            footer = footer.append(Component.text(text)).append(Component.newline());
        }
        player.sendPlayerListFooter(footer);

    }
}
