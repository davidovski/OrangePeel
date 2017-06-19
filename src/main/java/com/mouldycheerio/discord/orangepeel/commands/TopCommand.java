package com.mouldycheerio.discord.orangepeel.commands;

import org.json.JSONObject;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class TopCommand extends OrangePeelCommand {
    public long lastExec = 0;
    private String content = "d";

    public TopCommand() {
        setName("top");
        setDescription(new CommandDescription("Top", "Display the most popular people across the lands.", "top"));
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        content = createBoard(orangepeel);
        lastExec = System.currentTimeMillis();
        commandMessage.getChannel().sendMessage(content);

    }

    public static String createBoard(OrangePeel orangepeel) {
        JSONObject votes = orangepeel.getVotes();
        String[] names = JSONObject.getNames(votes);
        String biggest = "";
        String second = "";
        String third = "";
        for (String name : names) {
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

        int bestVotes = orangepeel.getVotes().getInt(biggest);
        int secondVotes = orangepeel.getVotes().getInt(second);
        int thirdVotes = orangepeel.getVotes().getInt(third);

        String content = "__**The Most Popular People:**__\n" + "**(1)**  <@" + biggest + "> with " + bestVotes + " votes\n" + "**(2)**  <@" + second + "> with " + secondVotes
                + " votes\n" + "**(3)**  <@" + third + "> with " + thirdVotes + " votes\n";
        return content;
    }

}
