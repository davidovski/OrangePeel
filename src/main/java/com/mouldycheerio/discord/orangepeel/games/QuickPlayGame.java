package com.mouldycheerio.discord.orangepeel.games;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.PeelingUtils;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class QuickPlayGame {
    private IChannel channel;
    private String currentWord = "";
    private boolean playing = false;

    private IMessage startMessage;
    private boolean contains = false;

    private int players = 0;

    private HashMap<IUser, Integer> scores = new HashMap<IUser, Integer>();
    private int maxScore = 20;
    private OrangePeel op;
    private int maxPlayers;

    public QuickPlayGame(IChannel channel, OrangePeel op, int players, int maxScore) {
        this.op = op;
        maxPlayers = players;
        this.maxScore = maxScore;
        this.setChannel(channel);
        startMessage = channel.sendMessage(
                "```Quick! Think! Act!```  A game about reacting :alarm_clock:, knowledge :thought_balloon: and speed! :runner: \n\n:arrow_forward: Type `ready` to join.\n\n:arrow_forward: Players needed to start : **" + maxPlayers +"**\n\n:arrow_forward: Players joined = **0**");
        setCurrentWord("ready");
        setPlaying(false);
        players = 0;
    }

    public String nextWord(IUser iUser) throws InterruptedException {
        if (iUser != null) {
            int value = 1;
            if (scores.containsKey(iUser)) {
                value = scores.get(iUser) + 1;
                scores.put(iUser, value);

            } else {
                scores.put(iUser, 1);
            }

            channel.sendMessage("<@" + iUser.getStringID() + "> won that round! They now have **" + scores.get(iUser) + "** points");
            if (value >= maxScore) {

                Comparator<Entry<IUser, Integer>> valueComparator = new Comparator<Entry<IUser, Integer>>() {

                    @Override
                    public int compare(Entry<IUser, Integer> o1, Entry<IUser, Integer> o2) {
                        return o2.getValue() - o1.getValue();
                    }
                };

                List<Entry<IUser, Integer>> listOfEntries = new ArrayList<Entry<IUser, Integer>>(scores.entrySet());

                Collections.sort(listOfEntries, valueComparator);

                LinkedHashMap<IUser, Integer> sortedByValue = new LinkedHashMap<IUser, Integer>(listOfEntries.size());

                for (Entry<IUser, Integer> entry : listOfEntries) {
                    sortedByValue.put(entry.getKey(), entry.getValue());
                }

                Set<Entry<IUser, Integer>> entrySetSortedByValue = sortedByValue.entrySet();

                String scores = "```\n";

                for (Entry<IUser, Integer> mapping : entrySetSortedByValue) {
                    scores = scores + mapping.getKey().getName() + ": " + mapping.getValue() + " points\n";
                }
                scores = scores + "```";
                for (Entry<IUser, Integer> e : sortedByValue.entrySet()) {
                    channel.sendMessage("<@" + e.getKey().getStringID() + "> " + "is the winner with **" + maxScore + "** points! Here are the results:\n" + scores);
                    op.coinController().incrementCoins(750, e.getKey(), channel.getGuild(), true);
                    break;

                }

                setPlaying(false);
                setCurrentWord("yrtytutr5e46rtiuyhgfdrr67t8yuyrd68yu");
                return "";
            }
        }
        Random random = new Random();
        int gametype = random.nextInt(5);
        Thread.sleep(400);
//         int gametype = 5;
        try {
            if (gametype == 0) {
                String json = PeelingUtils.getHTTP(
                        "http://api.wordnik.com:80/v4/words.json/randomWord?hasDictionaryDef=true&excludePartOfSpeech=family-name&minCorpusCount=0&maxCorpusCount=-1&minDictionaryCount=1&maxDictionaryCount=-1&minLength=4&maxLength=14&api_key=a2a73e7b926c924fad7001ca3111acd55af2ffabf50eb4ae5");
                JSONTokener parser = new JSONTokener(json);
                JSONObject obj = (JSONObject) parser.nextValue();
                String word = obj.getString("word").toLowerCase();
                word = word.replace("\n", "");
                IMessage m = channel.sendMessage("**Quick!**");
                Thread.sleep(400);
                m.edit("**Quick!** Type ...");
                Thread.sleep(200);
                m.edit("**Quick!** Type `" + word + "`");
                setCurrentWord(word);
                contains = false;
            } else if (gametype == 1) {
                int a = 0;
                int b = 0;
                a = random.nextInt(10);
                b = random.nextInt(10);
                int answer = 0;

                int operation = random.nextInt(100);
                String op = "";
                if (operation < 35) {
                    answer = a + b;
                    op = "+";
                } else if (operation < 70) {
                    answer = a - b;
                    op = "-";
                } else if (operation < 90) {
                    answer = a * b;
                    op = "x";
                } else {
                    answer = a / b;
                    op = "÷";
                }
                IMessage m = channel.sendMessage("**Quick!**");
                Thread.sleep(400);
                m.edit("**Quick!** Solve ...");
                Thread.sleep(200);
                m.edit("**Quick!** Solve `" + a + "" + op + "" + b + "`");
                contains = false;
                setCurrentWord(answer + "");
            } else if (gametype == 2) {
                String word = PeelingUtils.getHTTP("http://api.yomomma.info/");
                JSONTokener parser = new JSONTokener(word);
                JSONObject obj = (JSONObject) parser.nextValue();
                String string = obj.getString("joke");
                IMessage m = channel.sendMessage("**Quick!**");
                Thread.sleep(400);
                m.edit("**Quick!** Laugh! ```" + string + "```");
                contains = true;
                setCurrentWord("ha");

            } else if (gametype == 3) {
                IMessage m = channel.sendMessage("**Quick!**");
                Thread.sleep(700);
                m.edit("**Quick!** Dot!");
                contains = false;
                setCurrentWord(".");
            } else if (gametype == 4) {
                IMessage m = channel.sendMessage("**Quick!**");
                Thread.sleep(1500);
                m.edit("**Quick!** Speak!!");
                contains = true;
                setCurrentWord("");
            } else if (gametype == 5) {
                String json = PeelingUtils.getHTTP(
                        "http://api.wordnik.com:80/v4/words.json/randomWord?hasDictionaryDef=true&excludePartOfSpeech=family-name&minCorpusCount=0&maxCorpusCount=-1&minDictionaryCount=1&maxDictionaryCount=-1&minLength=4&maxLength=14&api_key=a2a73e7b926c924fad7001ca3111acd55af2ffabf50eb4ae5");
                JSONTokener parser = new JSONTokener(json);
                JSONObject obj = (JSONObject) parser.nextValue();
                String word = obj.getString("word").toLowerCase();
                word = word.replace("\n", "");
                int letter1i = random.nextInt(word.length() - 1);
                char letter1 = word.charAt(letter1i);

                int letter2i = letter1i + 1;
                char letter2 = word.charAt(letter2i);

                String str = word.substring(0, letter1i) + letter2 + letter1 + word.substring(letter2i + 1, word.length());

                IMessage m = channel.sendMessage("**Quick!**");

                Thread.sleep(400);
                m.edit("**Quick!** Correct ...");
                Thread.sleep(200);
                m.edit("**Quick!** Correct `" + str + "`");
                setCurrentWord(word);
                contains = false;

            }

        } catch (Exception e) {
            IMessage m = channel.sendMessage("**Quick!**");
            Thread.sleep(1500);
            m.edit("**Quick!** Speak!!");
            contains = true;
            setCurrentWord("");
            e.printStackTrace();
            if (op.getLogChannel() != null) {
                op.logError(e);
            }
        }
        return currentWord;
    }

    public IChannel getChannel() {
        return channel;
    }

    public void setChannel(IChannel channel) {
        this.channel = channel;
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public void setCurrentWord(String currentWord) {
        this.currentWord = currentWord;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public void addPlayer() {
        players++;
        startMessage.edit(startMessage.getContent().split("=")[0] + "= **" + players + "**");
        if (players >= maxPlayers) {
            setPlaying(true);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                nextWord(null);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public IMessage getStartMessage() {
        return startMessage;
    }

    public void setStartMessage(IMessage startMessage) {
        this.startMessage = startMessage;
    }

    public HashMap<IUser, Integer> getScores() {
        return scores;
    }

    public void setScores(HashMap<IUser, Integer> scores) {
        this.scores = scores;

    }

    public boolean isContains() {
        return contains;
    }

    public void setContains(boolean contains) {
        this.contains = contains;
    }
}
