package de.waischbrot.simpletablist.scoreboard;

import de.waischbrot.simpletablist.Main;
import de.waischbrot.simpletablist.util.SpigotStringUtil;
import fr.mrmicky.fastboard.FastBoard;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ScoreBoardManager {
    private final HashMap<UUID, FastBoard> boards;
    private final ArrayList<UUID> noScoreboard = new ArrayList<>();
    private final List<String> content;
    private final String title;

    public ScoreBoardManager(List<String> content, String title, Main plugin) {
        this.boards = new HashMap<>();
        this.content = content;
        this.title = SpigotStringUtil.format(title);

        Bukkit.getScheduler().runTaskTimer(plugin, () -> boards.forEach((id, board) -> updateBoard(board)), 0, 20);

        Bukkit.getOnlinePlayers().forEach(this::addPlayer);

        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onJoin(PlayerJoinEvent ev) {
                addPlayer(ev.getPlayer());
            }

            @EventHandler
            public void onQuit(PlayerQuitEvent ev) {
                removePlayer(ev.getPlayer());
            }
        }, plugin);
    }

    public void addPlayer(Player p) {
        if (hasPlayer(p) || noScoreboard.contains(p.getUniqueId())) return;
        var board = new FastBoard(p);
        updateBoard(board);
        board.updateTitle(PlaceholderAPI.setPlaceholders(p, title));
        boards.put(p.getUniqueId(), board);
    }

    public void removePlayer(Player p) {
        if (!hasPlayer(p)) return;
        var board = boards.remove(p.getUniqueId());
        if (board != null) board.delete();
    }

    public void hidePlayer(Player p) {
        noScoreboard.add(p.getUniqueId());
        removePlayer(p);
    }

    public void showPlayer(Player p) {
        noScoreboard.remove(p.getUniqueId());
        addPlayer(p);
    }

    public boolean playerHidden(Player p) {
        return noScoreboard.contains(p.getUniqueId());
    }

    public void updateBoard(FastBoard board) {
        board.updateLines(content
                .stream()
                .map(it-> PlaceholderAPI.setPlaceholders(board.getPlayer(), SpigotStringUtil.format(it)))
                .toList());
    }

    public boolean hasPlayer(Player p) {
        return boards.containsKey(p.getUniqueId());
    }
}
