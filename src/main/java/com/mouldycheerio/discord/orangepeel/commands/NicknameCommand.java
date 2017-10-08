package com.mouldycheerio.discord.orangepeel.commands;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class NicknameCommand extends OrangePeelAdminCommand {
    public NicknameCommand() {
        setName("nickname");
        setDescription(new CommandDescription("nickname", "changes my nickname for the current Guild!", "nickname <name>"));
        setCommandlvl(3);
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
        String text = sb.toString();
        if (text.length() > 1) {
            commandMessage.getChannel().getGuild().setUserNickname(client.getOurUser(), text);
        } else {
            commandMessage.getChannel().getGuild().setUserNickname(client.getOurUser(), client.getOurUser().getName());
        }

    }
}
