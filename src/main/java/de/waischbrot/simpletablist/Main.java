package de.waischbrot.simpletablist;

import de.waischbrot.simpletablist.files.FileHandler;
import de.waischbrot.simpletablist.listeners.ChatListener;
import de.waischbrot.simpletablist.listeners.JoinQuitListener;
import de.waischbrot.simpletablist.manager.DisplayNameProvider;
import de.waischbrot.simpletablist.manager.ScoreboardManager;
import de.waischbrot.simpletablist.manager.TablistManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private FileHandler fileHandler;
    private ScoreboardManager scoreboardManager;
    private TablistManager tablistManager;
    private DisplayNameProvider displayNameProvider;

    @Override
    public void onEnable() {

        fileHandler = new FileHandler(this);
        scoreboardManager = new ScoreboardManager(this);
        tablistManager = new TablistManager(this);
        displayNameProvider = new DisplayNameProvider(this);

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new ChatListener(this), this);
        pm.registerEvents(new JoinQuitListener(this), this);
    }

    public FileHandler getFileHandler() {
        return fileHandler;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public TablistManager getTablistManager() {
        return tablistManager;
    }

    public DisplayNameProvider getDisplayNameProvider() {
        return displayNameProvider;
    }
}
