package com.mouldycheerio.discord.orangepeel.commands;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class DownVoteCommand extends OrangePeelCommand {

    public DownVoteCommand() {
        setName("downvote");
        setDescription(new CommandDescription("Downvote", "Show some hate by taking a vote from a person", "downvote <mention user>"));
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        if (orangepeel.canVote(commandMessage.getAuthor().getStringID())) {
            String id = mentionToId(args[1]);
            if (id.equals(commandMessage.getAuthor().getStringID())) {
                commandMessage.getChannel().sendMessage("You can't vote for yourself mate!");
                return;
            }
            orangepeel.removeVote(id);
            orangepeel.justVoted(commandMessage.getAuthor().getStringID());
            commandMessage.reply("You down voted  " + args[1] + "! They now have " + orangepeel.getVotes().getInt(id) + " votes!");
            orangepeel.getStatsCounter().incrementStat("votes");
            orangepeel.getStatsCounter().incrementStat("downvotes");


        } else {
            commandMessage.reply("You've already voted recently!");
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
