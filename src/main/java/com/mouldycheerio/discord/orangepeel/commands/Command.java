package com.mouldycheerio.discord.orangepeel.commands;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public interface Command {
    public String getName();
    public CommandDescription getDescription();
    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args);
}
