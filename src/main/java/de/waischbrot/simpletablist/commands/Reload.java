package de.waischbrot.simpletablist.commands;

import de.waischbrot.simpletablist.Main;
import de.waischbrot.simpletablist.manager.NameProvider;
import de.waischbrot.simpletablist.manager.PrefixUpdater;
import de.waischbrot.simpletablist.manager.TabManager;
import de.waischbrot.simpletablist.messages.MessageHandler;
import de.waischbrot.simpletablist.permissions.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class Reload {

    public static void Do(Player player, String[] args) {

        if(Permissions.hasPermission(player, "stl.reload")){
            File file = new File(Main.getPlugin().getDataFolder().getAbsolutePath() + "/config.yml");
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            Main.getPlugin().config = cfg;

            TabManager.Update();
            PrefixUpdater.Update();

            String text = "Successfully reloaded the Config!";
            MessageHandler.Send(player, ChatColor.AQUA + text);
        }
        else{
            String text = "You are not allowed to use this command!";
            MessageHandler.Send(player, ChatColor.YELLOW + text);
        }

    }
}
