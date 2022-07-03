package de.waischbrot.simpletablist.manager;

import de.leonhard.storage.Yaml;
import de.waischbrot.simpletablist.Main;
import de.waischbrot.simpletablist.utils.StringUtil;
import fr.mrmicky.fastboard.FastBoard;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {

    private final Map<UUID, FastBoard> boards;
    private final List<String> lines;

    public ScoreboardManager(Main plugin) {
        boards = new HashMap<>();

        Yaml config = plugin.getFileHandler().getConfig();

        lines = config.getStringList("scoreboard.lines");

        int runningDelay = config.getOrSetDefault("scoreboard.update", 20);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (FastBoard board : boards.values()) {
                updateBoard(board);
            }
        }, 0, runningDelay);
    }

    public void addPlayer(Player player) {

        FastBoard board = new FastBoard(player);
        board.updateTitle("");
        boards.put(player.getUniqueId(), board);

    }

    public void removePlayer(Player player) {

        FastBoard board = boards.remove(player.getUniqueId());
        if (board != null) {
            board.delete();
        }

    }

    private void updateBoard(FastBoard board) {

        List<String> newLines = lines;

        for (String text : newLines) {

            text = PlaceholderAPI.setPlaceholders(board.getPlayer(), text);
            StringUtil.getMessageColour(text);

        }

        board.updateLines(newLines);

    }
}
