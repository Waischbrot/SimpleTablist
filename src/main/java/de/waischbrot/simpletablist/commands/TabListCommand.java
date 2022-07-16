package de.waischbrot.simpletablist.commands;

import de.waischbrot.simpletablist.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static de.waischbrot.simpletablist.Main.PREFIX;

public record TabListCommand(Main plugin) implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("tabList.admin")) return false;
        if (args.length == 1 && "reload".equals(args[0])) {
            sender.sendMessage(PREFIX + "Reloading config");
            plugin.loadConfig();
            sender.sendMessage(PREFIX + "Config reloaded");
            return true;
        }
        return true;
    }
}
