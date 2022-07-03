package de.waischbrot.simpletablist.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PrefixUpdater {

    public static void Update(){
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setPlayerListName(player.getDisplayName());
        }
    }
}
