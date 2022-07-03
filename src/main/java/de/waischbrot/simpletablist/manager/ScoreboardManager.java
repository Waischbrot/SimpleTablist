package de.waischbrot.simpletablist.manager;

import de.waischbrot.simpletablist.Main;
import de.waischbrot.simpletablist.utils.StringUtil;
import fr.mrmicky.fastboard.FastBoard;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {

    private final Main plugin;
    private final Map<UUID, FastBoard> boards;
    private final List<String> lines;

    public ScoreboardManager(Main plugin) {
        this.plugin = plugin;
        boards = new HashMap<>();

        lines = plugin.getFileHandler().getConfig().getStringList("scoreboard.lines");
    }

    public void addPlayer(Player player) {

        FastBoard board = new FastBoard(player);
        board.updateTitle("");
        boards.put(player.getUniqueId(), board);

    }

    public void removePlayer(Player player) {

        FastBoard board  = boards.get(player.getUniqueId());
        if (board != null) {
            board.delete();
        }

    }

    private void updateBoard(FastBoard board) {

        for (String text : lines) {

            text = StringUtil.getMessageColour(text);
            text =

        }

    }
}
