package com.mouldycheerio.discord.orangepeel.commands;

import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.PeelingUtils;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class SetVotesCommand extends OrangePeelAdminCommand {
    public SetVotesCommand() {
        setName("setVotes");
        setDescription(new CommandDescription("setVotes", "Set the amount of votes a user has (Please don't abuse)", "setVotes <@user> <value>"));
        setCommandlvl(3);
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
            String id = PeelingUtils.mentionToId(args[1], commandMessage.getGuild());
            String arg2 = args[2];
            int v = 0;
            if (arg2.contains("+")) {
                v = Integer.parseInt(arg2.replace("+", ""));
                orangepeel.getVotes().put(id, orangepeel.getVotes().getInt(id) + v);
            } else if (arg2.contains("-")) {
                v = Integer.parseInt(arg2.replace("-", ""));
                orangepeel.getVotes().put(id,  orangepeel.getVotes().getInt(id) - v);

            } else {
                v = Integer.parseInt(arg2);
                orangepeel.getVotes().put(id, v);

            }
            commandMessage.getChannel().sendMessage(args[1] + " now have " + orangepeel.getVotes().getInt(id) + " votes. Hope this was the RIGHT decision and not abusing your premissions, Mr. */* Mrs. " + commandMessage.getAuthor().getName() + "!");



    }
}
