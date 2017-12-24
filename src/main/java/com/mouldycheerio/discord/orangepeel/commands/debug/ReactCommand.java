package com.mouldycheerio.discord.orangepeel.commands.debug;

import com.mouldycheerio.discord.orangepeel.Logger;
import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.commands.CommandCatagory;
import com.mouldycheerio.discord.orangepeel.commands.CommandDescription;
import com.mouldycheerio.discord.orangepeel.commands.OrangePeelCommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IMessage;

public class ReactCommand extends OrangePeelCommand {
    public ReactCommand() {
        setName("react");
        setDescription(new CommandDescription("react", "reacts to your message ", "react [emoji name]"));
        setCatagory(CommandCatagory.DEBUG);
    }

    @Override
    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        commandMessage.addReaction(ReactionEmoji.of(args[1]));
        Logger.info(args[1]);
    }
}
