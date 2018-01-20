package com.mouldycheerio.discord.orangepeel.games;

import java.util.ArrayList;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.mouldycheerio.discord.orangepeel.Logger;
import com.mouldycheerio.discord.orangepeel.PeelingUtils;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

public class HangManGame {
    private IChannel channel;
    private IMessage message;
    private String word;
    private ArrayList<Character> guessed;
    private int mistakes = 0;

    private boolean playing = false;

    public HangManGame(IChannel channel) throws Exception {
        this.channel = channel;

        word = "-";

        while (word.contains("-")) {
            String json = PeelingUtils.getHTTP(
                    "http://api.wordnik.com:80/v4/words.json/randomWord?hasDictionaryDef=true&excludePartOfSpeech=family-name&minCorpusCount=0&maxCorpusCount=-1&minDictionaryCount=1&maxDictionaryCount=-1&minLength=4&maxLength=14&api_key=a2a73e7b926c924fad7001ca3111acd55af2ffabf50eb4ae5");
            JSONTokener parser = new JSONTokener(json);
            JSONObject obj = (JSONObject) parser.nextValue();
            word = obj.getString("word").toLowerCase();
        }

        word = word.replace("\n", "");
        guessed = new ArrayList<Character>();
        String m = "";
        Logger.info(word);
        message = channel.sendMessage("```Hangman```");
        refreshMessage();
        playing = true;
    }

    public void refreshMessage() {
        String m = makeTitle() + "\n";
        int wrongs = 0;
        for (char c : word.toCharArray()) {
            if (guessed.contains(new Character(c))) {
                m = m + "**" + c + "** ";
            } else {
                m = m + "  ̲   ";
            }
        }
        m = m + "\n         ";
        if (!m.contains("  ̲  ")) {

            // m = m + "\n*Incorrect letters:*\n ";

            message.edit("```👱```\n" + "The world was **" + word + "**\nWell done!");
            playing = false;
            return;
        }
        for (Character c : guessed) {
            if (!word.contains(c.toString())) {
                m = m + "~~" + c + "~~ ";
            }
        }
        message.edit(m);

    }

    public String makeTitle() {
        String hang = "Hangman";
        if (Math.random() > 0.5 && mistakes > 1) {
            hang = hang + "🤦🏼‍♂️";
        } else {
            hang = hang + "👱";
        }

        return "```☠️⚙️" + hang.substring(mistakes) + "```";
    }

    public void guess(Character c) {
        if (playing) {
            getGuessed().add(c);
            if (!word.contains(c.toString())) {
                mistakes++;

                if (mistakes > 6) {
                    message.edit("```☠️⚙💀```\nThe Word was **" + word + "**");
                    playing = false;
                    return;
                }
            }
            refreshMessage();
        }
    }

    public IChannel getChannel() {
        return channel;
    }

    public void setChannel(IChannel channel) {
        this.channel = channel;
    }

    public IMessage getMessage() {
        return message;
    }

    public void setMessage(IMessage message) {
        this.message = message;
    }

    public ArrayList<Character> getGuessed() {
        return guessed;
    }

    public void setGuessed(ArrayList<Character> guessed) {
        this.guessed = guessed;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }
}
