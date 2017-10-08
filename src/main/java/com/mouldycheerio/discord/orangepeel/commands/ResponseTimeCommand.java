package com.mouldycheerio.discord.orangepeel.commands;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class ResponseTimeCommand extends OrangePeelCommand {
    public ResponseTimeCommand() {
        setName("ping");
        setDescription(new CommandDescription("Ping", "Find out how fast the bots running", "ping"));
        addAlias("pi");
        addAlias("pong");
        setCatagory(CommandCatagory.ABOUT);
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        commandMessage.getChannel().sendMessage("I like ping pong too. (" + (commandMessage.getTimestamp().getNano() / 1000000) + "ms)");
        orangepeel.getStatsCounter().incrementStat("pings");

    }
}
