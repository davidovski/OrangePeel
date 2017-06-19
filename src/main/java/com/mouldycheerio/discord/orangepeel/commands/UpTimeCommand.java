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

        String time = "";
        if (days > 0) {
            time = days + " Days";
        } else if (hours > 0) {
            time = hours + " Hours";
        } else if (minutes > 0) {
            time = minutes + " Minutes";
        } else if (seconds > 0) {
            time = seconds + " Seconds";
        } else {
            time = orangepeel.getUptime() + " millis";
        }
        commandMessage.getChannel().sendMessage("I've been awake for " + time);

    }
}
