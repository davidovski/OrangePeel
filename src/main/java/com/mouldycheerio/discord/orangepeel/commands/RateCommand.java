package com.mouldycheerio.discord.orangepeel.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.PeelingUtils;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class RateCommand extends OrangePeelCommand {
    public RateCommand() {
        setName("rate");
        setDescription(new CommandDescription("rate", "rate you on certain stuff...", "rate"));
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        IUser u = commandMessage.getAuthor();
        if (args.length >= 2) {
            u = commandMessage.getGuild().getUserByID(Long.parseLong(PeelingUtils.mentionToId(args[1], commandMessage.getGuild())));
        }

        File d = new File("avatars");
        d.mkdirs();
        File f = new File(d, u.getName().replaceAll(" ", "_"));

        IMessage m = commandMessage.getChannel().sendMessage("Calculating...");

        try {
            // download(f, u.getAvatar());
            // BufferedImage img = ImageIO.read(f);
            // Color rgb = new Color(img.getRGB(0, 0));
            // Color rgb2 = new Color(img.getRGB(img.getWidth() / 2, img.getHeight() / 2));
            // int redRating = Math.abs(rgb.getRed() - rgb2.getRed());
            // int greenRating = Math.abs(rgb.getGreen() - rgb2.getGreen());
            // int blueRating = Math.abs(rgb.getBlue() - rgb2.getBlue());
            // double avgRGBrating = (redRating + greenRating + blueRating) / 3;
            // double imageRating = 1.0 - (avgRGBrating / 255);
            double imageRating = Math.random();

            int urlPoints = 0;
            for (char c : u.getAvatarURL().toCharArray()) {
                if (Character.isDigit(c)) {
                    urlPoints += 5;
                } else if (Character.isAlphabetic(c)) {
                    if (Character.isLowerCase(c)) {
                        urlPoints += 10;
                    } else {
                        urlPoints += 7;

                    }
                } else {
                    urlPoints += 1;
                }
            }
            double maxUrlPoints = u.getAvatarURL().length() * 10.0;
            double urlRating = urlPoints / maxUrlPoints;

            int namePoints = 0;
            for (char c : u.getName().toCharArray()) {
                if (Character.isDigit(c)) {
                    namePoints += 5;
                } else if (Character.isAlphabetic(c)) {
                    if (Character.isLowerCase(c)) {
                        namePoints += 10;
                    } else {
                        namePoints += 7;

                    }
                }
                namePoints -= 1;
            }
            double maxNamePoints = u.getName().length() * 10.0;
            double nameRating = namePoints / maxNamePoints;

            boolean hasnick = false;
            double nickRating = 0.0;
            if (u.getNicknameForGuild(commandMessage.getGuild()) != null) {
                hasnick = true;
                int nickPoints = 0;
                for (char c : u.getNicknameForGuild(commandMessage.getGuild()).toCharArray()) {
                    if (Character.isDigit(c)) {
                        nickPoints += 8;
                    } else if (Character.isLetter(c)) {
                        if (Character.isLowerCase(c)) {
                            nickPoints += 4;
                        } else {
                            nickPoints += 5;

                        }
                    } else {
                        nickPoints += 10;
                    }

                }
                double maxnickPoints = u.getNicknameForGuild(commandMessage.getGuild()).length() * 10.0;
                nickRating = nickPoints / maxnickPoints;
            } else {

            }

            int c1 = ammountOfTimes(u.getDiscriminator(), u.getDiscriminator().charAt(0));
            int c2 = ammountOfTimes(u.getDiscriminator(), u.getDiscriminator().charAt(1));
            int c3 = ammountOfTimes(u.getDiscriminator(), u.getDiscriminator().charAt(2));
            int c4 = ammountOfTimes(u.getDiscriminator(), u.getDiscriminator().charAt(3));

            double discrimRating = ((c1 + c2 + c3 + c4) / 4.0) / 4.0;

                double overall = (urlRating + discrimRating + nameRating + nickRating) / 4.0;
                m.edit("__" + u.getName() + "'s Ratings__\n" + "`Username:` " + numbertoGrading(nameRating) + "\n" + "`Discriminator:` " + numbertoGrading(discrimRating) + "\n"
                        + "`Avatar:` " + numbertoGrading(urlRating) + "\n" + "`Nickname:` " + numbertoGrading(nickRating) + "\n" + "**OVERALL GRADE:**: " + numbertoGrading(overall)
                        + "!");
                orangepeel.getStatsCounter().incrementStat("rates");
        } catch (Exception e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            sw.toString();
            m.edit("Oppsy, something went wrong!\n```" + sw + "```");
            orangepeel.logError(e, commandMessage);
        }
        m.getChannel().setTypingStatus(false);
    }

    public int ammountOfTimes(String s, char c) {
        int counter = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                counter++;
            }
        }
        return counter;
    }

    public String numbertoGrading(double n) {
        String grade = "";
        int percent = (int) Math.ceil(n * 100);
        if (percent < 10) {
            grade = "F-";
        } else if (percent < 20) {
            grade = "F";
        } else if (percent < 25) {
            grade = "F+";
        } else if (percent < 30) {
            grade = "E-";
        } else if (percent < 35) {
            grade = "E";
        } else if (percent < 40) {
            grade = "E+";
        } else if (percent < 45) {
            grade = "C-";
        } else if (percent < 50) {
            grade = "C";
        } else if (percent < 55) {
            grade = "C+";
        } else if (percent < 60) {
            grade = "B-";
        } else if (percent < 65) {
            grade = "B";
        } else if (percent < 70) {
            grade = "B+";
        } else if (percent < 85) {
            grade = "A-";
        } else if (percent < 9) {
            grade = "A";
        } else if (percent < 99) {
            grade = "A+";
        } else {
            grade = "★";
        }

        return "**" + grade + "** *(" + percent + "%)*";
    }

    private static void download(File file, String urlString) throws MalformedURLException, IOException, ProtocolException, FileNotFoundException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        try {
            InputStream in = connection.getInputStream();
            try {
                FileOutputStream out = new FileOutputStream(file);
                try {
                    copy(in, out);
                } finally {
                    out.close();
                }
            } finally {
                in.close();
            }
        } finally {
            connection.disconnect();
        }
    }

    private static byte[] buffer = new byte[10240];

    public static void copy(InputStream input, OutputStream output) throws IOException {
        System.out.println("Copying...");
        int n = input.read(buffer);
        while (n >= 0) {
            output.write(buffer, 0, n);
            n = input.read(buffer);
        }
        output.flush();
        System.out.print(" Copied!");

        // int len;
        // while ((len = zis.read(buffer)) > 0) {
        // fos.write(buffer, 0, len);
        // }
    }
}
