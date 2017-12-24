package com.mouldycheerio.discord.orangepeel;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.mouldycheerio.discord.orangepeel.challenges.ChallengeController;
import com.mouldycheerio.discord.orangepeel.commands.Command;
import com.mouldycheerio.discord.orangepeel.commands.CommandDescription;
import com.mouldycheerio.discord.orangepeel.commands.PerServerCustomCmd;
import com.mouldycheerio.discord.orangepeel.commands.SimpleCustomCmd;
import com.mouldycheerio.discord.orangepeel.commands.SummonCommand;
import com.mouldycheerio.discord.orangepeel.commands.coin.CoinController;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RateLimitException;

public class OrangePeel {
    private static final long PEEL_SERVER = Long.parseLong("306508548651745282");
    private EventListener eventListener;
    private IDiscordClient client;
    public EventDispatcher dispatcher;

    private long uptime = 0;
    private long creation;
    private JSONObject votes;

    private Map<String, Long> voted;


    private CoinController coinController;

    private Random random;
    private StatsCounter statsCounter;

    private long bugReports = 327504772691263488L;
    private long suggestions = 324448786912903168L;
    private long logChannel = 376141347029254144L;

    private JSONObject admins;
    private String playingText = "";

    private BotStatus status = BotStatus.INACTIVE;
    private ChatterBot chatterBot;
    private ChatterBotSession chatsession;

    private int playingtextindex = 0;
    private String prefix;
    private ChallengeController challengeController;
    private HashMap<String, String> autoRole = new HashMap<String, String>();
    private HashMap<String, String> greet = new HashMap<String, String>();
    private HashMap<String, String> muted = new HashMap<String, String>();
    private List<Long> banned;
    private int cmdadded;
    private String stream_link = "";

    public OrangePeel(String token, String prefix) throws Exception {
        setCmdadded(0);
        this.prefix = prefix;
        // ChatterBotFactory chatterBotFactory = new ChatterBotFactory();
        // chatterBot = chatterBotFactory.create(ChatterBotType.CLEVERBOT);
        // chatsession = chatterBot.createSession(Locale.ENGLISH);

        random = new Random();

        coinController = new CoinController(this);

        votes = new JSONObject();
        admins = new JSONObject();
        voted = new HashMap<String, Long>();
        banned = new ArrayList<Long>();


        statsCounter = new StatsCounter(new JSONObject(), this);
        status = BotStatus.ACTIVE;

        eventListener = new EventListener(prefix, this);
        challengeController = new ChallengeController(this);

        creation = System.currentTimeMillis();
        client = ClientFactory.createClient(token, true);
        dispatcher = client.getDispatcher();
        dispatcher.registerListener(eventListener);

    }



    public void logError(Exception e, IMessage commandMessage) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.withTitle(e.getMessage() + " (" + e.getClass().getName() + ")");
        embedBuilder.withDescription(sw.toString());
        embedBuilder.withAuthorName("OrangePeel Logger");
        embedBuilder.withColor(new Color(54, 57, 62));

        embedBuilder.appendField("command message", commandMessage.getContent(), true);
        embedBuilder.appendField("user executing", commandMessage.getAuthor().getName() + "#" + commandMessage.getAuthor().getDiscriminator(), true);
        embedBuilder.appendField("host server", commandMessage.getGuild().getName(), true);
        embedBuilder.appendField("channel", commandMessage.getChannel().getName(), true);

        getLogChannel().sendMessage(embedBuilder.build());
    }

    public void logError(Exception e) {
        if (e instanceof RateLimitException)
            return;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.withTitle(e.getMessage() + " (" + e.getClass().getName() + ")");
            embedBuilder.withDescription(sw.toString());
            embedBuilder.withAuthorName("OrangePeel Logger");
            embedBuilder.withColor(new Color(54, 57, 62));
            getLogChannel().sendMessage(embedBuilder.build());
        } catch (RateLimitException ex) {
            try {
                Thread.sleep(3000);

            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void log(String e) {

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.withTitle("Log");
        embedBuilder.withDescription(e);
        embedBuilder.withAuthorName("OrangePeel Logger");
        embedBuilder.withColor(new Color(54, 57, 62));
        getLogChannel().sendMessage(embedBuilder.build());
    }

    public void loadAll() {
        try {
            coinController.load();
            FileReader fileReader = new FileReader("OrangePeel.opf");
            JSONTokener parser = new JSONTokener(fileReader);

            JSONObject obj = (JSONObject) parser.nextValue();



            if (obj.has("stats")) {
                statsCounter.setStats(obj.getJSONObject("stats"));
            }

            if (obj.has("autoroles")) {
                JSONObject jo = obj.getJSONObject("autoroles");
                Iterator<String> keys = jo.keys();
                while (keys.hasNext()) {
                    String next = keys.next();
                    autoRole.put(next, jo.getString(next));

                }
            }

            if (obj.has("greetings")) {
                JSONObject jo = obj.getJSONObject("greetings");
                Iterator<String> keys = jo.keys();
                while (keys.hasNext()) {
                    String next = keys.next();
                    greet.put(next, jo.getString(next));

                }
            }

            if (obj.has("muted")) {
                JSONObject jo = obj.getJSONObject("muted");
                Iterator<String> keys = jo.keys();
                while (keys.hasNext()) {
                    String next = keys.next();
                    muted.put(next, jo.getString(next));

                }
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

            if (obj.has("stream_link")) {
                setStream_link(obj.getString("stream_link"));
            }

//            try {
//
//                if (obj.has("bug_reports")) {
//                    for (IChannel c : client.getChannels()) {
//                        if (c.getLongID() == obj.getLong("bug_reports")) {
//                            bugReports = c.getLongID();
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

//            try {
//                if (obj.has("suggestions")) {
//                    for (IChannel c : client.getChannels()) {
//                        if (c.getLongID() == obj.getLong("suggestions")) {
//                            suggestions = c.getLongID();
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            try {
//                if (obj.has("logchannel")) {
//                    Logger.info("" + obj.getLong("logchannel"));
//
//                                setLogChannel(obj.getLong("logchannel"));
//
//                    if (getLogChannel() == null) {
//                        Logger.warn("NO LOGGER CHANNEL!");
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            try {
                JSONArray ban = obj.getJSONArray("banned");
                for (int i = 0; i < ban.length(); i++) {
                    banned.add(ban.getLong(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (obj.has("commands")) {
                JSONArray b = obj.getJSONArray("commands");

                for (int i = 0; i < b.length(); i++) {
                    JSONObject cmd = b.getJSONObject(i);
                    String cmdname = cmd.getString("command");
                    String cmddesc = cmd.getString("description");
                    String cmdtext = cmd.getString("text");
                    String cmdServer = null;
                    try {
                        cmdServer = cmd.getString("server");
                    } catch (Exception e) {
                    }

                    boolean doit = true;
                    CommandDescription desc = new CommandDescription(cmdname, cmddesc, cmdname);
                    for (Command command : eventListener.getCommandController().getCommands()) {
                        if (command.getName().equals(cmdname)) {
                            doit = false;
                        }
                    }
                    if (doit) {

                        if (cmdServer != null) {
                            PerServerCustomCmd scc = new PerServerCustomCmd(cmdname, desc, cmdtext, client.getGuildByID(Long.parseLong(cmdServer)));
                            scc.setShowInHelp(false);
                            eventListener.getCommandController().getCommands().add(scc);
                            setCmdadded(getCmdadded() + 1);
                        } else {
                            SimpleCustomCmd scc = new SimpleCustomCmd(cmdname, desc, cmdtext);
                            scc.setShowInHelp(false);
                            eventListener.getCommandController().getCommands().add(scc);
                            setCmdadded(getCmdadded() + 1);
                        }
                    }
                }

            }

            if (obj.has("connected")) {
                Logger.info("reconnecting to lost channels");
                JSONArray jsonArray = obj.getJSONArray("connected");
                for (int i = 0; i < jsonArray.length(); i++) {
                    SummonCommand.joinandplay(client.getVoiceChannelByID(jsonArray.getLong(i)), this);
                }
            }
            fileReader.close();
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
        getStatsCounter().incrementStat("loads");

        try {
            challengeController.loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveAll() {
        coinController.save();

        challengeController.saveAll();
        JSONObject obj = new JSONObject();
        obj.put("votes", votes);
        obj.put("admins", admins);
        obj.put("stats", statsCounter.getStats());
        getStatsCounter().incrementStat("saves");
        obj.put("playing", playingText);
        obj.put("stream_link", stream_link);

        JSONObject objauto = new JSONObject();
        for (Entry<String, String> entry : autoRole.entrySet()) {
            objauto.put(entry.getKey(), entry.getValue());
        }
        obj.put("autoroles", objauto);


        JSONObject objgreet = new JSONObject();
        for (Entry<String, String> entry : greet.entrySet()) {
            objgreet.put(entry.getKey(), entry.getValue());
        }
        obj.put("greetings", objgreet);

        JSONObject objmute = new JSONObject();
        for (Entry<String, String> entry : muted.entrySet()) {
            objmute.put(entry.getKey(), entry.getValue());
        }
        obj.put("muted", objmute);

        JSONArray array = new JSONArray();
        for (Command command : getEventListener().getCommandController().getCommands()) {
            if (command instanceof SimpleCustomCmd) {
                try {
                    JSONObject cmd = new JSONObject();
                    cmd.put("command", command.getName());
                    cmd.put("description", command.getDescription().getText());
                    cmd.put("text", ((SimpleCustomCmd) command).getText());

                    if (command instanceof PerServerCustomCmd) {
                        cmd.put("server", ((PerServerCustomCmd) command).getServer().getStringID());
                    }
                    array.put(cmd);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        JSONArray array2 = new JSONArray();
        for (Long l : banned) {
            array2.put(l.longValue());
        }
        obj.put("banned", array2);
        obj.put("commands", array);
        try {
            obj.put("bug_reports", bugReports);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            obj.put("suggestions", suggestions);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            obj.put("logchannel", logChannel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<IVoiceChannel> voices = client.getConnectedVoiceChannels();
        JSONArray jsonArray = new JSONArray();
        for (IVoiceChannel iVoiceChannel : voices) {
            jsonArray.put(iVoiceChannel.getLongID());
        }
        obj.put("connected", jsonArray);

        try {
            File file = new File("OrangePeel.opf");
            FileWriter filew = new FileWriter(file);
            filew.write(obj.toString(1));
            filew.flush();

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
        challengeController.update();
        if (alpha % 400 == 10) {

            playingtextindex++;
            updatePlaying();

        }

        if (alpha % 20 * 60 * 5 == (20 * 60 * 5) - 4) {
            saveAll();

        }


        Iterator<Entry<String, Long>> iterator = voted.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry<String, Long> entry = iterator.next();
            if (System.currentTimeMillis() - entry.getValue() > (1000 * 60 * 60)) {
                voted.remove(entry.getKey());
            }
        }
    }

    public void updatePlaying() {
        if (client.isReady()) {

            if (!getStream_link().startsWith("https://twitch.tv/")) {
                if (playingtextindex == 0) {
                    client.idle(playingText);
                } else if (playingtextindex == 1) {
                    client.idle(prefix + "help");
                } else if (playingtextindex == 2) {
                    client.idle("on " + statsCounter.getServers() + " servers!");
                } else {
                    playingtextindex = 0;
                }
            } else {
                if (playingtextindex == 0) {
                    client.streaming(getStream_link(), getStream_link());
                } else if (playingtextindex == 1) {
                    client.streaming(prefix + "help", getStream_link());
                } else if (playingtextindex == 2) {
                    client.streaming("on " + statsCounter.getServers() + " servers!", getStream_link());
                } else {
                    playingtextindex = 0;
                }
            }
        } else {
            playingtextindex = 0;
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
        client.streaming(playingText, "https://twitch.tv/theredturtlez");
        this.playingText = playingText;
    }

    public BotStatus getStatus() {
        return status;
    }

    public void setStatus(BotStatus status) {
        this.status = status;
    }



    public EventListener getEventListener() {
        return eventListener;
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public long getSuggestions() {
        return suggestions;
    }

    public IChannel getSuggestionsChannel() {
        return client.getGuildByID(PEEL_SERVER).getChannelByID(suggestions);
    }

    public void setSuggestions(long suggestions) {
        this.suggestions = suggestions;
    }

    public long getBugReports() {
        return bugReports;
    }

    public IChannel getBugReportsChannel() {
        return client.getGuildByID(PEEL_SERVER).getChannelByID(bugReports);
    }

    public void setBugReports(long bugReports) {
        this.bugReports = bugReports;
    }

    public ChatterBotSession getChatsession() {
        return chatsession;
    }

    public void setChatsession(ChatterBotSession chatsession) {
        this.chatsession = chatsession;
    }

    public long getLogChannelID() {
        return logChannel;
    }

    public IChannel getLogChannel() {
        IGuild guildByID = client.getGuildByID(PEEL_SERVER);
        return guildByID.getChannelByID(376141347029254144L);
    }

    public void setLogChannel(long logChannel) {
        this.logChannel = logChannel;
    }

    public ChallengeController getChallengeController() {
        return challengeController;
    }

    public void setChallengeController(ChallengeController challengeController) {
        this.challengeController = challengeController;
    }

    public List<Long> getBanned() {
        return banned;
    }

    public void setBanned(List<Long> banned) {
        this.banned = banned;
    }

    public HashMap<String, String> getAutoRole() {
        return autoRole;
    }

    public void setAutoRole(HashMap<String, String> autoRole) {
        this.autoRole = autoRole;
    }

    public HashMap<String, String> getGreet() {
        return greet;
    }

    public void setGreet(HashMap<String, String> greet) {
        this.greet = greet;
    }

    public HashMap<String, String> getMuted() {
        return muted;
    }

    public void setMuted(HashMap<String, String> muted) {
        this.muted = muted;
    }

    public int getCmdadded() {
        return cmdadded;
    }

    public void setCmdadded(int cmdadded) {
        this.cmdadded = cmdadded;
    }

    public CoinController coinController() {
        return coinController;
    }

    public String getStream_link() {
        return stream_link;
    }

    public void setStream_link(String stream_link) {
        this.stream_link = stream_link;
    }

}
