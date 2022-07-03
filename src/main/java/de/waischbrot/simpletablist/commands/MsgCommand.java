package de.waischbrot.simpletablist.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;

@CommandAlias("msg|message")
@CommandPermission("basic.msg")
public class MsgCommand extends BaseCommand {

    @Default
    @CatchUnknown
    @CommandCompletion("@players @nothing")
    public void onDefault() {

    }
}
