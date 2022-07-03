package de.waischbrot.simpletablist.commands;

import co.aikar.commands.annotation.*;
import org.bukkit.entity.Player;

@CommandAlias("msg|message")
@CommandPermission("basic.msg")
public class MsgCommand {

    @Default
    @CatchUnknown
    @CommandCompletion("@players @nothing")
    public void onDefault(Player player, String[] args) {

        if (args.length > 1) {

        } else {
            //Benutzung
        }

    }
}
