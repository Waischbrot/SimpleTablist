package de.waischbrot.simpletablist.manager;

import de.leonhard.storage.Yaml;
import de.waischbrot.simpletablist.Main;
import de.waischbrot.simpletablist.utils.StringUtil;
import fr.mrmicky.fastboard.FastBoard;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class ScoreboardManager {

    private final Map<UUID, FastBoard> boards;
    private final List<String> lines;
    private final String title;

    public ScoreboardManager(Main plugin) {
        boards = new HashMap<>();

        Yaml config = plugin.getFileHandler().getConfig();

        lines = config.getStringList("scoreboard.lines");
        title = config.getString("scoreboard.title");

        int runningDelay = config.getOrSetDefault("scoreboard.update", 20);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (FastBoard board : boards.values()) {
                updateBoard(board);
            }
        }, 0, runningDelay);
    }

    public void addPlayer(Player player) {

        FastBoard board = new FastBoard(player);
        String bTitle = StringUtil.getLegacyColour(PlaceholderAPI.setPlaceholders(board.getPlayer(), title));
        board.updateTitle(bTitle);
        boards.put(player.getUniqueId(), board);

    }

    public void removePlayer(Player player) {

        FastBoard board = boards.remove(player.getUniqueId());
        if (board != null) {
            board.delete();
        }

    }

    public boolean checkPlayer(Player player) {

        return boards.containsKey(player.getUniqueId());

    }

    private void updateBoard(FastBoard board) {

        List<String> newLines = new ArrayList<>();

        for (String text : lines) {

            text = PlaceholderAPI.setPlaceholders(board.getPlayer(), text);
            newLines.add(StringUtil.getLegacyColour(text));

        }

        board.updateLines(newLines);

    }
}
