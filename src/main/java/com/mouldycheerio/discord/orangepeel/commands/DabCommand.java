package com.mouldycheerio.discord.orangepeel.commands;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class DabCommand extends OrangePeelCommand {
    private static Random random;

    public static enum ArtType {
        SQUARES, NOISE, CIRCLES, SCENE, PHOTO, PHOTO_NEW
    }

    public DabCommand() {
        setName("dab");
        setDescription(new CommandDescription("dab", "I like to dab", "dab"));
        random = new Random();
        setCatagory(CommandCatagory.FUN);
    }

    @Override
    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        try {

            BufferedImage bi = createDab();
            if (bi != null) {
                try {
                    File output = new File("dab.png");
                    output.mkdirs();
                    boolean write = ImageIO.write(bi, "PNG", output);
                    if (write) {
                        commandMessage.getChannel().sendFile(output);
                        orangepeel.getStatsCounter().incrementStat("dab");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            orangepeel.logError(e1, commandMessage);
        }
    }

    public static BufferedImage createDab() throws IOException {

        URL url = new URL("http://mouldycheerio.com/i/orangePeelDab.png");

        if (Math.random() > 0.5) {
            return flip(ImageIO.read(url));
        } else {
            return ImageIO.read(url);

        }
    }

    public static BufferedImage flip(BufferedImage image) {
        for (int i = 0; i < image.getWidth() / 2; i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int tmp = image.getRGB(i, j);
                image.setRGB(i, j, image.getRGB(image.getWidth() - i - 1, j));
                image.setRGB(image.getWidth() - i - 1, j, tmp);
            }
        }
        return image;
    }

}
