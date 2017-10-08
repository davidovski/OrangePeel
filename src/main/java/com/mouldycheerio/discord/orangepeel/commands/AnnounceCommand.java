package com.mouldycheerio.discord.orangepeel.commands;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.handle.obj.IUser;

public class AnnounceCommand extends OrangePeelAdminCommand {
    public AnnounceCommand() {
        setName("announce");
        setDescription(new CommandDescription("announce", "Announce something\n" + "`-e` mention everyone\n" + "`-p` Private Messages (Default)\n"
                + "`-a` announcements channel for each server \n" + "`-m` main channel \n" + "`-h` current channel \n", "announce -[p, a, m, h, e] <message>"));
        setCommandlvl(3);
        addAlias("say");
        setCatagory(CommandCatagory.BOT_ADMIN);
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        if (args.length >= 2) {
            if (args[1].startsWith("-")) {

                StringBuilder sb = new StringBuilder();
                if (args.length > 1) {
                    for (int i = 2; i < args.length; i++) {
                        if (i > 2) {
                            sb.append(" ");
                        }
                        sb.append(args[i]);
                    }
                }

                String text = sb.toString();

                if (args[1].contains("e")) {
                    text = "@everyone " + text;
                }

                if (args[1].contains("p")) {
                    for (IUser u : client.getUsers()) {
                        if (!u.equals(client.getOurUser())) {
                            IPrivateChannel pm = client.getOrCreatePMChannel(u);
                            pm.sendMessage(text);
                        }
                    }
                }
                if (args[1].contains("a")) {

                }

                if (args[1].contains("m")) {
                    for (IGuild g : client.getGuilds()) {
                        IChannel c = g.getGeneralChannel();
                        c.sendMessage(text);
                    }
                }

                if (args[1].contains("h")) {
                    commandMessage.getChannel().sendMessage(text);
                }
            }
        }
    }
}
