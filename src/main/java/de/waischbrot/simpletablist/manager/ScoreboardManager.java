package de.waischbrot.simpletablist.manager;

import de.waischbrot.simpletablist.Main;
import fr.mrmicky.fastboard.FastBoard;

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

        lines = plugin.getFileHandler().getConfig()
    }
}
