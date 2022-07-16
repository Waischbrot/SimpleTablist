package de.waischbrot.simpletablist.tab;

import de.waischbrot.simpletablist.Main;
import de.waischbrot.simpletablist.util.Pair;
import de.waischbrot.simpletablist.util.SpigotStringUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.ChatMetaNode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.regex.Pattern;

public class TabListManager {
    private final Main plugin;
    private final GroupManager gm;
    private final HashMap<Group, Team> getRelatedTeam = new HashMap<>();
    private String header;
    private String footer;


    public TabListManager(Main plugin, String header, String footer, String formatString) {
        this.plugin = plugin;
        gm = LuckPermsProvider.get().getGroupManager();
        this.header = header;
        this.footer = footer;

        String prefixString = SpigotStringUtil.format(formatString);

        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onChat(AsyncChatEvent ev) {
                ev.renderer((source, sourceDisplayName, message, viewer) -> {
                    var prefix = SpigotStringUtil.getAdventureColour(replaceColors(prefixString
                            .replace("$rank", getRelatedTeam.get(getHighestGroup(source)).getPrefix())
                            .replace("$player_name", source.getName())));
                    Style style = Style.style().hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text(formatDate(new Date())))).build();
                    var component = prefix;
                    component = component.style(style);
                    component = component.content(prefixString
                            .replace("$rank", getRelatedTeam.get(getHighestGroup(source)).getPrefix())
                            .replace("$player_name", source.getName())
                    );
                    if (ev.getPlayer().hasPermission("chat.colors")) {
                        component = component.append(SpigotStringUtil.getAdventureColour(
                                replaceColors(((TextComponent) message.asComponent()).content())
                        ));
                    } else {
                        component = component.append(message);
                    }
                    component = component.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + ev.getPlayer().getName() + " "));
                    return component;
                });
            }
        }, plugin);
    }

    private String replaceColors(String messageString) {
        var pattern = Pattern.compile("#[0-9A-Fa-f]{6}");
        var matcher = pattern.matcher(messageString);
        while (matcher.find()) {
            var colorCode = messageString.substring(matcher.start(), matcher.end());
            messageString = messageString.replace(colorCode, "&"+colorCode);
        }
        return messageString;
    }

    private String formatDate(Date date) {
        return "§6"+formatIntDate(date.getHours()) + ":"+formatIntDate(date.getMinutes())+":"+formatIntDate(date.getSeconds());
    }

    private String formatIntDate(int i) {
        if (i < 10) return "0"+i;
        else return i+"";
    }

    //public

    public void startSorter() {
        new Thread(() -> {

            new Thread(() -> {
                while (plugin.running) {
                    Bukkit.getOnlinePlayers().forEach(this::sendTab);
                    try {
                        Thread.sleep(plugin.waitHeader);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            var groups = getSortedGroups();
            ArrayList<Pair<Group, String>> teams = new ArrayList<>();
            for (Group group : groups) {
                teams.add(new Pair<>(group, SpigotStringUtil.format(getGroupPrefix(group))));
            }
            createTeams(teams);
            new Thread(() -> {
                while (plugin.running) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        //User u = lp.getPlayerAdapter(Player.class).getUser(player);
                        var temp = getHighestGroup(player);
                        if (temp != null)
                            addPlayerToTeam(player,  temp);
                        groups.forEach(group -> {
                            if (group != temp) getRelatedTeam.get(group).removePlayer(player);
                        });
                    }

                    try {
                        Thread.sleep(plugin.waitRank);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }).start();
    }

    public void sendTab(Player p) {
        header = PlaceholderAPI.setPlaceholders(p, header);
        footer = PlaceholderAPI.setPlaceholders(p, footer);
        p.setPlayerListHeaderFooter(header, footer);
    }

    public void sendTab(Collection<? extends Player> players) {
        players.forEach((p) -> p.setPlayerListHeaderFooter(header, footer));
    }

    //private

    private void createTeams(List<Pair<Group, String>> teams) {
        var count = 0;
        var board = Bukkit.getScoreboardManager().getMainScoreboard();
        for (Pair<Group, String> teamNames : teams) {

            if (board.getTeam(count + teamNames.first().getName()) != null) Objects.requireNonNull(board.getTeam(count + teamNames.first().getName())).unregister();
            var team = board.registerNewTeam(count + teamNames.first().getName());

            team.setColor(ChatColor.GRAY);
            getRelatedTeam.put(teamNames.first(), team);
            team.setPrefix(teamNames.second());
            count++;
        }
    }

    private void addPlayerToTeam(Player p, Group g) {
        try {
            getRelatedTeam.get(g).addPlayer(p);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    private List<Group> getSortedGroups() {
        return gm.getLoadedGroups().stream().sorted((o1, o2) -> {
            if (o1.getWeight().isEmpty() && o2.getWeight().isPresent()) return 1;
            if (o2.getWeight().isEmpty() && o1.getWeight().isPresent()) return -1;
            return o2.getWeight().getAsInt() - o1.getWeight().getAsInt();
        }).toList();
    }

    private String getGroupPrefix(Group g) {
        var temp = g.getNodes(NodeType.PREFIX).stream().sorted(Comparator.comparingInt(ChatMetaNode::getPriority)).toList();
        return temp.size() > 0 ? temp.get(0).getMetaValue() : "";
    }

    private Group getHighestGroup(Player p) {
        Group before = null;
        for (Group group : gm.getLoadedGroups()) {
            if (p.hasPermission("group."+group.getName())) {
                if (before == null) before = group;
                if (group.getWeight().isEmpty()) continue;
                if (before.getWeight().isEmpty()) before = group;
                if (group.getWeight().getAsInt() > before.getWeight().getAsInt()) before = group;
            }
        }
        return before;
    }

}