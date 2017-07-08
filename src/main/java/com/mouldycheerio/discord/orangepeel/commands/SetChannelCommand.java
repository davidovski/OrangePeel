package com.mouldycheerio.discord.orangepeel.commands;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

public class SetChannelCommand extends OrangePeelAdminCommand {
    public SetChannelCommand() {
        setName("setChannel");
        setDescription(new CommandDescription("setChannel", "set the channel for: \n•Suggestions\n•Bug Reports", "setChannel <suggestions, bugs> <#channel>"));
        setCommandlvl(3);
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        if (args.length >= 2) {
            String channelMention = args[2];
            channelMention = channelMention.replace("<#", "");
            channelMention = channelMention.replace(">", "");
            IChannel channel = commandMessage.getGuild().getChannelByID(Long.parseLong(channelMention));

            if (args[1].contains("sug")) {
                orangepeel.setSuggestions(channel);
                commandMessage.reply("Ok, if anyone has a suggestion, I'll post it in <#" + channelMention + "> for developers to implement.");
            } else if (args[1].contains("bug")) {
                orangepeel.setBugReports(channel);
                commandMessage.reply("Ok, if anyone has a bug, I'll post it in <#" + channelMention + "> for developers to look at (and fix?).");
            }

            orangepeel.saveAll();

        }
    }

}
