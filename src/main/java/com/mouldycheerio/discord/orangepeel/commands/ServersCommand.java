package com.mouldycheerio.discord.orangepeel.commands;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
        addAlias("serverinfo");

        setDescription(new CommandDescription("servers", "I am in a lot of servers, this will give you a list of them all!", "servers"));
        addAlias("guilds");
        setCatagory(CommandCatagory.ABOUT);
    }

    @Override
    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        if (args[0].equals(orangepeel.getPrefix() + "serverinfo")) {
            sendServerInfo(client, commandMessage, commandMessage.getGuild());

        } else {
            List<IGuild> guilds = orangepeel.getClient().getGuilds();

            Collections.sort(guilds, new Comparator<IGuild>() {

                @Override
                public int compare(IGuild o1, IGuild o2) {
                    return o2.getUsers().size() - o1.getUsers().size();
                }
            });
            boolean ids = false;
            if (args.length > 1) {
                if (args[1].contains("-")) {
                    if (args[1].contains("i")) {
                        ids = true;
                    }
                } else {
                    if (args[1].equalsIgnoreCase("compare")) {
                        ArrayList<Long> server1Members = new ArrayList<Long>();
                        ArrayList<Long> server2Members = new ArrayList<Long>();
                        ArrayList<Long> mutual = new ArrayList<Long>();

                        IGuild one = client.getGuildByID(Long.parseLong(args[2]));
                        IGuild two = client.getGuildByID(Long.parseLong(args[3]));
                        for (IUser i : one.getUsers()) {
                            if (!i.isBot()) {
                                server1Members.add(i.getLongID());
                            }
                        }

                        for (IUser i : two.getUsers()) {
                            if (!i.isBot()) {

                                server2Members.add(i.getLongID());
                                if (server1Members.contains(i.getLongID())) {
                                    mutual.add(i.getLongID());
                                }
                            }
                        }

                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.withTitle("Comparing " + one.getName() + " and " + two.getName());
                        embedBuilder.withDesc("information");
                        embedBuilder.appendField(one.getName() + "'s Member Count", server1Members.size() + " humans", true);
                        embedBuilder.appendField(two.getName() + "'s Member Count", server2Members.size() + " humans", false);
                        embedBuilder.appendField("Mutual members", mutual.size() + "", false);
                        embedBuilder.appendField(one.getName() + "'s Unique Members", (server1Members.size() - mutual.size()) + "", true);
                        embedBuilder.appendField(two.getName() + "'s Unique Members", (server2Members.size() - mutual.size()) + "", false);

                        commandMessage.getChannel().sendMessage(embedBuilder.build());
                        return;
                    } else if (args[1].equalsIgnoreCase("info")) {
                        if (args.length > 2) {
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
                            sendServerInfo(client, commandMessage, g);
                            return;
                        }
                    }
                }
            }

            String message = "```           Servers         \n(" + guilds.size() + " in total)\n\n";

            int a = 0;
            int ommitted = 0;
            boolean ommittedThis = false;
            int place = 1;
            for (IGuild g : guilds) {
                a++;
                if (a <= 25) {
                    String p = "" + place;
                    int length = p.length();
                    for (int i = length; i < 3; i++) {
                        p =  p + " ";
                    }
                    String members = "" + g.getUsers().size();
                    length = members.length();
                    for (int i = length; i < 8; i++) {
                        if (g.equals(commandMessage.getGuild())) {
                            if (i == length) {
                                members = ">" + members;
                            } else {
                                members = "-" + members;

                            }
                        } else {
                            members = " " + members;
                        }
                    }

                    members = "|" + members + "| ";
                    message = message + "\n" + "|" + p + members + g.getName();
                    if (ids) {
                        message = message + " (" + g.getStringID() + ")";
                    }
                } else {
                    if (g.equals(commandMessage.getGuild())) {
                        ommittedThis = true;
                    } else {
                        ommitted++;
                    }
                }
                place++;
            }

            if (ommittedThis) {
                String members = "" + commandMessage.getGuild().getUsers().size();
                int length = members.length();
                for (int i = length; i < 8; i++) {
                    if (commandMessage.getGuild().equals(commandMessage.getGuild())) {
                        if (i == length) {
                            members = ">" + members;
                        } else {
                            members = "-" + members;

                        }
                    } else {
                        members = " " + members;
                    }
                }

                members = "|" + members + "| ";
                message = message + "\n...\n";
                message = message + "\n" + members + commandMessage.getGuild().getName();
                if (ids) {
                    message = message + " (" + commandMessage.getGuild().getStringID() + ")";
                }
            }

            if (ommitted > 0) {
                message = message + "\nAnd " + ommitted + " more...";
            }

            message = message + "```";
            commandMessage.getChannel().sendMessage(message);
        }
    }

    private void sendServerInfo(IDiscordClient client, IMessage commandMessage, IGuild g) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.withTitle("Server:");
        embedBuilder.withDescription(g.getName());
        embedBuilder.withThumbnail(g.getIconURL());
        embedBuilder.withColor(new Color(54, 57, 62));
        embedBuilder.withAuthorName("OrangePeel");
        embedBuilder.withAuthorUrl("https://bot.mouldycheerio.com");
        embedBuilder.appendField("ID", g.getStringID(), true);

        int normal = 0;
        int bots = 0;
        for (IUser iUser : g.getUsers()) {
            if (iUser.isBot()) {
                bots++;
            } else {
                normal++;
            }
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        embedBuilder.appendField("Humans", " " + normal, true);
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
    }
}
