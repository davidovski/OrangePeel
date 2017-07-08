package com.mouldycheerio.discord.orangepeel;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import com.mouldycheerio.discord.orangepeel.commands.Command;
import com.mouldycheerio.discord.orangepeel.commands.CommandDescription;
import com.mouldycheerio.discord.orangepeel.commands.SimpleCustomCmd;
import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IReaction;

public class OrangePeel {
    private EventListener eventListener;
    private IDiscordClient client;
    public EventDispatcher dispatcher;

    private long uptime = 0;
    private long creation;
    private JSONObject votes;

    private Map<String, Long> voted;
    private List<XOXgame> xox;
    private List<RPSgame> rps;

    private Random random;
    private StatsCounter statsCounter;

    private IChannel bugReports;
    private IChannel suggestions;

    private JSONObject admins;
    private String playingText = "a game";

    private BotStatus status = BotStatus.INACTIVE;
    private ChatterBot chatterBot;
    private ChatterBotSession chatsession;

    private int playingtextindex = 0;
    private String prefix;

    public OrangePeel(String token, String prefix) throws Exception {
        this.prefix = prefix;
        ChatterBotFactory chatterBotFactory = new ChatterBotFactory();
        chatterBot = chatterBotFactory.create(ChatterBotType.CLEVERBOT);
        chatsession = chatterBot.createSession(Locale.ENGLISH);



        random = new Random();

        client = ClientFactory.createClient(token, true);
        dispatcher = client.getDispatcher();

        eventListener = new EventListener(prefix, this);
        dispatcher.registerListener(eventListener);
        creation = System.currentTimeMillis();

        votes = new JSONObject();
        admins = new JSONObject();
        voted = new HashMap<String, Long>();

        xox = new ArrayList<XOXgame>();
        rps = new ArrayList<RPSgame>();

        statsCounter = new StatsCounter(new JSONObject(), this);
        status = BotStatus.ACTIVE;
        loadAll();

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

    public void loadAll() {

        try {
            JSONTokener parser = new JSONTokener(new FileReader("OrangePeel.opf"));

            JSONObject obj = (JSONObject) parser.nextValue();
            if (obj.has("stats")) {
                statsCounter.setStats(obj.getJSONObject("stats"));
            }
            if (obj.has("votes")) {
                votes = obj.getJSONObject("votes");
            }
            if (obj.has("admins")) {
                admins = obj.getJSONObject("admins");
            }

            if (obj.has("playing")) {
                playingText = obj.getString("playing");
            }

            if (obj.has("bug_reports")) {
                bugReports = client.getChannelByID(obj.getLong("bug_reports"));
            }

            if (obj.has("suggestions")) {
                suggestions = client.getChannelByID(obj.getLong("suggestions"));
            }

            if (obj.has("commands")) {
                JSONArray b = obj.getJSONArray("commands");

                for (int i = 0; i < b.length(); i++) {
                    JSONObject cmd = b.getJSONObject(i);
                    String cmdname = cmd.getString("command");
                    String cmddesc = cmd.getString("description");
                    String cmdtext = cmd.getString("text");

                    boolean doit = true;
                    CommandDescription desc = new CommandDescription(cmdname, cmddesc, cmdname);
                    for (Command command : eventListener.getCommandController().getCommands()) {
                        if (command.getName().equals(cmdname)) {
                            doit = false;
                        }
                    }
                    if (doit) {
                        eventListener.getCommandController().getCommands().add(new SimpleCustomCmd(cmdname, desc, cmdtext));
                    }
                }

            }

        } catch (FileNotFoundException e) {
            System.out.println("No file found! Creating new one!");
            try {
                FileWriter file = new FileWriter("OrangePeel.opf");
                file.flush();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void saveAll() {
        JSONObject obj = new JSONObject();
        obj.put("votes", votes);
        obj.put("admins", admins);
        obj.put("stats", statsCounter.getStats());
        obj.put("playing", playingText);

        JSONArray array = new JSONArray();
        for (Command command : getEventListener().getCommandController().getCommands()) {
            if (command instanceof SimpleCustomCmd) {
                JSONObject cmd = new JSONObject();
                cmd.put("command", command.getName());
                cmd.put("description", command.getDescription().getText());
                cmd.put("text", ((SimpleCustomCmd) command).getText());
                array.put(cmd);
            }
        }
        obj.put("commands", array);
        if (bugReports != null) {
            obj.put("bug_reports", bugReports.getLongID());
        }
        if (suggestions != null) {
            obj.put("suggestions", suggestions.getLongID());
        }

        try {
            FileWriter file = new FileWriter("OrangePeel.opf");
            file.write(obj.toString(1));
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void justVoted(String whoid) {
        voted.put(whoid, System.currentTimeMillis());
    }

    public boolean canVote(String whoid) {
        return !voted.containsKey(whoid);
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
                votes.put(id, -1);
            }
            saveAll();
        }
    }

    public void loop(long alpha) throws InterruptedException {
        uptime = alpha;

        if (alpha % 400 == 0) {
            playingtextindex++;
            if (playingtextindex == 1) {
                client.changePlayingText(playingText);
            } else  if (playingtextindex == 2) {
                client.changePlayingText(prefix + "help");
            } else  if (playingtextindex == 3) {
                client.changePlayingText("on " + statsCounter.getServers() + " servers!");
            } else {
                playingtextindex = 0;
            }

        }

        if (alpha % 10 == 0) {

            Iterator<RPSgame> itrps = rps.iterator();
            while (itrps.hasNext()) {
                RPSgame g = itrps.next();
                if (g.isEnded()) {
                    itrps.remove();
                }
                if (alpha % 30 == 0) {
                    g.getMessage().addReaction(RPSitem.PAPER.getEmoji());
                }
                if (alpha % 30 == 10) {
                    g.getMessage().addReaction(RPSitem.SCISSORS.getEmoji());
                }
                if (alpha % 30 == 20) {
                    g.getMessage().addReaction(RPSitem.ROCK.getEmoji());
                }

            }
            if (suggestions != null) {
                for (IMessage iMessage : suggestions.getMessages()) {
                    if (!iMessage.getReactionByUnicode("thumbsup").getClientReacted()) {
                        iMessage.addReaction("thumbsup");
                    }

                    if (!iMessage.getReactionByUnicode("thumbsdown").getClientReacted()) {
                        iMessage.addReaction("thumbsdown");
                    }
                }
            }
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
        Iterator<Entry<String, Long>> iterator = voted.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry<String, Long> entry = iterator.next();
            if (System.currentTimeMillis() - entry.getValue() > (1000 * 60 * 60 * 3)) {
                voted.remove(entry.getKey());
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

    public JSONObject getAdmins() {
        return admins;
    }

    public void setAdmins(JSONObject admins) {
        this.admins = admins;
    }

    public String getPlayingText() {
        return playingText;
    }

    public void setPlayingText(String playingText) {
        client.changePlayingText(playingText);
        this.playingText = playingText;
    }

    public BotStatus getStatus() {
        return status;
    }

    public void setStatus(BotStatus status) {
        this.status = status;
    }

    public List<RPSgame> getRps() {
        return rps;
    }

    public void setRps(List<RPSgame> rps) {
        this.rps = rps;
    }

    public EventListener getEventListener() {
        return eventListener;
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public IChannel getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(IChannel suggestions) {
        this.suggestions = suggestions;
    }

    public IChannel getBugReports() {
        return bugReports;
    }

    public void setBugReports(IChannel bugReports) {
        this.bugReports = bugReports;
    }

    public ChatterBotSession getChatsession() {
        return chatsession;
    }

    public void setChatsession(ChatterBotSession chatsession) {
        this.chatsession = chatsession;
    }

}
