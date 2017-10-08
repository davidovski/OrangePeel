package com.mouldycheerio.discord.orangepeel.commands;

import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.PeelingUtils;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class VoteCommand extends OrangePeelCommand {

    public VoteCommand() {
        setName("vote");
        setDescription(new CommandDescription("Vote", "Vote for your favourite person!", "vote <mentionuser>"));
        addAlias("v");
        addAlias("like");
        setCatagory(CommandCatagory.VOTING);
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        if (orangepeel.canVote(commandMessage.getAuthor().getStringID())) {
            String id = PeelingUtils.mentionToId(args[1], commandMessage.getGuild());
            if (id.equals(commandMessage.getAuthor().getStringID())) {
                commandMessage.getChannel().sendMessage("You can't vote for yourself mate!");
                return;
            }
            orangepeel.addVote(id);
            orangepeel.justVoted(commandMessage.getAuthor().getStringID());
            commandMessage.reply("You voted for " + args[1] + "! They now have " + orangepeel.getVotes().getInt(id) + " votes! (wow)");
            orangepeel.getStatsCounter().incrementStat("votes");
            orangepeel.getStatsCounter().incrementStat("upvotes");

        } else {
            commandMessage.reply("You've already voted in the past hour!");
        }
    }


}
