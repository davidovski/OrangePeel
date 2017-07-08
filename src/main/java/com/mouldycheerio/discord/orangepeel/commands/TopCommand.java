package com.mouldycheerio.discord.orangepeel.commands;

import org.json.JSONObject;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class TopCommand extends OrangePeelCommand {
    public long lastExec = 0;
    private String content = "d";

    public TopCommand() {
        setName("top");
        setDescription(new CommandDescription("Top", "Display the most popular people across the lands.", "top"));
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        if (commandMessage.getChannel().isPrivate()) {
            content = createBoard(orangepeel, null);
        } else {
            content = createBoard(orangepeel, commandMessage.getGuild());

        }
        lastExec = System.currentTimeMillis();
        commandMessage.getChannel().sendMessage(content);

    }

    public static String createBoard(OrangePeel orangepeel, IGuild g) {
        JSONObject votes = orangepeel.getVotes();
        String[] names = JSONObject.getNames(votes);
        String biggest = "";
        String second = "";
        String third = "";
        for (String name : names) {
            boolean passed = false;

            if (g != null) {
                for (IUser iUser : g.getUsers()) {
                    if (iUser.getStringID().equals(name)) {
                        passed = true;
                        break;
                    }
                }
            } else {
                passed = true;
            }

            if (passed) {
                if (biggest == "") {
                    biggest = name;
                } else {
                    if (votes.getInt(biggest) < votes.getInt(name)) {
                        third = second;
                        second = biggest;
                        biggest = name;
                    } else {
                        if (second == "") {
                            second = name;
                        } else {
                            if (votes.getInt(second) < votes.getInt(name)) {
                                third = second;
                                second = name;
                            } else {
                                if (third == "") {
                                    third = name;
                                } else {
                                    if (votes.getInt(third) < votes.getInt(name)) {
                                        third = name;
                                    } else {
                                        // not in top 3;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        String name1 = "invalid-user";
        String name2 = "invalid-user";
        String name3 = "invalid-user";

        for (IUser iUser : orangepeel.getClient().getUsers()) {
            if (iUser.getStringID().equals(biggest)) {
                name1 = iUser.getName();
            } else if (iUser.getStringID().equals(second)) {
                name2 = iUser.getName();
            } else if (iUser.getStringID().equals(third)) {
                name3 = iUser.getName();
            }
        }

        int bestVotes = orangepeel.getVotes().getInt(biggest);
        int secondVotes = orangepeel.getVotes().getInt(second);
        int thirdVotes = orangepeel.getVotes().getInt(third);

        String content = "__**The Most Popular People:**__\n" + "**(1)**  " + name1 + " with " + bestVotes + " votes\n" + "**(2)**  " + name2 + " with " + secondVotes + " votes\n"
                + "**(3)**  " + name3 + " with " + thirdVotes + " votes\n";
        return content;
    }

}
