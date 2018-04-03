package com.mouldycheerio.discord.orangepeel.commands;

import java.awt.Color;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

public class SuggestionCommand extends OrangePeelCommand {
    public SuggestionCommand() {
        setName("suggest");
        setDescription(new CommandDescription("suggest", "Suggest a feature for the bot", "suggest <suggestion>"));
        setCatagory(CommandCatagory.ABOUT);
    }

    @Override
    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        if (orangepeel.getSuggestionsChannel() != null) {
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
//            embedBuilder.withThumbnail(client.getOurUser().getAvatarURL());
            embedBuilder.withAuthorIcon(author.getAvatarURL());
            embedBuilder.withAuthorName(author.getName());
            embedBuilder.withColor(new Color(54, 57, 62));
            embedBuilder.withTimestamp(System.currentTimeMillis());

            IMessage iMessage = orangepeel.getSuggestionsChannel().sendMessage(embedBuilder.build());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            iMessage.addReaction(ReactionEmoji.of("thumbsup", 409355634644090890L));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            iMessage.addReaction(ReactionEmoji.of("thumbsdown", 409355670597402644L));
        } else {
            commandMessage.reply("I can't do that for some reason, heres the reason why: ```Suggestion channel not set. Error code 1424```");
        }

    }
}
