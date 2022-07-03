package de.waischbrot.simpletablist;

import de.waischbrot.simpletablist.files.FileHandler;
import de.waischbrot.simpletablist.listeners.ChatListener;
import de.waischbrot.simpletablist.listeners.JoinQuitListener;
import de.waischbrot.simpletablist.manager.PrefixUpdater;
import de.waischbrot.simpletablist.manager.TabManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.C;

public final class Main extends JavaPlugin {

    private FileHandler fileHandler;

    @Override
    public void onEnable() {

        fileHandler = new FileHandler(this);

        new BukkitRunnable() {

            @Override
            public void run() {
                TabManager.Update();
                PrefixUpdater.Update();
            }
        }.runTaskTimer(this, 0, 20L);

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new JoinQuitListener(), this);
        pm.registerEvents(new ChatListener(), this);

    }

    public FileHandler getFileHandler() {
        return fileHandler;
    }
}
