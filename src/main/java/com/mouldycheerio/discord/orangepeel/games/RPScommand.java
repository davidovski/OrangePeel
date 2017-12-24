package com.mouldycheerio.discord.orangepeel.games;

import java.util.ArrayList;
import java.util.List;

import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.commands.CommandCatagory;
import com.mouldycheerio.discord.orangepeel.commands.CommandDescription;
import com.mouldycheerio.discord.orangepeel.commands.OrangePeelCommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.RequestBuffer;

public class RPScommand extends OrangePeelCommand {
    private List<RPSgame> rps;
    private boolean registered = false;

    public RPScommand() {
        rps = new ArrayList<RPSgame>();
        setName("rps");
        setDescription(new CommandDescription("Rock Paper Scissors", "Play rock-paper-scissors", "rps"));
        addAlias("RockPaperScissors");
        setCatagory(CommandCatagory.GAMES);
    }

    @Override
    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        if (!registered  ) {
            client.getDispatcher().registerListener(this);
            registered = true;
        }
        IMessage m = commandMessage.getChannel().sendMessage("Rock Paper Scissors");
        RequestBuffer.request(() -> {
            m.addReaction(RPSitem.ROCK.getEmoji());
        });
        RequestBuffer.request(() -> {
            m.addReaction(RPSitem.PAPER.getEmoji());
        });
        RequestBuffer.request(() -> {
            m.addReaction(RPSitem.SCISSORS.getEmoji());
        });


        rps.add(new RPSgame(m, orangepeel));

        orangepeel.getStatsCounter().incrementStat("rps-plays");

    }

    @EventSubscriber
    public void onReactionAddEvent(ReactionAddEvent event) throws InterruptedException {
        for (RPSgame g : rps) {
            if (g.onReaction(event)) {
                break;
            }
        }
    }
}
