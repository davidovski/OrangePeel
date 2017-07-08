package com.mouldycheerio.discord.orangepeel.commands;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class UpTimeCommand extends OrangePeelCommand {
    public UpTimeCommand() {
        setName("uptime");
        setDescription(new CommandDescription("uptime", "Show the bot's uptime.", "uptime"));
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        long seconds = orangepeel.getUptime() / 1000;
        long minutes = seconds / 60;
        long hours =  minutes / 60;
        long days = hours / 24;
        long weeks = days / 7;

        String time = "";
        if (days > 0) {
            time = days + " days and " + (hours - (days * 24)) + " hours";
        } else if (hours > 0) {
            time = hours + " hours and " + (minutes - (hours * 60)) + " minutes";
        } else if (minutes > 0) {
            time = minutes + " minutes and " + (seconds - (minutes * 60)) + " seconds";
        } else if (seconds > 0) {
            time = seconds + " seconds";
        } else {
            time = orangepeel.getUptime() + " millis";
        }
        commandMessage.getChannel().sendMessage("Hmm, let's see... I've been awake for about " + time);

    }
}
