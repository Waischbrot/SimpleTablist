package de.waischbrot.simpletablist.commands;

import de.waischbrot.simpletablist.scoreboard.ScoreBoardManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static de.waischbrot.simpletablist.Main.PREFIX;

public record ScoreboardCommand(
        ScoreBoardManager manager) implements CommandExecutor {

    private void toggle(Player p, CommandSender listener) {
        if (manager.playerHidden(p)) {
            listener.sendMessage(PREFIX + "§aScoreboard enabled");
            manager.showPlayer(p);
        } else {
            listener.sendMessage(PREFIX + "§aScoreboard disabled");
            manager.hidePlayer(p);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("tabList.scoreboard")) {
            sender.sendMessage(PREFIX + "§4Dazu hast du keine Rechte!");
            return true;
        }

        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(PREFIX + "§4You're not a player and therefore don't have a scoreboard.\n You might want to do this: /scoreboard <Player>");
                return true;
            }
            toggle((Player) sender, sender);
            return true;
        }

        if (args.length == 1) {
            if (!(sender.hasPermission("tabList.scoreboard.admin") || sender.hasPermission("tabList.admin"))) {
                sender.sendMessage(PREFIX + "§4Dazu hast du keine Rechte!");
                return true;
            }
            var target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(PREFIX + "§4Invalid Playername");
                return true;
            }
            toggle(target, sender);
            return true;
        }

        sender.sendMessage(PREFIX + "§4Invalid Syntax... Use: /scoreboard OR /scoreboard <Player>");
        return true;
    }
}
