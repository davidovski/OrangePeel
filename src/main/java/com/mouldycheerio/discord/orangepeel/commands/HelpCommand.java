package com.mouldycheerio.discord.orangepeel.commands;

import java.util.List;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IPrivateChannel;

public class HelpCommand extends OrangePeelCommand {
    private List<Command> commands;

    public HelpCommand(List<Command> commands) {
        this.commands = commands;
        setName("help");
        setDescription(new CommandDescription("Help", "Display this message", "help"));
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        commandMessage.reply("Check your DMs! ;)");

        IPrivateChannel pm = client.getOrCreatePMChannel(commandMessage.getAuthor());
        String message = "";
        message = message + "```                                  The Orange Peel Bot```\n";
        message = message + "**by davidovski**\n\n";
        message = message + "***__Commands__***\n\n";
        for (Command c : commands) {
            message = message + c.getDescription().toString() + "\n";
            message = message + "`" + commandMessage.getContent().charAt(0) + c.getDescription().getUsage() + "`\n\n";

        }
        orangepeel.getStatsCounter().incrementStat("helps");


        pm.sendMessage(message);
        pm.sendMessage("**Add me to your server: ** https://goo.gl/HkrWLH\n" +
        "Join the help server: https://discord.gg/KzHHRFg");


        //https://discordapp.com/oauth2/authorize?client_id=306115875784622080&scope=bot
    }
}
