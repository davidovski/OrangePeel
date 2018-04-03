package com.mouldycheerio.discord.orangepeel.commands;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class ResponseTimeCommand extends OrangePeelCommand {
    public ResponseTimeCommand() {
        setName("ping");
        setDescription(new CommandDescription("Ping", "Find out how fast the bots running", "ping"));
        addAlias("pi");
        addAlias("pong");
        setCatagory(CommandCatagory.ABOUT);
    }

    @Override
    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        commandMessage.getChannel().sendMessage("I like ping pong too. (" + pingUrl("https://discordapp.com") + "ms)");
        orangepeel.getStatsCounter().incrementStat("pings");

    }

    public static long pingUrl(final String address) {
        try {
            final URL url = new URL(address);
            final HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setConnectTimeout(1000 * 10); // mTimeout is in seconds
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
