package com.mouldycheerio.discord.orangepeel.challenges;

public enum ChallengeType {
    LIVE(0, "Live"),
    TIME(1, "Timed"),
    ONLINE(2, "Online"),
    NO_AVATAR(3, "NoAvatar"),
    NO_MENTIONS(4, "NoMentions"),
    OFFLINE(5, "Offline");

    private int numVal;
    private String name;

    ChallengeType(int numVal, String name) {
        this.numVal = numVal;
        this.name = name;
    }

    public int getId() {
        return numVal;
    }

    @Override
    public String toString() {
        return name;
    }

    public static ChallengeType getById(int id) {
        for(ChallengeType e : values()) {
            if(e.getId() == id) return e;
        }
        return null;
     }

    public static ChallengeType getByName(String name) {
        for(ChallengeType e : values()) {
            if(e.toString().equals(name)) return e;
        }
        return null;
     }
}
