package com.mouldycheerio.discord.orangepeel.commands.coin;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.commands.CommandCatagory;
import com.mouldycheerio.discord.orangepeel.commands.CommandDescription;
import com.mouldycheerio.discord.orangepeel.commands.OrangePeelCommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;

public class JackieChan extends OrangePeelCommand {
    private static Random random;

    public JackieChan() {
        setName("jackiechan");
        setDescription(new CommandDescription("jackiechan", "Jackie Chan is an mlg god", "jackiechan"));
        random = new Random();
        setCatagory(CommandCatagory.FUN);
        addAlias("jc");
    }

    @Override
    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        if (args.length == 1) {
        File folder = new File("jackiechan");
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            ArrayList<File> filelist = new ArrayList<File>();
            for (File f : files) {
                if (f.getName().startsWith("0")) {
                    filelist.add(f);
                }
            }
            File photo = filelist.get(random.nextInt(filelist.size()));
            System.out.println(photo.getName());
            try {
                commandMessage.getChannel().sendFile("**Jackie Chan**", photo);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        } else {
                StringBuilder sb = new StringBuilder();
                if (args.length > 1) {
                    for (int i = 1; i < args.length; i++) {
                        if (i > 1) {
                            sb.append(" ");
                        }
                        sb.append(args[i]);
                    }
                }
                String text = sb.toString();
                sendJackieChanMessage(text, commandMessage.getChannel());
        }

    }

    public static void sendJackieChanMessage(String content, IChannel channel) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.withAuthorName("Jackie Chan");
        embedBuilder.withAuthorUrl("http://jackiechan.com/");
        embedBuilder.withThumbnail("https://pmcvariety.files.wordpress.com/2017/09/jackie_chan.png");

        embedBuilder.withDescription(content);

        embedBuilder.withColor(new Color(54, 57, 62));

        channel.sendMessage(embedBuilder.build());
    }

}
