package de.waischbrot.simpletablist.messages;

import de.waischbrot.simpletablist.Main;
import de.waischbrot.simpletablist.manager.StringFormatter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public final class MessageHandler {

    public static void Send(Player player, String text){
        String prefix = Main.getPlugin().config.getString("Chat.Prefix");
        if(Main.getPlugin().config.getBoolean("Plugin.ActionbarMessage")){
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(prefix + " " + StringFormatter.Get(text, player)));
        }
        else{
            player.sendMessage(prefix + " " + StringFormatter.Get(text, player));
        }
    }
}
