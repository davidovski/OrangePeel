package com.mouldycheerio.discord.orangepeel.games;

import java.util.List;

import com.mouldycheerio.discord.orangepeel.GameOutcome;
import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.obj.IMessage;

public class RPSgame {
    private IMessage message;

    private IDiscordClient client;

    private boolean ended = false;
    private OrangePeel op;

    public RPSgame(IMessage m, OrangePeel op) {
        this.op = op;
        this.client = op.getClient();
        this.setMessage(m);
    }

    public IMessage getMessage() {
        return message;
    }

    public void setMessage(IMessage message) {
        this.message = message;
    }


    public boolean onReaction(ReactionAddEvent event) {
        if (event.getMessage().equals(message) && !event.getUser().isBot()) {
            RPSitem choice = RPSitem.getRandom();
            GameOutcome o = GameOutcome.DRAW;
            List<String> aliases = event.getReaction().getUnicodeEmoji().getAliases();
            String emojiname = aliases.get(0);
            if (aliases.contains("full_moon")) {
                if (choice == RPSitem.PAPER) {
                    o = GameOutcome.WIN;
                } else if (choice == RPSitem.SCISSORS) {
                    o = GameOutcome.LOSE;
                }
            } else if (aliases.contains("page_facing_up")) {
                if (choice == RPSitem.ROCK) {
                    o = GameOutcome.LOSE;
                } else if (choice == RPSitem.SCISSORS) {
                    o = GameOutcome.WIN;
                }
            } else if (aliases.contains("scissors")) {
                if (choice == RPSitem.PAPER) {
                    o = GameOutcome.LOSE;
                } else if (choice == RPSitem.ROCK) {
                    o = GameOutcome.WIN;
                }
            } else {
                return false;
            }
            ended = true;

            if (o == GameOutcome.WIN) {
                message.edit(choice.getEmoji() + " beats :" + emojiname + ":\n I WIN! :yum:");
                op.getStatsCounter().incrementStat("rps-wins");
            } else if (o == GameOutcome.LOSE) {
                message.edit( ":" + emojiname + ": beats " + choice.getEmoji() + "\n You win. :cry:");
                op.getStatsCounter().incrementStat("rps-losses");
            } else if (o == GameOutcome.DRAW) {
                message.edit(choice.getEmoji() + " = :" + emojiname + ":\n Its a draw!");
                op.getStatsCounter().incrementStat("rps-draws");
            }
        }

        return ended;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }
}
