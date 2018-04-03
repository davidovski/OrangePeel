package com.mouldycheerio.discord.orangepeel.commands.debug;

import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.commands.CommandCatagory;
import com.mouldycheerio.discord.orangepeel.commands.CommandDescription;
import com.mouldycheerio.discord.orangepeel.commands.OrangePeelCommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class ConfigListCommand extends OrangePeelCommand {
    public ConfigListCommand() {
        setName("config");
        setDescription(new CommandDescription("config", "lists the server's config", "config"));
        setCatagory(CommandCatagory.DEBUG);
    }

    @Override
    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        commandMessage.getChannel().sendMessage(orangepeel.getConfig(commandMessage.getGuild()).toString());
    }
}
