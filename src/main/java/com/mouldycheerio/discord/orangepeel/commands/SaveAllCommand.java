package com.mouldycheerio.discord.orangepeel.commands;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class SaveAllCommand extends OrangePeelAdminCommand {
    public SaveAllCommand() {
        setName("saveAll");
        setDescription(new CommandDescription("saveAll", "Save all files", "saveAll"));
        setCommandlvl(4);
        addAlias("save");
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        long c = System.currentTimeMillis();
        orangepeel.saveAll();
        commandMessage.getChannel().sendMessage(":floppy_disk: " + (System.currentTimeMillis() - c) + "ms");
    }
}
