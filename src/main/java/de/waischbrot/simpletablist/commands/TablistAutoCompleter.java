package de.waischbrot.simpletablist.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

import static de.waischbrot.simpletablist.utils.Utils.stringListOf;

public class TablistAutoCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String string, String[] args) {
        if (args.length == 1) {
            return stringListOf("disable", "enable", "visibility");
        }
        if (args.length == 2) {
            if (args[0].equals("visibility"))
                return stringListOf("0", "1", "hide", "show");
        }
        return null;
    }
}