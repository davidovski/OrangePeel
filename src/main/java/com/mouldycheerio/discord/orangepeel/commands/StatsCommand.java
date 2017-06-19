package com.mouldycheerio.discord.orangepeel.commands;

import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.StatsCounter;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class StatsCommand extends OrangePeelCommand {
    public StatsCommand() {
        setName("stats");
        setDescription(new CommandDescription("stats", "how many apples can you put in there?", "stats"));
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        StatsCounter statsCounter = orangepeel.getStatsCounter();
        commandMessage.getChannel()
                .sendMessage("```    Bot Statistics ``` \n"
                        + "```java\n"
                        + "Servers = " + statsCounter.getServers() + "\n"
                        + "Users = " + statsCounter.getUsers() + "\n"
                        + "Channels = " + statsCounter.getChannels() + "```\n"
                        + "```java\n"
                        + "Tic Tac Toe Games played = " + statsCounter.getXoxGamesPlayed() + "\n"
                        + "     Wins = " + statsCounter.getXOXwins() + "\n"
                        + "     Losses = " + statsCounter.getXOXlosses() + "\n"
                        + "Votes = " + statsCounter.getVotes() + "\n"
                        + "     Positive = " + statsCounter.getUpVotes() + "\n"
                        + "     Negative = " + statsCounter.getDownVotes() + "\n"
                        + "```");
    }
}
