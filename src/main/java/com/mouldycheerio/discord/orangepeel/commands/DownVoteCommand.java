package com.mouldycheerio.discord.orangepeel.commands;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class DownVoteCommand extends OrangePeelCommand {

    public DownVoteCommand() {
        setName("downvote");
        setDescription(new CommandDescription("Downvote", "Show some hate by taking a vote from a person", "downvote <mention user>"));
        addAlias("dv");
        addAlias("downv");
        addAlias("dislike");
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        if (orangepeel.canVote(commandMessage.getAuthor().getStringID())) {
            String id = mentionToId(args[1]);
            if (id.equals(commandMessage.getAuthor().getStringID())) {
                commandMessage.getChannel().sendMessage("Why would you down vote yourself?!");
                return;
            }
            orangepeel.removeVote(id);
            orangepeel.justVoted(commandMessage.getAuthor().getStringID());
            commandMessage.reply("You down voted  " + args[1] + "! They now have " + orangepeel.getVotes().getInt(id) + " votes! They must be loosing popularity by the minute.");
            orangepeel.getStatsCounter().incrementStat("votes");
            orangepeel.getStatsCounter().incrementStat("downvotes");


        } else {
            commandMessage.reply("You can only do a voting command every 3 hours, come back later, though!");
        }
    }

    public static String mentionToId(String mention) {
        String id = "";
        for (char c : mention.toCharArray()) {
            if (Character.isDigit(c)) {
                id = id + c;
            }
        }
        return id;
    }
}
