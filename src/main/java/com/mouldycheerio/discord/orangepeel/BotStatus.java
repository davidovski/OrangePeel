package com.mouldycheerio.discord.orangepeel;

public enum BotStatus {
    INACTIVE(0),
    ACTIVE(1),
    REBOOTING(2),
    SHUTTINGDOWN(3);

    private int numVal;

    BotStatus(int numVal) {
        this.numVal = numVal;
    }

    public int getNumVal() {
        return numVal;
    }
}
