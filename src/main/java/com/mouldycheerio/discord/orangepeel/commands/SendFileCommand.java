package com.mouldycheerio.discord.orangepeel.commands;

import java.io.File;
import java.io.FileNotFoundException;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class SendFileCommand extends OrangePeelAdminCommand {
    public SendFileCommand() {
        setName("opf");
        setDescription(new CommandDescription("opf", "Send ~~nudes~~ the data file", "reopfboot "));
        setCommandlvl(4);
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        java.sql.Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());

        try {
            commandMessage.getChannel().sendFile("IDK why you want this, but here:", new File("OrangePeel.opf"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            orangepeel.logError(e,commandMessage);
        }

    }
}
