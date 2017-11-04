package com.mouldycheerio.discord.orangepeel.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.mouldycheerio.discord.orangepeel.Logger;
import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class StoryCommand extends OrangePeelCommand {
    public StoryCommand() {
        setName("story");
        setDescription(new CommandDescription("story", "story!", "story"));
        addAlias("bedtimestory");
        setCatagory(CommandCatagory.FUN);
    }

    @Override
    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        String[] split = readFile("stories/story.txt").split("\n");
        IMessage lastMessage = commandMessage.getChannel().sendMessage(split[0]);
        for (int i = 1; i < split.length; i++) {
            String string = split[i];
            lastMessage = nextLineAfterTime(string, lastMessage);
        }
        orangepeel.getStatsCounter().incrementStat("stories");

    }

    private String readFile(String fileName) {
        BufferedReader br = null;

        try {

            br = new BufferedReader(new FileReader(fileName));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            return everything;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return "";
    }

    private IMessage nextLineAfterTime(String line, IMessage lastMessage) {

        sleep(lastMessage.getContent().length() * 100);
        String[] split = line.split("|");
        String fileLink = "";

        if (split.length > 1) {
            fileLink = split[1];
            File file = new File("stories/" + fileLink);
            if (file.exists()) {
                try {
                    return lastMessage.getChannel().sendFile(split[0], file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Logger.warn("404");

                    return lastMessage.getChannel().sendMessage(split[0]);
                }
            } else {
                Logger.warn("404");
            }
        }

        Logger.info("no attachements");
        return lastMessage.getChannel().sendMessage(split[0]);

    }

    private void nextLine(String line, IMessage lastMessage) {
        lastMessage.getChannel().sendMessage(line);
    }

    private void sleep(long t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
