package com.mouldycheerio.discord.orangepeel;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IReaction;

public class OrangePeel {
    public EventListener eventListener;
    private IDiscordClient client;
    public EventDispatcher dispatcher;

    private long uptime = 0;
    private long creation;
    private JSONObject votes;

    private List<String> voted;
    private List<XOXgame> xox;
    private Random random;
    private StatsCounter statsCounter;

    public OrangePeel(String token) {
        random = new Random();

        client = ClientFactory.createClient(token, true);
        dispatcher = client.getDispatcher();
        client.streaming("=help", "https://goo.gl/HkrWLH");
        eventListener = new EventListener("=", this);
        dispatcher.registerListener(eventListener);
        creation = System.currentTimeMillis();

        votes = new JSONObject();
        voted = new ArrayList<String>();
        loadAll();
        xox = new ArrayList<XOXgame>();

        statsCounter = new StatsCounter(new JSONObject(), this);

    }

    public void xoxYourTurn(IMessage m, String number, IReaction reaction) throws InterruptedException {
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
            if (game.isUserTurn()) {
                m.edit(m.getContent().replaceAll(number, "negative_squared_cross_mark"));
                Thread.sleep(200);
                m.removeReaction(reaction);

                game.toggleUserTurn();
                game.nextTurn();
                if (game.ended()) {
                    xox.remove(game);
                }

            }

        }
    }

    public void saveAll() {
        votes.toString();
        try {
            FileWriter file = new FileWriter("votes.json");
            file.write(votes.toString(1));
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print(votes);

        statsCounter.getStats().toString();
        try {
            FileWriter file = new FileWriter("stats.json");
            file.write(statsCounter.getStats().toString(1));
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print(statsCounter.getStats());
    }

    public void justVoted(String whoid) {
        voted.add(whoid);
    }

    public boolean canVote(String whoid) {
        return !voted.contains(whoid);
    }

    public void addVote(String id) {
        if (votes != null) {
            if (votes.has(id)) {
                votes.put(id, votes.getInt(id) + 1);
            } else {
                votes.put(id, 1);
            }
            saveAll();
        }
    }

    public void removeVote(String id) {
        if (votes != null) {
            if (votes.has(id)) {
                votes.put(id, votes.getInt(id) - 1);
            } else {
                votes.put(id, 1);
            }
            saveAll();
        }
    }

    public void loadAll() {

        try {
            JSONTokener parser = new JSONTokener(new FileReader("votes.json"));

            JSONObject obj = (JSONObject) parser.nextValue();

            votes = obj;
            System.out.println(votes);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        try {
            JSONTokener parser = new JSONTokener(new FileReader("stats.json"));

            JSONObject obj = (JSONObject) parser.nextValue();

            statsCounter.setStats(obj);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void loop(long alpha) throws InterruptedException {
        uptime = alpha;

        if (alpha % 20 == 0) {

            Iterator<XOXgame> it = xox.iterator();
            while (it.hasNext()) {

                XOXgame g = it.next();

                if (!g.isUserTurn()) {
                    IMessage m = g.getMessage();
                    List<IReaction> mine = new ArrayList<IReaction>();
                    for (IReaction iReaction : m.getReactions()) {
                        if (iReaction.getClientReacted()) {
                            mine.add(iReaction);
                        }
                    }
                    // if (mine.size() == 0) {
                    // xox.remove(id);
                    // m.edit("DRAW!");
                    // return;
                    // }
                    Logger.warn("chosing out of:" + mine.size());
                    String nextMove = g.getNextMove();
                    if (nextMove != "") {
                        Emoji e = EmojiManager.getForAlias(nextMove);
                        IReaction myturn = m.getReactionByUnicode(e);
                        Logger.info("I choose " + myturn.getUnicodeEmoji().getUnicode());
                        Thread.sleep(200);

                        m.removeReaction(myturn);
                        Thread.sleep(200);

                        m.edit(m.getContent().replaceAll(myturn.getUnicodeEmoji().getAliases().get(0), "o2"));

                        g.toggleUserTurn();
                        g.nextTurn();
                    } else {
                        it.remove();
                        return;
                    }

                }

                if (g.getMessage().getContent().equals("Game Loading...")) {
                    if (g.getNextnumbertoadd() == 1) {
                        g.getMessage().addReaction(":one:");
                    }
                    if (g.getNextnumbertoadd() == 2) {
                        g.getMessage().addReaction(":two:");
                    }
                    if (g.getNextnumbertoadd() == 3) {
                        g.getMessage().addReaction(":three:");
                    }
                    if (g.getNextnumbertoadd() == 4) {
                        g.getMessage().addReaction(":four:");
                    }
                    if (g.getNextnumbertoadd() == 5) {
                        g.getMessage().addReaction(":five:");
                    }
                    if (g.getNextnumbertoadd() == 6) {
                        g.getMessage().addReaction(":six:");
                    }
                    if (g.getNextnumbertoadd() == 7) {
                        g.getMessage().addReaction(":seven:");
                    }
                    if (g.getNextnumbertoadd() == 8) {
                        g.getMessage().addReaction(":eight:");
                    }
                    if (g.getNextnumbertoadd() == 9) {
                        g.getMessage().addReaction(":nine:");
                        g.getMessage().edit(":one::two::three:\n:four::five::six:\n:seven::eight::nine:");
                    }
                    g.addedNumber();
                }
                if (g.ended()) {
                    it.remove();
                }
            }
        }

        long l = getUptime() % (1000 * 60 * 60 * 3);
        if (l > 0 && l < 1000) {
            voted.clear();

            for (IGuild g : client.getGuilds()) {
                //g.getGeneralChannel().sendMessage(TopCommand.createBoard(this));
            }

        }
    }

    public IDiscordClient getClient() {
        return client;
    }

    public void setClient(IDiscordClient client) {
        this.client = client;
    }

    public long getUptime() {
        return System.currentTimeMillis() - creation;
    }

    public JSONObject getVotes() {
        return votes;
    }

    public void setVotes(JSONObject votes) {
        this.votes = votes;
    }

    public List<XOXgame> getXox() {
        return xox;
    }

    public void addXOXmessage(XOXgame m) {
        xox.add(m);

    }

    public StatsCounter getStatsCounter() {
        return statsCounter;
    }

    public void setStatsCounter(StatsCounter statsCounter) {
        this.statsCounter = statsCounter;
    }

}
