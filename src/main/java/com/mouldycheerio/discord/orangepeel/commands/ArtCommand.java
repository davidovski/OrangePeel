package com.mouldycheerio.discord.orangepeel.commands;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class ArtCommand extends OrangePeelCommand {
    private static Random random;

    public static enum ArtType {
        SQUARES, NOISE, CIRCLES, SCENE, PHOTO
    }

    public ArtCommand() {
        setName("art");
        setDescription(new CommandDescription("art", "create some modern art", "art [type]"));
        random = new Random();
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {

        try {
            int typen = random.nextInt(15);
            ArtType type = ArtType.SQUARES;
            if (typen < 3) {
                type = ArtType.SQUARES;
            } else if (typen < 6) {
                type = ArtType.CIRCLES;
            } else if (typen < 8) {
                type = ArtType.SCENE;
            } else if (typen < 15) {
                type = ArtType.PHOTO;
            }
            String name = ArtNameGenerator.makename(type);
            BufferedImage bi = createArt(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)), type);
            if (bi != null) {

                try {
                    File output = new File("art/" + name + ".png");
                    int i = 0;
                    if (output.exists()) {
                        while (output.exists()) {
                            i++;
                            output = new File("art/" + name + i + ".png");

                        }
                    }
                    output.mkdirs();
                    boolean write = ImageIO.write(bi, "PNG", output);
                    if (write) {

                        ArrayList<String> options = new ArrayList<String>();
                        options.add("This one is called: ");
                        options.add("I made this one, i called it");
                        options.add("Tah, Dah! I present you ");
                        options.add("I present to you: ");
                        options.add("This one is called ");
                        options.add("I like this one. I called it ");
                        options.add("I made this one just for you: ");
                        options.add("Check this one out! ");
                        options.add("Voila! ");
                        options.add("Do you like it? ");
                        options.add("This one's called ");
                        options.add("This one took ages! ");
                        options.add("I present to you ");
                        options.add("");

                        commandMessage.getChannel().sendFile(options.get(random.nextInt(options.size())) + " **" + name + "**", output);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    public static BufferedImage createArt(Color color, ArtType type) throws IOException {

        if (type == ArtType.SQUARES) {
            BufferedImage buffimg = new BufferedImage(360, 240, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = buffimg.createGraphics();
            int width = buffimg.getWidth();
            int height = buffimg.getHeight();
            g2.setPaint(color);
            g2.fill(new Rectangle(0, 0, width, height));
            for (int i = 0; i < random.nextInt(500); i++) {
                int r = color.getRed();
                int g = color.getBlue();
                int b = color.getGreen();
                if (random.nextInt(10) == 0) {
                    r = g + (random.nextInt(100) - 50);
                    g = b + (random.nextInt(100) - 50);
                    b = r + (random.nextInt(100) - 50);
                } else {
                    r = r + (random.nextInt(100) - 50);
                    g = g + (random.nextInt(100) - 50);
                    b = b + (random.nextInt(100) - 50);
                }

                if (r > 255) {
                    r = 255;
                }
                if (r < 0) {
                    r = 0;
                }
                if (g > 255) {
                    g = 255;
                }
                if (g < 0) {
                    g = 0;
                }
                if (b > 255) {
                    b = 255;
                }
                if (b < 0) {
                    b = 0;
                }

                Color color2 = new Color(r, g, b);
                g2.setPaint(color2);
                g2.fill(new Rectangle(random.nextInt(width), random.nextInt(height), random.nextInt(width / 2), random.nextInt(height / 2)));
            }
            return buffimg;
        }
        if (type == ArtType.SCENE) {
            BufferedImage buffimg = new BufferedImage(360, 240, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = buffimg.createGraphics();
            int width = buffimg.getWidth();
            int height = buffimg.getHeight();
            int sat = (int) Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), new float[3])[0] * 255;
            int blue = random.nextInt(100) + 155;
            int green = random.nextInt(100) + 155;

            g2.setPaint(new Color(sat, sat, blue));
            g2.fill(new Rectangle(0, 0, width, height));

            float halver = 2;

            for (int i = 0; i < random.nextInt(500); i++) {
                int r = sat;
                int b = random.nextInt(100) + 155;
                int g = sat;

                Color color2 = new Color(r, g, b);
                g2.setPaint(color2);
                g2.fill(new Rectangle(random.nextInt(width), random.nextInt((int) (height / halver)), random.nextInt((int) (width / halver)),
                        random.nextInt((int) (height / halver))));
            }
            g2.setPaint(new Color(random.nextInt(255), random.nextInt(255), 0));
            g2.fill(new Ellipse2D.Double(width / 4, height / 4, width / 2, height / 2));

            g2.setPaint(new Color(sat, green, sat));
            g2.fill(new Rectangle(0, (int) (height / halver), width, (int) (height / halver)));

            sat = random.nextInt(100);
            for (int i = 0; i < random.nextInt(500); i++) {
                int r = sat;
                int g = random.nextInt(100) + 155;
                int b = sat;

                Color color2 = new Color(r, g, b);
                g2.setPaint(color2);
                g2.fill(new Rectangle(random.nextInt(width), (int) ((height / halver) + random.nextInt((int) (height / halver))), random.nextInt((int) (width / halver)),
                        random.nextInt((height))));
            }
            return buffimg;

        }
        if (type == ArtType.CIRCLES) {
            BufferedImage buffimg = new BufferedImage(360, 240, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = buffimg.createGraphics();
            int width = buffimg.getWidth();
            int height = buffimg.getHeight();
            g2.setPaint(color);
            g2.fill(new Rectangle(0, 0, width, height));
            for (int i = 0; i < random.nextInt(500); i++) {
                int r = color.getRed();
                int g = color.getBlue();
                int b = color.getGreen();
                if (random.nextInt(10) == 0) {
                    r = g + (random.nextInt(30) - 15);
                    g = b + (random.nextInt(30) - 15);
                    b = r + (random.nextInt(30) - 15);
                } else {
                    r = r + (random.nextInt(30) - 15);
                    g = g + (random.nextInt(30) - 15);
                    b = b + (random.nextInt(30) - 15);
                }

                if (r > 255) {
                    r = 255;
                }
                if (r < 0) {
                    r = 0;
                }
                if (g > 255) {
                    g = 255;
                }
                if (g < 0) {
                    g = 0;
                }
                if (b > 255) {
                    b = 255;
                }
                if (b < 0) {
                    b = 0;
                }

                Color color2 = new Color(r, g, b);
                g2.setPaint(color2);
                g2.fill(new Ellipse2D.Double(random.nextInt(width), random.nextInt(height), random.nextInt(width / 2), random.nextInt(height / 2)));
            }
            return buffimg;
        }

        if (type == ArtType.PHOTO) {

            File folder = new File("photos");
            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles();
                ArrayList<File> filelist = new ArrayList<File>();
                for (File f : files) {
                    if (f.getName().startsWith("photo-")) {
                        filelist.add(f);
                    }
                }
                File photo = filelist.get(random.nextInt(filelist.size()));
                System.out.println(photo.getName());

                BufferedImage img = ImageIO.read(photo);
                BufferedImage bufferedImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D gr = (Graphics2D) bufferedImage.getGraphics();

                int size = img.getWidth() / (5 + random.nextInt(30));

                for (int x = 0; x < img.getWidth(); x += size) {
                    for (int y = 0; y < img.getHeight(); y += size) {
                        int clr = img.getRGB(x, y);
                        int r = (clr & 0x00ff0000) >> 16;
                        int g = (clr & 0x0000ff00) >> 8;
                        int b = clr & 0x000000ff;

                        r = r + (random.nextInt(10) - 5);
                        g = g + (random.nextInt(10) - 5);
                        b = b + (random.nextInt(10) - 5);

                        if (r > 255) {
                            r = 255;
                        }
                        if (r < 0) {
                            r = 0;
                        }
                        if (g > 255) {
                            g = 255;
                        }
                        if (g < 0) {
                            g = 0;
                        }
                        if (b > 255) {
                            b = 255;
                        }
                        if (b < 0) {
                            b = 0;
                        }

                        gr.setPaint(new Color(r, g, b));
                        gr.fill(new Rectangle(x, y, size, size));
                    }
                }
                return bufferedImage;

            }
        }
        return null;
    }

}
