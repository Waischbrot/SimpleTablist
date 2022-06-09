package de.waischbrot.simpletablist.commands;

import de.waischbrot.simpletablist.Main;
import de.waischbrot.simpletablist.tablist.CustomTablist;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TablistCommand implements CommandExecutor {

    private final Main plugin;

    public TablistCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender,Command command,String string, String[] args) {
        if (!commandSender.hasPermission("tabList.admin")) return false;
        if (args.length == 0) return false;
        switch (args.length) {
            case 1 -> {
                if (args[0].equals("disable")) {
                    plugin.getTabListManager().stopSorter();
                }
                if (args[0].equals("enable")) {
                    plugin.getTabListManager().startSorter();
                }
                return true;
            }
            case 2 -> {
                if (args[0].equals("visibility")) {
                    if (args[1].equals("hide") || args[1].equals("0")) {
                        new Thread(() -> {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                CustomTablist.removePlayers(player);
                            }
                        }).start();
                    }
                    if (args[1].equals("show") || args[1].equals("1")) {
                        new Thread(() -> {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                for (Player elementPlayer : Bukkit.getOnlinePlayers()) {
                                    CustomTablist.addPlayers(player, ((CraftPlayer) elementPlayer).getHandle());
                                }
                            }
                        }).start();
                    }
                }
            }
        }
        return false;
    }
}