package com.mouldycheerio.discord.orangepeel.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class JackieChanCommand extends OrangePeelCommand {
    private static Random random;

    public static enum ArtType {
        SQUARES, NOISE, CIRCLES, SCENE, PHOTO
    }

    public JackieChanCommand() {
        setName("jackiechan");
        setDescription(new CommandDescription("jackiechan", "Jackie Chan is an mlg god", "jackiechan"));
        random = new Random();
        setCatagory(CommandCatagory.FUN);
    }

    @Override
    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
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

    }

}
