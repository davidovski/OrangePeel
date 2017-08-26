package com.mouldycheerio.discord.orangepeel.commands;

import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.PeelingUtils;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class VotesCommand extends OrangePeelCommand {
    public long lastExec = 0;
    private String content = "d";

    public VotesCommand() {
        setName("votes");
        setDescription(new CommandDescription("votes", "How popular are they?", "votes <@mention>"));
        addAlias("howpopular");
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        String id = commandMessage.getAuthor().getStringID();
        if (args.length > 1) {
        id = PeelingUtils.mentionToId(args[1], commandMessage.getGuild());
        if (orangepeel.getVotes().has(id))  {
            commandMessage.getChannel().sendMessage("That person has "  + orangepeel.getVotes().getInt(id) + " votes!");
        } else {
            commandMessage.getChannel().sendMessage("Erm, who is that? They must be so unpopular!");
        } return;
        }

        if (orangepeel.getVotes().has(id))  {
            commandMessage.getChannel().sendMessage("You have "  + orangepeel.getVotes().getInt(id) + " votes!");
        } else {
            commandMessage.getChannel().sendMessage("Erm, hi, i guess. You don't seem to be voted for much...");
        }


    }



}
