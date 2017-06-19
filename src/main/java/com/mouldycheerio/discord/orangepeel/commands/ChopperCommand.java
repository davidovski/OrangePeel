package com.mouldycheerio.discord.orangepeel.commands;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class ChopperCommand extends OrangePeelCommand {
    public ChopperCommand() {
        setName("chopper");
        setDescription(new CommandDescription("Chopper", "Get the chopper!", "chopper"));
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        commandMessage.getChannel().sendMessage("```  ROFL:ROFL|ROFL:ROFL" +
"\n         __|___"+
"\n L    __/   []  \\"+
"\nLOL===__         \\"+
"\n L      \\________]"+
"\n        _I___I_____/```");
    }
}
