package com.mouldycheerio.discord.orangepeel.commands;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class LoadAllCommand extends OrangePeelAdminCommand {
    public LoadAllCommand() {
        setName("loadAll");
        setDescription(new CommandDescription("loadAll", "Load all files", "loadAll"));
        setCommandlvl(4);
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        long c = System.currentTimeMillis();

        orangepeel.loadAll();
        commandMessage.getChannel().sendMessage(":open_file_folder:" + (System.currentTimeMillis() - c) + "ms");
    }
}
