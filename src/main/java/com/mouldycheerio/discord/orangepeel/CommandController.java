package com.mouldycheerio.discord.orangepeel;

import java.util.ArrayList;
import java.util.List;

import com.mouldycheerio.discord.orangepeel.commands.ChopperCommand;
import com.mouldycheerio.discord.orangepeel.commands.Command;
import com.mouldycheerio.discord.orangepeel.commands.DownVoteCommand;
import com.mouldycheerio.discord.orangepeel.commands.HelpCommand;
import com.mouldycheerio.discord.orangepeel.commands.HeyCommand;
import com.mouldycheerio.discord.orangepeel.commands.ResponseTimeCommand;
import com.mouldycheerio.discord.orangepeel.commands.StatsCommand;
import com.mouldycheerio.discord.orangepeel.commands.TopCommand;
import com.mouldycheerio.discord.orangepeel.commands.UpTimeCommand;
import com.mouldycheerio.discord.orangepeel.commands.VoteCommand;
import com.mouldycheerio.discord.orangepeel.commands.XOXCommand;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandController {
    private List<Command> commands;
    private OrangePeel orangePeel;
    public CommandController(OrangePeel orangePeel) {
        this.orangePeel = orangePeel;
        commands = new ArrayList<Command>();

        commands.add(new HeyCommand());
        commands.add(new UpTimeCommand());
        commands.add(new VoteCommand());
        commands.add(new DownVoteCommand());
        commands.add(new ChopperCommand());
        commands.add(new ResponseTimeCommand());
        commands.add(new XOXCommand());

        commands.add(new TopCommand());
        commands.add(new StatsCommand());

        commands.add(new HelpCommand(commands));


    }

    public void onMessageReceivedEvent(MessageReceivedEvent event) {
        String msg = event.getMessage().getContent();
        String[] parts = msg.split(" ");
        String commandname = parts[0].substring(1);
        for (Command c : commands) {
            if (commandname.equals(c.getName())) {
                c.onCommand(orangePeel, orangePeel.getClient(), event.getMessage(), parts);
            }
        }
    }
}
