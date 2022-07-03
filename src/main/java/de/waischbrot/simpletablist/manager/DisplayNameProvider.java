package de.waischbrot.simpletablist.manager;

import de.leonhard.storage.Yaml;
import de.waischbrot.simpletablist.Main;
import de.waischbrot.simpletablist.utils.StringUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class DisplayNameProvider {

    private LuckPerms api;

    public DisplayNameProvider(Main plugin) {

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider == null) {
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }
        api = provider.getProvider();

        Yaml config = plugin.getFileHandler().getConfig();
        int runningDelay = config.getOrSetDefault("updatePrefix", 600);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            for (Player player : Bukkit.getOnlinePlayers()) {
                updateName(player);
            }

        }, 0, runningDelay);
    }

    private void updateName(Player player) {

        CachedMetaData metaData = api.getPlayerAdapter(Player.class).getMetaData(player);

        String prefix = metaData.getPrefix();
        String suffix = metaData.getSuffix();

        if (prefix == null) prefix = "";
        if (suffix == null) suffix = "";

        TextComponent result = StringUtil.getAdventureColour(prefix + player.getName() + suffix);

        player.displayName(result);
        player.playerListName(result);
        player.customName(result);
        player.setCustomNameVisible(true);

    }
}
