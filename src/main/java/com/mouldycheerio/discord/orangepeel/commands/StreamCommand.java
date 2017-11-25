package com.mouldycheerio.discord.orangepeel.commands;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class StreamCommand extends OrangePeelAdminCommand {
    public StreamCommand() {
        setName("streaming");
        setDescription(new CommandDescription("streaming", "stream!", "streaming <text>"));
        setCommandlvl(3);
        setCatagory(CommandCatagory.BOT_ADMIN);
    }

    @Override
    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        StringBuilder sb = new StringBuilder();
        if (args.length > 1) {
            for (int i = 1; i < args.length; i++) {
                if (i > 1) {
                    sb.append(" ");
                }
                sb.append(args[i]);
            }
        }
        String text = sb.toString();
        orangepeel.setStream_link(text);
        orangepeel.updatePlaying();

    }
}
