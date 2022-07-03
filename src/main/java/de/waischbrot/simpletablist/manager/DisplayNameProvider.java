package de.waischbrot.simpletablist.manager;

import de.leonhard.storage.Yaml;
import de.waischbrot.simpletablist.Main;
import de.waischbrot.simpletablist.utils.StringUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class DisplayNameProvider {

    public DisplayNameProvider(Main plugin) {

        Yaml config = plugin.getFileHandler().getConfig();
        int runningDelay = config.getOrSetDefault("updatePrefix", 600);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            for (Player player : Bukkit.getOnlinePlayers()) {
                sendTag(player);
            }

        }, 0, runningDelay);
    }

    private void sendTag(Player player) {

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider == null) {
            return;
        }

        LuckPerms api = provider.getProvider();
        CachedMetaData metaData = api.getPlayerAdapter(Player.class).getMetaData(player);

        String prefix = metaData.getPrefix();
        String suffix = metaData.getSuffix();

        if (prefix == null) prefix = "";
        if (suffix == null) suffix = "";

        TextComponent result = Component.text(StringUtil.getMessageColour(prefix + player.getName() + suffix));

        player.displayName(result);
        player.customName(result);
        player.setCustomNameVisible(true);

    }
}
