package com.mouldycheerio.discord.orangepeel.games;

import java.util.Random;

import sx.blah.discord.handle.impl.obj.ReactionEmoji;

public enum RPSitem {
    ROCK(0, "🌕"),
    PAPER(1, "📄"),
    SCISSORS(2, "✂");

    private int numVal;
    private String emoji;

    RPSitem(int numVal, String emoji) {
        this.numVal = numVal;
        this.emoji = emoji;
    }

    public int getNumVal() {
        return numVal;
    }

    public ReactionEmoji getEmoji() {
        return ReactionEmoji.of(emoji);
    }

    public static RPSitem getRandom() {
        Random r = new Random();
        int n = r.nextInt(3);
        if (n == 0) {
            return RPSitem.ROCK;
        } else if (n == 1) {
            return RPSitem.PAPER;
        } else if (n == 2) {
            return RPSitem.SCISSORS;
        }
        return RPSitem.ROCK;
    }
}
