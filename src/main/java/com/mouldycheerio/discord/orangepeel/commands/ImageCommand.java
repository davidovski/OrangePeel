package com.mouldycheerio.discord.orangepeel.commands;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class ImageCommand extends OrangePeelCommand {
    public ImageCommand() {
        setName("image");
        setDescription(new CommandDescription("image", "image", "image"));
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        BufferedImage bufferedImage = new BufferedImage(360, 240, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.setPaint(Color.WHITE);
        g2.fill(new Rectangle(0, 0, 360, 240));
        g2.setPaint(Color.GREEN);
        g2.fill(new Rectangle(40, 40, 40, 40));
        g2.setPaint(Color.RED);
        g2.fill(new Rectangle(100, 160, 260, 80));

        try {
            File output = new File("images/output_image.png");
            output.mkdirs();
            boolean write = ImageIO.write(bufferedImage, "PNG", output);
            if (write) {
                commandMessage.getChannel().sendFile(output);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
