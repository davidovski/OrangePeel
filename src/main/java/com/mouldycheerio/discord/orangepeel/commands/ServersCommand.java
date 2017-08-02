package com.mouldycheerio.discord.orangepeel.commands;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;

public class ServersCommand extends OrangePeelCommand {
    public ServersCommand() {
        setName("servers");
        setDescription(new CommandDescription("servers", "I am in a lot of servers, this will give you a list of them all!", "servers"));
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {

        List<IGuild> guilds = orangepeel.getClient().getGuilds();

        Collections.sort(guilds, new Comparator<IGuild>() {

            public int compare(IGuild o1, IGuild o2) {
                return o2.getTotalMemberCount() - o1.getTotalMemberCount();
            }
        });
        String message = "```           Servers         \n(" + guilds.size() + " in total)\n\n";

        for (IGuild g : guilds) {
            String members = "" + g.getTotalMemberCount();
            for (int i = members.length(); i < 8; i++) {
                members = " " + members;
            }
            if (g.equals(commandMessage.getGuild())) {
                members = "!" + members + "! ";
                message = message + "\n" + members + g.getName();
            } else {
                members = "|" + members + "| ";
                message = message + "\n" + members + g.getName();
            }
        }

        message = message + "```";
        commandMessage.getChannel().sendMessage(message);

    }
}
