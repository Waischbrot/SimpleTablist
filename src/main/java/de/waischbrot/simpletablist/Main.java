package de.waischbrot.simpletablist;

import de.waischbrot.simpletablist.manager.PrefixUpdater;
import de.waischbrot.simpletablist.manager.TabManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Main extends JavaPlugin {

    public FileConfiguration config = getConfig();

    private static Main plugin;

    public static Main getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        config.addDefault("PlaceholderInfo", "Placeholder using PlaceholderAPI [Example: \"Welcome {player_name} on this Server!\"]");
        config.addDefault("Tab.Header", "Welcome on my Server!");
        config.addDefault("Tab.Footer", "Enjoy your stay!");
        config.addDefault("Event.Use", false);
        config.addDefault("Event.JoinMessage", "The Player {player_name} joined the Server!");
        config.addDefault("Event.QuitMessage", "The Player {player_name} left the Server!");
        config.addDefault("Chat.Use", false);
        config.addDefault("Chat.Prefix", "§f[§cSTL§f]");
        config.addDefault("Chat.Separator", " >> ");
        config.addDefault("Chat.Colors", true);
        config.addDefault("Homes.Use", true);
        config.addDefault("Homes.Amount", 5);
        config.addDefault("Plugin.ActionbarMessage", false);
        config.addDefault("Plugin.NoticeMe", "You need LuckPerms to get this Plugin to work!");
        config.addDefault("bstats.Use", true);
        config.options().copyDefaults(true);
        saveConfig();

        new BukkitRunnable() {

            @Override
            public void run() {
                TabManager.Update();
                PrefixUpdater.Update();
            }
        }.runTaskTimer(this, 0, 20L);

        getServer().getPluginManager().registerEvents(new Listeners(), this);
        getCommand("stl").setExecutor(new Commands());
    }

}
