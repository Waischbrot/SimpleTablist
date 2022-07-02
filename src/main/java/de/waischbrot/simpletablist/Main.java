package de.waischbrot.simpletablist;

import de.waischbrot.simpletablist.listeners.JoinListener;
import de.waischbrot.simpletablist.managers.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private FileManager fileManager;

    @Override
    public void onEnable() {

        fileManager = new FileManager(this);

        Bukkit.getPluginManager().registerEvents(new JoinListener(this), this);

    }

    public FileManager getFileManager() {
        return fileManager;
    }
}
