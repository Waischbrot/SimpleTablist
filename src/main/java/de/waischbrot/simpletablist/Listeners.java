package de.waischbrot.simpletablist;

import de.waischbrot.simpletablist.manager.*;
import de.waischbrot.simpletablist.messages.MessageHandler;
import de.waischbrot.simpletablist.permissions.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listeners implements Listener {

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event){
        TabManager.Update();
        PrefixUpdater.Update();
        NameProvider.luckpermsName(event.getPlayer());
    }

    @EventHandler
    public void OnPlayerQuit(PlayerQuitEvent event){
        TabManager.Update();
        PrefixUpdater.Update();
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        String nametag = event.getPlayer().getDisplayName();

        Config.setup(event.getPlayer());
        FileConfiguration con = Config.get();

        if(Permissions.hasPermission(event.getPlayer(), "stl.chat.staff")){
            if(con.getBoolean("Chat.Staff")){
                event.setCancelled(true);
                for(Player player : Bukkit.getOnlinePlayers()){
                    player.sendMessage(Main.getPlugin().config.getString("Chat.Prefix") + " | Staff | " + nametag + StringFormatter.Get(Main.getPlugin().config.getString("Chat.Separator"), event.getPlayer()) + StringFormatter.Get(message, event.getPlayer()));
                }
            }
            else{
                if(con.getBoolean("Chat.Muted")){
                    event.setCancelled(true);
                    MessageHandler.Send(event.getPlayer(), ChatColor.YELLOW + "You are muted!");
                }
                else{
                    event.setCancelled(false);
                    event.setFormat(nametag + StringFormatter.Get(Main.getPlugin().config.getString("Chat.Separator"), event.getPlayer()) + StringFormatter.Get(message, event.getPlayer()));
                }
            }
        }
        else{
            if(con.getBoolean("Chat.Muted")){
                event.setCancelled(true);
                MessageHandler.Send(event.getPlayer(), ChatColor.YELLOW + "You are muted!");
            }
            else{
                event.setCancelled(false);
                event.setFormat(nametag + StringFormatter.Get(Main.getPlugin().config.getString("Chat.Separator"), event.getPlayer()) + StringFormatter.Get(message, event.getPlayer()));
            }
        }
    }
}
