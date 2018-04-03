package com.mouldycheerio.discord.orangepeel.commands;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class NsfwCommand extends OrangePeelCommand {

    public NsfwCommand() {
        setName("nsfw");
        setDescription(new CommandDescription("nsfw", "Sends an image that is not safe for work (oranges)!", "nsfw"));
        setCatagory(CommandCatagory.FUN);

    }

    @Override
    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        if (commandMessage.getChannel().isNSFW()) {
            try {
                IMessage message = commandMessage.getChannel().sendMessage("Loading...");
//                URL url = new URL("http://mouldycheerio.com/i/orangePeelDab.png");

                URL url = new URL("https://bot.mouldycheerio.com/api/peeled.jpg");
                BufferedImage img = ImageIO.read(url);
                if (img != null) {

                    File output = new File("peeled.jpg");
                    output.mkdirs();
                    boolean write = ImageIO.write(img, "PNG", output);
                    if (write) {
                        commandMessage.getChannel().sendFile(output);
                        message.delete();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            commandMessage.getChannel().sendMessage(":x: This command can only be run in nsfw channels! :x:");
        }
    }
}
