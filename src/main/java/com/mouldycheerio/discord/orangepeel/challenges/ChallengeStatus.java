package com.mouldycheerio.discord.orangepeel.challenges;

public enum ChallengeStatus {
    INACTIVE(0), ACTIVE(1), COMPLETE(2);

    private int numVal;

    ChallengeStatus(int numVal) {
        this.numVal = numVal;
    }

    public int getNumVal() {
        return numVal;
    }

    public static ChallengeStatus getById(int id) {
        for (ChallengeStatus e : values()) {
            if (e.getNumVal() == id)
                return e;
        }
        return null;
    }
}
