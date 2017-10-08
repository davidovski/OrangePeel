package com.mouldycheerio.discord.orangepeel.commands;

import java.awt.Color;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;

public class LogCommand extends OrangePeelAdminCommand {
    public LogCommand() {
        setName("log");
        setDescription(new CommandDescription("log", "Append something to the bot's log.", "log <message>"));
        setCommandlvl(2);
        setCatagory(CommandCatagory.BOT_ADMIN);
    }

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
        String url = sb.toString();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.withTitle(commandMessage.getAuthor().getName() + "#" +commandMessage.getAuthor().getDiscriminator());
        embedBuilder.withDescription(url);
        embedBuilder.withAuthorName("[Log]");
        embedBuilder.withColor(new Color(54, 57, 62));

        orangepeel.getLogChannel().sendMessage(embedBuilder.build());
    }
}
