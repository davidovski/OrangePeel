package com.mouldycheerio.discord.orangepeel.commands;

import java.util.Iterator;
import java.util.List;

import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.RPSgame;
import com.mouldycheerio.discord.orangepeel.XOXgame;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class EndGamesCommand extends OrangePeelAdminCommand {
    public EndGamesCommand() {
        setName("endGames");
        setDescription(new CommandDescription("endGames", "kills all currently running games", "endGames"));
        setCommandlvl(3);
        addAlias("killfun");
        setCatagory(CommandCatagory.BOT_ADMIN);
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        orangepeel.getStatsCounter().incrementStat("gamesended");
        List<RPSgame> rps = orangepeel.getRps();
        Iterator<RPSgame> it = rps.iterator();
        while (it.hasNext()) {
            RPSgame next = it.next();
            if (next.getMessage().getChannel().getStringID().equals(commandMessage.getChannel().getStringID())) {
                next.getMessage().edit("The game has been ended by a staff member. Please play responsibly");
                it.remove();
            }
        }

        List<XOXgame> xox = orangepeel.getXox();
        Iterator<XOXgame> it2 = xox.iterator();
        while (it2.hasNext()) {
            XOXgame next = it2.next();
            if (next.getMessage().getChannel().getStringID().equals(commandMessage.getChannel().getStringID())) {
                next.getMessage().edit("The game has been ended by a staff member. Please play responsibly");
                it2.remove();
            }
        }
    }
}
