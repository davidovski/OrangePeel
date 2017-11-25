package com.mouldycheerio.discord.orangepeel.commands;

import java.util.ArrayList;

import com.mouldycheerio.discord.orangepeel.Member;
import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class ChannelsCommand extends OrangePeelCommand {
    public java.util.List<Member> games = new ArrayList<Member>();

    public ChannelsCommand() {
        setName("channels");
        setDescription(new CommandDescription("channels", "channels!", "channels"));
        setCatagory(CommandCatagory.DEBUG);

    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {

        String logs = "Logs = ";
        try {
            logs = logs + orangepeel.getLogChannel().getLongID();
        } catch (NullPointerException  e ){
            e.printStackTrace();
            logs = logs + "N/A";
        }

        String suggestions = "suggestions = ";
        try {
            suggestions = suggestions + orangepeel.getSuggestionsChannel().getLongID();
        } catch (NullPointerException  e){
            e.printStackTrace();
            suggestions = suggestions + "N/A";
        }

        String bugs = "bugs = ";
        try {
            bugs = bugs + orangepeel.getBugReportsChannel().getLongID();
        } catch (NullPointerException  e){
            e.printStackTrace();
            bugs = bugs + "N/A";
        }

        commandMessage.getChannel().sendMessage("```" + logs + "\n" + suggestions + "\n" + bugs + "```");
    }
}
