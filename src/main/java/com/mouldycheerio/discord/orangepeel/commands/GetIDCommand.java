package com.mouldycheerio.discord.orangepeel.commands;

import java.util.ArrayList;

import com.mouldycheerio.discord.orangepeel.Logger;
import com.mouldycheerio.discord.orangepeel.Member;
import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class GetIDCommand extends OrangePeelCommand {
    public java.util.List<Member> games = new ArrayList<Member>();

    public GetIDCommand() {
        setName("getIDs");
        setDescription(new CommandDescription("getIDs", "getIDs!", "getIDs"));
        setCatagory(CommandCatagory.DEBUG);

    }

    @Override
    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        client.getDispatcher().registerListener(this);
        StringBuilder sb = new StringBuilder();
        if (args.length > 1) {
            for (int i = 1; i < args.length; i++) {
                if (i > 1) {
                    sb.append(" ");
                }
                sb.append(args[i]);
            }
        }

        String ids = "```";

        String name = sb.toString();
        boolean found = false;
        for (IUser u : client.getUsers()) {
            if (name.contains(u.getName())) {
                found = true;
                ids = ids + "\n" + u.getLongID() + " : " + u.getName() + "#" + u.getDiscriminator();
            }
        }
        if (!found) {
            ids = ids + "No match";
        }

        ids = ids + "```";
        commandMessage.getChannel().sendMessage(ids);

        Logger.info("found");
    }

}
