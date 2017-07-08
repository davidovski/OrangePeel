package com.mouldycheerio.discord.orangepeel.commands;

import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.RPSgame;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class RPScommand extends OrangePeelCommand {
    public RPScommand() {
        setName("rps");
        setDescription(new CommandDescription("Rock Paper Scissors", "Play rock-paper-scissors", "rps"));
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        IMessage m = commandMessage.getChannel().sendMessage("Choose your weapon!");
        orangepeel.getRps().add(new RPSgame(m, orangepeel));


        orangepeel.getStatsCounter().incrementStat("rps-plays");

    }
}
