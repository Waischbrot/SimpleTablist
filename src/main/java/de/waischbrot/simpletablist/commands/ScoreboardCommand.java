package de.waischbrot.simpletablist.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import de.waischbrot.simpletablist.Main;
import org.bukkit.entity.Player;

@CommandAlias("scoreboard|sb")
public class ScoreboardCommand extends BaseCommand {

    private final Main plugin;

    public ScoreboardCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Default
    @CatchUnknown
    public void onDefault(Player player, String[] args) {

        if (plugin.getScoreboardManager().checkPlayer(player)) {
            plugin.getScoreboardManager().removePlayer(player);
        } else {
            plugin.getScoreboardManager().addPlayer(player);
        }

    }
}
