package de.waischbrot.simpletablist.manager;

import de.waischbrot.simpletablist.utils.StringUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

public class TablistManager {

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
