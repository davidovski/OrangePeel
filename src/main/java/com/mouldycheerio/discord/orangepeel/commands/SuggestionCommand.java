package com.mouldycheerio.discord.orangepeel.commands;

import java.awt.Color;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

public class SuggestionCommand extends OrangePeelCommand {
    public SuggestionCommand() {
        setName("suggest");
        setDescription(new CommandDescription("suggest", "Suggest a feature for the bot", "suggest <suggestion>"));
        setCatagory(CommandCatagory.ABOUT);
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        if (orangepeel.getSuggestions() != null) {
            StringBuilder sb = new StringBuilder();
            if (args.length > 1) {
                for (int i = 1; i < args.length; i++) {
                    if (i > 1) {
                        sb.append(" ");
                    }
                    sb.append(args[i]);
                }
            }

            IUser author = commandMessage.getAuthor();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.withTitle("Suggestion");
            embedBuilder.withDescription(sb.toString());
            embedBuilder.withThumbnail(client.getOurUser().getAvatarURL());
            embedBuilder.withAuthorIcon(author.getAvatarURL());
            embedBuilder.withAuthorName(author.getName());
            embedBuilder.withColor(Color.ORANGE);

            orangepeel.getSuggestions().sendMessage(embedBuilder.build());
        }


    }
}
