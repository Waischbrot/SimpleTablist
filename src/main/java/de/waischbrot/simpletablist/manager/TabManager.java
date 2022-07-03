package de.waischbrot.simpletablist.manager;

import de.waischbrot.simpletablist.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TabManager {

    public static void Update(){
        for (Player player : Bukkit.getOnlinePlayers()) {
            //TabList Header
            String HeaderText = StringFormatter.Get(Main.getPlugin().config.getString("Tab.Header"), player);
            player.setPlayerListHeader(HeaderText);

            //TabList Footer
            String FooterText = StringFormatter.Get(Main.getPlugin().config.getString("Tab.Footer"), player);
            player.setPlayerListFooter(FooterText);
        }
    }

}
