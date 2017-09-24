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

    public QuickPlayGame(IChannel channel) {
        this.setChannel(channel);
        startMessage = channel.sendMessage("```Quick! Think! Act!```  A game about reacting :alarm_clock:, knowledge :thought_balloon: and speed! :runner: \n\n:arrow_forward: Type `ready` to join.\n\n:arrow_forward: Players needed to start : **3**\n\n:arrow_forward: Players joined = **0**");
        setCurrentWord("ready");
        setPlaying(false);
        players = 0;
    }

    public String nextWord(IUser iUser) {
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

                Comparator<Entry<IUser, Integer>> valueComparator = new Comparator<Entry<IUser,Integer>>() {


                    public int compare(Entry<IUser, Integer> o1, Entry<IUser, Integer> o2) {
                        return o2.getValue() - o1.getValue();
                    }
                };

                List<Entry<IUser, Integer>> listOfEntries = new ArrayList<Entry<IUser, Integer>>(scores.entrySet());

                Collections.sort(listOfEntries, valueComparator);

                LinkedHashMap<IUser, Integer> sortedByValue = new LinkedHashMap<IUser, Integer>(listOfEntries.size());

                for(Entry<IUser, Integer> entry : listOfEntries){
                    sortedByValue.put(entry.getKey(), entry.getValue());
                }

                System.out.println("HashMap after sorting entries by values ");
                Set<Entry<IUser,Integer>> entrySetSortedByValue = sortedByValue.entrySet();

                String scores = "```\n";

                for(Entry<IUser, Integer> mapping : entrySetSortedByValue){
                    scores = scores + mapping.getKey().getName() + ": " + mapping.getValue() + " points\n";
                }
                scores = scores + "```";
                for (Entry<IUser, Integer> e : sortedByValue.entrySet()) {
                    channel.sendMessage("<@" + e.getKey().getStringID() + "> " + "is the winner with **" + maxScore + "** points! Here are the results:\n" + scores);

                    break;

                }

                setPlaying(false);
                setCurrentWord("neeeeee");
                return "";
            }
        }
        Random random = new Random();
        int gametype = random.nextInt(5);
        // int gametype = 0;
        if (gametype == 0) {
            try {
                String word = PeelingUtils.getHTTP("http://www.setgetgo.com/randomword/get.php");
                word = word.replace("\n", "");
                IMessage m = channel.sendMessage("**Quick!**");
                Thread.sleep(400);
                m.edit("**Quick!** Type ...");
                Thread.sleep(200);
                m.edit("**Quick!** Type `" + word + "`");
                setCurrentWord(word);
                contains = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (gametype == 1) {
            try {
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (gametype == 2) {
            try {
                String word = PeelingUtils.getHTTP("http://api.yomomma.info/");
                System.out.println(word);
                JSONTokener parser = new JSONTokener(word);
                JSONObject obj = (JSONObject) parser.nextValue();
                String string = obj.getString("joke");
                IMessage m = channel.sendMessage("**Quick!**");
                Thread.sleep(400);
                m.edit("**Quick!** Laugh! ```" + string + "```");
                contains = true;
                setCurrentWord("ha");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (gametype == 3) {
            try {
                String word = PeelingUtils.getHTTP("http://api.yomomma.info/");
                System.out.println(word);
                JSONTokener parser = new JSONTokener(word);
                JSONObject obj = (JSONObject) parser.nextValue();
                String string = obj.getString("joke");
                IMessage m = channel.sendMessage("**Quick!**");
                Thread.sleep(700);
                m.edit("**Quick!** Dot!");
                contains = false;
                setCurrentWord(".");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (gametype == 4) {
            try {
                IMessage m = channel.sendMessage("**Quick!**");
                Thread.sleep(700);
                m.edit("**Quick!** Speak!!");
                contains = true;
                setCurrentWord("");
            } catch (Exception e) {
                e.printStackTrace();
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
        if (players >= 2) {
            setPlaying(true);
            nextWord(null);
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
