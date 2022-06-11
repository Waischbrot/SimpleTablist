package de.waischbrot.simpletablist.tablist;

import de.waischbrot.simpletablist.Main;
import de.waischbrot.simpletablist.utils.Pair;
import me.clip.placeholderapi.PlaceholderAPI;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.ChatMetaNode;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class TablistManager {
    private final Main plugin;
    private final GroupManager gm;
    private final HashMap<Group, Team> getRelatedTeam = new HashMap<>();
    private String header;
    private String footer;


    public TablistManager(Main plugin, String header, String footer) {
        this.plugin = plugin;
        gm = LuckPermsProvider.get().getGroupManager();
        this.header = header;
        this.footer = footer;
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
                teams.add(new Pair<>(group, getGroupPrefix(group)));
            }
            createTeams(teams);

            new Thread(() -> {
                while (plugin.running) {

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        //User u = lp.getPlayerAdapter(Player.class).getUser(player);
                        addPlayerToTeam( player, getHighestGroup(player) );
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

    public void stopSorter() {
        getRelatedTeam.forEach(((group, team) -> team.unregister()));
    }

    public void sendTab(Player p) {
        header = PlaceholderAPI.setPlaceholders(p, header);
        footer = PlaceholderAPI.setPlaceholders(p, footer);
        p.setPlayerListHeaderFooter(header, footer);
    }

    @Deprecated
    public void sendTab(Player[] players) {
        sendTabPackets(players);
    }

    public void sendTab(Collection<? extends Player> players) {
        players.forEach((p) -> p.setPlayerListHeaderFooter(header, footer));
    }

    //private

    private void createTeams(List<Pair<Group, String>> teams) {
        var count = 0;
        var board = Bukkit.getScoreboardManager().getMainScoreboard();
        for (Pair<Group, String> teamNames : teams) {

            if (board.getTeam(count + teamNames.v1().getName()) != null) Objects.requireNonNull(board.getTeam(count + teamNames.v1().getName())).unregister();
            var team = board.registerNewTeam(count + teamNames.v1().getName());
            getRelatedTeam.put(teamNames.v1(), team);
            team.setPrefix(teamNames.v2());
            count++;
        }
    }

    private void addPlayerToTeam(Player p, Group g) {
        getRelatedTeam.get(g).addPlayer(p);
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

    @Deprecated
    private void sendTabPackets(Player[] players) {
        if (players == null || players.length == 0)return;

        for (Player p: players) {
            PlaceholderAPI.setPlaceholders(p, header);
            PlaceholderAPI.setPlaceholders(p, footer);
            PlayerConnection connection = ((CraftPlayer) p).getHandle().b;
            IChatBaseComponent header2 = IChatBaseComponent.ChatSerializer.a("{\"text\":\""+header+"\"}");
            IChatBaseComponent footer2 = IChatBaseComponent.ChatSerializer.a("{\"text\":\""+footer+"\"}");

            PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(header2, footer2);
            connection.sendPacket(packet);
        }
    }

}