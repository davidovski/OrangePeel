package com.mouldycheerio.discord.orangepeel.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.commands.CommandCatagory;
import com.mouldycheerio.discord.orangepeel.commands.CommandDescription;
import com.mouldycheerio.discord.orangepeel.commands.OrangePeelCommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IReaction;
import sx.blah.discord.util.RequestBuffer;

public class XOXCommand extends OrangePeelCommand {
    private List<XOXgame> xox;
    private boolean registered = false;

    public XOXCommand(IDiscordClient client) {
        xox = new ArrayList<XOXgame>();
        setName("xox");
        setDescription(new CommandDescription("XOX", "Play Tic Tac Toe", "xox"));
        addAlias("NaughtsAndCrosses");
        addAlias("tictactoe");
        addAlias("nac");
        setCatagory(CommandCatagory.GAMES);


    }

    @Override
    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        if (!registered ) {
            client.getDispatcher().registerListener(this);
            registered = true;
        }
        IMessage m = commandMessage.getChannel().sendMessage("Game Loading...");

        // <:xox1:392070187610013696L>
        // <:xox2:392070187601625118L>
        // <:xox3:392070187601625088L>
        // <:xox4:392070187563876352L>
        // <:xox5:392070187794432000L>
        // <:xox6:392070187362418701L>
        // <:xox7:392070187559682048L>
        // <:xox8:392070187589042186L>
        // <:xox9:392070187584585728L>

        ReactionEmoji[] em = new ReactionEmoji[9];
        em[0] = ReactionEmoji.of("xox1", 392070187610013696L);
        em[1] = ReactionEmoji.of("xox2", 392070187601625118L);
        em[2] = ReactionEmoji.of("xox3", 392070187601625088L);
        em[3] = ReactionEmoji.of("xox4", 392070187563876352L);
        em[4] = ReactionEmoji.of("xox5", 392070187794432000L);
        em[5] = ReactionEmoji.of("xox6", 392070187362418701L);
        em[6] = ReactionEmoji.of("xox7", 392070187559682048L);
        em[7] = ReactionEmoji.of("xox8", 392070187589042186L);
        em[8] = ReactionEmoji.of("xox9", 392070187584585728L);

        addReactions(m, em);

        xox.add(new XOXgame(m, orangepeel));
        orangepeel.getStatsCounter().incrementStat("xox-plays");

    }

    @EventSubscriber
    public void onReactionAddEvent(ReactionAddEvent event) throws InterruptedException {
        System.out.println("Reacted");
        ReactionEmoji emoji = event.getReaction().getEmoji();
        int number = 0;
        if (emoji.equals(ReactionEmoji.of("xox1", 392070187610013696L))) {
            number = 1;
        } else if (emoji.equals(ReactionEmoji.of("xox2", 392070187601625118L))) {
            number = 2;
        } else if (emoji.equals(ReactionEmoji.of("xox3", 392070187601625088L))) {
            number = 3;
        } else if (emoji.equals(ReactionEmoji.of("xox4", 392070187563876352L))) {
            number = 4;
        } else if (emoji.equals(ReactionEmoji.of("xox5", 392070187794432000L))) {
            number = 5;
        } else if (emoji.equals(ReactionEmoji.of("xox6", 392070187362418701L))) {
            number = 6;
        } else if (emoji.equals(ReactionEmoji.of("xox7", 392070187559682048L))) {
            number = 7;
        } else if (emoji.equals(ReactionEmoji.of("xox8", 392070187589042186L))) {
            number = 8;
        } else if (emoji.equals(ReactionEmoji.of("xox9", 392070187584585728L))) {
            number = 9;
        }

        if (number != 0) {
            xoxYourTurn(event.getMessage(), "<:" + emoji.getName() + ":" + emoji.getStringID() + ">", event.getReaction());
        }
        // for (RPSgame g : orangePeel.getRps()) {
        // if (g.onReaction(event)) {
        // break;
        // }
        //
        // }
    }

    public String playXOXturn(XOXgame g, Iterator<XOXgame> it, boolean edit) throws InterruptedException {

        IMessage m = g.getMessage();

        // if (mine.size() == 0) {
        // xox.remove(id);
        // m.edit("DRAW!");
        // return;
        // }
        System.out.println("processing bot turn ");

        String nextMove = g.getNextMove();
        System.out.println("made move ");

        if (nextMove != "") {

            try {
                System.out.println("remobing reaction ");
                ReactionEmoji of = ReactionEmoji.of(nextMove.split(":")[1], Long.parseLong(nextMove.split(":")[2].replace(">", "")));
                m.removeReaction(g.getOp().getClient().getOurUser(), of);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Thread.sleep(200);

//            if (edit) {
//                m.edit(m.getContent().replace(nextMove, ":o2:"));
//            }

            g.toggleUserTurn();
            g.nextTurn();
            return nextMove;
        } else {
            if (it != null) {
                it.remove();
            }
        }
        return "";

    }

    public void addReactions(IMessage m, ReactionEmoji[] es2) {
        List<ReactionEmoji> es1 = Arrays.asList(es2);
        Collections.reverse(es1);
        ReactionEmoji[] es = (ReactionEmoji[]) es1.toArray();
        final AtomicInteger i = new AtomicInteger();
        RequestBuffer.request(() -> {
            for (; i.get() < es.length; i.incrementAndGet()) {
                if (es[i.intValue()] != null) {
                    m.addReaction(es[i.intValue()]);
                }
            }
            m.edit("<:xox1:392070187610013696> <:xox2:392070187601625118> <:xox3:392070187601625088>\n"
                    + "<:xox4:392070187563876352> <:xox5:392070187794432000> <:xox6:392070187362418701>\n"
                    + "<:xox7:392070187559682048> <:xox8:392070187589042186> <:xox9:392070187584585728>");
            for (XOXgame g : xox) {
                if (g.getMessage().equals(m)) {
                    g.createArrays(m.getContent());
                }
            }

        });
    }

    public void xoxYourTurn(IMessage m, String number, IReaction reaction) throws InterruptedException {
        System.out.println("checking if xox");

        boolean equals = false;
        XOXgame game = null;
        for (XOXgame g : xox) {

            if (g.getMessage().getStringID().equals(m.getStringID())) {
                equals = true;
                game = g;

                break;

            }
        }
        if (equals && !m.getContent().equals("Game Loading...")) {
            System.out.println("processing userturn");
            // if (game.isUserTurn()) {
            String replace = m.getContent();
            replace = replace.replace(number, ":negative_squared_cross_mark:"); //
            game.createArrays(replace);
            String turn = playXOXturn(game, null, false);

            if (!game.ended()) {

                replace = replace.replace(turn, ":o2:"); //
                System.out.println("editing ");

                m.edit(replace);
                game.createArrays(m.getContent());
                System.out.println("removing reaction ");

                m.removeReaction(game.getOp().getClient().getOurUser(), reaction);

                game.toggleUserTurn();

                game.nextTurn();
                System.out.println("---");


            } else {
                xox.remove(game);
            }

            // }

        }
    }

    public static String emojiString(ReactionEmoji e) {
        return "<:" + e.getName() + ":" + e.getStringID() + ">";

    }
}
