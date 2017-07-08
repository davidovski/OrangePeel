package com.mouldycheerio.discord.orangepeel;

import java.util.Random;

public enum RPSitem {
    ROCK(0, ":full_moon:"),
    PAPER(1, ":page_facing_up:"),
    SCISSORS(2, ":scissors:");

    private int numVal;
    private String emoji;

    RPSitem(int numVal, String emoji) {
        this.numVal = numVal;
        this.emoji = emoji;
    }

    public int getNumVal() {
        return numVal;
    }

    public String getEmoji() {
        return emoji;
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
