package com.mouldycheerio.discord.orangepeel.commands;

import java.awt.Color;
import java.util.ArrayList;

import com.mouldycheerio.discord.orangepeel.Member;
import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;

public class EmbedCommand extends OrangePeelCommand {
    public java.util.List<Member> games = new ArrayList<Member>();

    public EmbedCommand() {
        setName("embed");
        setDescription(new CommandDescription("embed", "Sends an embeded message", "embed title | desc"));
        setCatagory(CommandCatagory.DEBUG);

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
        System.out.println(text);
        String[] split = text.split("\\|");

        for (String s : split) {
            System.out.println(s);
        }
        String title = "";
        String desc = "";


        if (split.length > 1) {
            desc = split[1];
        }
        title = split[0];

        EmbedBuilder one = new EmbedBuilder();
        one.appendDesc(desc);
        one.withTitle(title);

        one.withColor(new Color(255, 177, 3));
        commandMessage.getChannel().sendMessage(one.build());
    }
}
