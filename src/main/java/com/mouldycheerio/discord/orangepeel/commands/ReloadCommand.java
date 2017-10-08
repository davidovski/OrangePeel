package com.mouldycheerio.discord.orangepeel.commands;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class ReloadCommand extends OrangePeelAdminCommand {
    public ReloadCommand() {
        setName("reload");
        setDescription(new CommandDescription("reload", "reload all ", "reload"));
        setCommandlvl(4);
        addAlias("rl");
        setCatagory(CommandCatagory.BOT_ADMIN);
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        long c = System.currentTimeMillis();
        IMessage m = commandMessage.getChannel().sendMessage(":recycle:");
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        m.edit(":open_file_folder:");
        orangepeel.loadAll();

        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        m.edit(":floppy_disk:");
        orangepeel.saveAll();

        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        m.delete();
        commandMessage.getChannel().sendMessage(":recycle: Reload Complete (" + (System.currentTimeMillis() - c) + ") :recycle:");

    }
}
