package com.mouldycheerio.discord.orangepeel.commands;

import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.StatsCounter;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class StatsCommand extends OrangePeelCommand {
    public StatsCommand() {
        setName("stats");
        setDescription(new CommandDescription("stats", "how many apples can you put in there?", "stats"));
        addAlias("statistics");
        setCatagory(CommandCatagory.ABOUT);
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        StatsCounter statsCounter = orangepeel.getStatsCounter();
        commandMessage.getChannel().sendMessage("```    Bot Statistics ``` \n" + "```python\n" + "Servers = " + statsCounter.getServers() + "\n" + "Users = "
                + statsCounter.getUsers() + "\n" + "Channels = " + statsCounter.getChannels() + "```\n"

                + "```python\n" + "Tic Tac Toe Games played = " + statsCounter.getXoxGamesPlayed() + "\n" + "     Bot Wins = " + statsCounter.getXOXwins() + "\n"
                + "     Bot Losses = " + statsCounter.getXOXlosses() + "\n" + "Rock Paper Scissors Games played = " + statsCounter.getRPSplays() + "\n" + "     Bot Wins = "
                + statsCounter.getRPSwins() + "\n" + "     Bot Losses = " + statsCounter.getRPSlosses() + "\n" + "     Draws = " + statsCounter.getRPSdraws() + "\n" + "Votes = "
                + statsCounter.getVotes() + "\n" + "     Positive = " + statsCounter.getUpVotes() + "\n" + "     Negative = " + statsCounter.getDownVotes() + "\n"
                + "```\n```python\n" + "Bedtime Stories Read = " + statsCounter.getStat("stories") + "\n" + "People Rated = " + statsCounter.getStat("rates") + "\n"
                + "Artwork Painted = " + statsCounter.getStat("art") + "\n" + "Helicopters flown = " + statsCounter.getStat("choppers") + "\n" + "Text Drawn (prettily) = "
                + statsCounter.getStat("ascii") + "\n" + "House adresses stolen = " + statsCounter.getStat("coords") + "\n"

                + "Messages Received = " + statsCounter.getStat("messages") + "\n" + "Orders Executed = " + statsCounter.getStat("commands") + "```\n" + "```python\n"
                + "Loads from disk = " + statsCounter.getStat("loads") + "\n" + "Saves to disk = " + statsCounter.getStat("saves") + "\n" + "Boots = "
                + statsCounter.getStat("boots") + "```\n");
        commandMessage.getChannel().sendMessage("Wow, check that out ^");
    }
}
