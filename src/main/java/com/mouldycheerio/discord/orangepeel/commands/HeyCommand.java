package com.mouldycheerio.discord.orangepeel.commands;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class HeyCommand extends OrangePeelCommand {
    public HeyCommand() {
        setName("hey");
        setDescription(new CommandDescription("Hey", "Hey!", "hey"));
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        commandMessage.getChannel().sendMessage("Hey!");
    }
}
