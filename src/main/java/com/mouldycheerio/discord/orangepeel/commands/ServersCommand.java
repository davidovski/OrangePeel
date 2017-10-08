package com.mouldycheerio.discord.orangepeel.commands;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

public class ServersCommand extends OrangePeelCommand {
    public ServersCommand() {
        setName("servers");
        setDescription(new CommandDescription("servers", "I am in a lot of servers, this will give you a list of them all!", "servers"));
        addAlias("guilds");
        setCatagory(CommandCatagory.ABOUT);
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {

        List<IGuild> guilds = orangepeel.getClient().getGuilds();

        Collections.sort(guilds, new Comparator<IGuild>() {

            public int compare(IGuild o1, IGuild o2) {
                return o2.getTotalMemberCount() - o1.getTotalMemberCount();
            }
        });
        boolean ids = false;
        if (args.length > 1) {
            if (args[1].contains("-")) {
                if (args[1].contains("i")) {
                    ids = true;
                }
            } else {
                if (args[1].equalsIgnoreCase("info")) {
                    if (args[2] != null) {
                        boolean id = true;
                        for (char c : args[2].toCharArray()) {
                            if (!Character.isDigit(c)) {
                                id = false;
                                break;
                            }
                        }
                        IGuild g = null;
                        if (id) {
                            g = client.getGuildByID(Long.parseLong(args[2]));
                        } else {
                            for (IGuild iGuild : guilds) {
                                if (iGuild.getName().split(" ")[0].equals(args[2])) {
                                    g = iGuild;
                                }
                            }
                        }
                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.withTitle("Server:");
                        embedBuilder.withDescription(g.getName());
                        embedBuilder.withThumbnail(g.getIconURL());
                        embedBuilder.withColor(new Color(54, 57, 62));
                        embedBuilder.withAuthorName("OrangePeel");
                        embedBuilder.appendField("ID", g.getStringID(), true);

                        int normal = 0;
                        int bots = 0;
                        for (IUser iUser : g.getUsers()) {
                            if (iUser.isBot()) {
                                bots ++;
                            } else {
                                normal++;
                            }
                        }
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        embedBuilder.appendField("Members", " " + normal, true);
                        embedBuilder.appendField("Bots", " " + bots, true);
                        embedBuilder.appendField("TotalMembers", g.getTotalMemberCount() + "", true);
                        embedBuilder.appendField("Channels", " " + g.getChannels().size(), true);
                        embedBuilder.appendField("Voice Channels", " " + g.getVoiceChannels().size(), true);
                        embedBuilder.appendField("Owner", " " + g.getOwner().getName() + "#" + g.getOwner().getDiscriminator(), true);
                        embedBuilder.appendField("webhooks", " " + g.getWebhooks().size(), true);
                        embedBuilder.appendField("Emojis", " " + g.getEmojis().size(), true);
                        embedBuilder.appendField("Roles", " " + g.getRoles().size(), true);
                        embedBuilder.appendField("CreationDate", g.getCreationDate().format(formatter), true);

                        LocalDateTime ourJoin = g.getJoinTimeForUser(client.getOurUser());

                        String formatDateTime = ourJoin.format(formatter);
                        embedBuilder.appendField("OrangePeel join date:", formatDateTime, true);
                        commandMessage.getChannel().sendMessage(embedBuilder.build());
                        return;
                    }
                }
            }
        }

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
            if (ids) {
                message = message + " (" + g.getStringID() + ")";
            }
        }

        message = message + "```";
        commandMessage.getChannel().sendMessage(message);

    }
}
