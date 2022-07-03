package de.waischbrot.simpletablist.manager;

import de.waischbrot.simpletablist.Main;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {

    private final Main plugin;
    private final Map<UUID, FastBoard> boards;

    public ScoreboardManager(Main plugin) {
        this.plugin = plugin;
        boards = new HashMap<>();


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

        board.updateLines(
                "",
                "Players: " + Bukkit.getOnlinePlayers().size(),
                "",
                "Kills: " + board.getPlayer().getStatistic(Statistic.PLAYER_KILLS),
                ""
        );
    }
}
