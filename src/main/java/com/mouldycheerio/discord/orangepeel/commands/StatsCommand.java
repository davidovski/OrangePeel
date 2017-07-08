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
                        + "Channels = " + statsCounter.getChannels() + "\n"
                        + "Admins = " + statsCounter.getAdmins() + "```\n"

                        + "```java\n"
                        + "Tic Tac Toe Games played = " + statsCounter.getXoxGamesPlayed() + "\n"
                        + "     Bot Wins = " + statsCounter.getXOXwins() + "\n"
                        + "     Bot Losses = " + statsCounter.getXOXlosses() + "\n"
                        + "Rock Paper Scissors Games played = " + statsCounter.getRPSplays() + "\n"
                        + "     Bot Wins = " + statsCounter.getRPSwins() + "\n"
                        + "     Bot Losses = " + statsCounter.getRPSlosses() + "\n"
                        + "     Draws = " + statsCounter.getRPSdraws() + "\n"
                        + "Votes = " + statsCounter.getVotes() + "\n"
                        + "     Positive = " + statsCounter.getUpVotes() + "\n"
                        + "     Negative = " + statsCounter.getDownVotes() + "\n"
                        + "```");
        commandMessage.getChannel().sendMessage("Wow, check that out ^");
    }
}
