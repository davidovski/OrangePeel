package com.mouldycheerio.discord.orangepeel.commands;

import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.XOXgame;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class XOXCommand extends OrangePeelCommand {
    public XOXCommand() {
        setName("xox");
        setDescription(new CommandDescription("XOX", "Play Tic Tac Toe", "xox"));
        addAlias("NaughtsAndCrosses");
        addAlias("tictactoe");
        addAlias("nac");
        setCatagory(CommandCatagory.GAMES);
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        IMessage m = commandMessage.getChannel().sendMessage("Game Loading...");
        orangepeel.getXox().add(new XOXgame(m, orangepeel));
        orangepeel.getStatsCounter().incrementStat("xox-plays");

    }
}
