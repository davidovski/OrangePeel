package com.mouldycheerio.discord.orangepeel.commands;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;

public class PerServerCustomCmd extends SimpleCustomCmd {
    private IGuild server;

    public PerServerCustomCmd(String command, CommandDescription description, String text, IGuild server) {
        super(command, description, text);
        this.server = server;
        setCatagory(CommandCatagory.CUSTOM);
    }

    @Override
    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        if (server != null && commandMessage.getGuild().getStringID().equals(server.getStringID())) {
            commandMessage.getChannel().sendMessage(compute(orangepeel, commandMessage, args));
        }
    }

    public IGuild getServer() {
        return server;
    }

    public void setServer(IGuild server) {
        this.server = server;
    }

    public boolean isOnServer(IGuild g) {
        return (g.getStringID().equals(server.getStringID()));
    }
}
