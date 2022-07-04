package de.waischbrot.simpletablist.manager;

import de.waischbrot.simpletablist.Main;
import de.waischbrot.simpletablist.utils.StringUtil;
import net.kyori.adventure.text.TextComponent;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scoreboard.Team;

import java.util.Objects;

public class DisplayNameProvider {

    private LuckPerms api;

    public DisplayNameProvider(Main plugin) {

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider == null) {
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }
        api = provider.getProvider();

    }

    public void updateName(Player player) {

        CachedMetaData metaData = api.getPlayerAdapter(Player.class).getMetaData(player);

        String prefix = metaData.getPrefix();
        String suffix = metaData.getSuffix();

        if (prefix == null) prefix = "";
        if (suffix == null) suffix = "";

        TextComponent result = StringUtil.getAdventureColour(prefix + player.getName() + suffix);

        player.displayName(result);
        player.playerListName(result);

        User user = api.getUserManager().getUser(player.getUniqueId());
        Group group = api.getGroupManager().getGroup(Objects.requireNonNull(user).getPrimaryGroup());
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(Objects.requireNonNull(group).getWeight() + group.getFriendlyName());
        team.displayName(result);
        team.addEntry(player.getName());

    }
}
