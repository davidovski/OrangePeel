package com.mouldycheerio.discord.orangepeel.commands;

import com.mouldycheerio.discord.orangepeel.BotStatus;
import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class ShutdownCommand extends OrangePeelAdminCommand {
    public ShutdownCommand() {
        setName("shutdown");
        setDescription(new CommandDescription("shutdown", "shutdown the bot", "shutdown "));
        setCommandlvl(5);
        addAlias("term");
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        IMessage m = commandMessage.getChannel().sendMessage("Shutting down...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        m.edit("Shutting down... :floppy_disk: Saving... :floppy_disk: ");

        orangepeel.saveAll();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        m.edit(":wave: Goodbye! :wave:");
        orangepeel.getClient().logout();
        orangepeel.setStatus(BotStatus.SHUTTINGDOWN);


    }
}
