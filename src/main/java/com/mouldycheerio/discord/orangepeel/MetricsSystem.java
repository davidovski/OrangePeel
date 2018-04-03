package com.mouldycheerio.discord.orangepeel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import sx.blah.discord.api.IDiscordClient;

public class MetricsSystem {
    public static void logCommand(long l, String command, long m) {
        File file = new File("metrics/commands.all");
        file.mkdirs();
        try {
            Writer output = new BufferedWriter(new FileWriter(file, true));
            output.append(System.currentTimeMillis() + "|" + l + "|" + command + "|" + m + "\n");
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logPing() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                int ping = (int) pingUrl("https://discordapp.com/");
                File file = new File("metrics/ping");
                file.mkdirs();
                try {
                    Writer output = new BufferedWriter(new FileWriter(file, true));
                    output.append(System.currentTimeMillis() + "|" + ping + "\n");
                    output.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        thread.start();


    }

    public static void logServers(IDiscordClient c) {

        File file = new File("metrics/guilds");
        file.mkdirs();
        try {
            Writer output = new BufferedWriter(new FileWriter(file, true));
            String csq = System.currentTimeMillis() + "|" + c.getGuilds().size() + "|" + c.getUsers().size() + "|" + c.getChannels().size() + "\n";
            output.append(csq);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long pingUrl(final String address) {
        try {
            final URL url = new URL(address);
            final HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setConnectTimeout(1000 * 10);
            final long startTime = System.currentTimeMillis();
            urlConn.connect();
            final long endTime = System.currentTimeMillis();
            return (endTime - startTime);
        } catch (final MalformedURLException e1) {
            e1.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
