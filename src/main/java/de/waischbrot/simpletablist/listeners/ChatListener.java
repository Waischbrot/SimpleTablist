package de.waischbrot.simpletablist.listeners;

import de.waischbrot.simpletablist.Main;
import de.waischbrot.simpletablist.utils.StringUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;

public class ChatListener implements Listener {

    private String format;
    private LuckPerms api;

    public ChatListener(Main plugin) {

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider == null) {
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }
        api = provider.getProvider();

        format = plugin.getFileHandler().getConfig().getString("chat.format");
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {

        Player player = event.getPlayer();
        TextComponent content = Component.text("").append(event.message());

        String legacyMessage = content.content();

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (legacyMessage.contains("@" + p.getName())) {
                legacyMessage = legacyMessage.replace('@' + p.getName(), "&b@" + p.getName());
                p.playSound(Sound.sound(Key.key("block.note_block.chime"), Sound.Source.AMBIENT, 4f, 1f));
            }
        }

        CachedMetaData metaData = api.getPlayerAdapter(Player.class).getMetaData(player);

        String prefix = metaData.getPrefix();
        String suffix = metaData.getSuffix();
        if (prefix == null) prefix = "";
        if (suffix == null) suffix = "";

        String mF = format;

        mF = mF.replace("%player%", player.getName());
        mF = mF.replace("%prefix%", prefix);
        mF = mF.replace("%suffix%", suffix);
        mF = mF.replace("%message%", legacyMessage);

        TextComponent message = StringUtil.getAdventureColour(mF);
        event.message(message);
    }
}
