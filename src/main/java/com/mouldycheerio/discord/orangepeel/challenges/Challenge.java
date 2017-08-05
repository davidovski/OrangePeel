package com.mouldycheerio.discord.orangepeel.challenges;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IUser;

public interface Challenge {
    IUser getUser();
    IDiscordClient getClient();
    long getMaxProgress();
    long getProgress();
    ChallengeStatus getStatus();
    void fail();
    void win();
    boolean check();
    String toString();
    ChallengeDescription getDescription();
    ChallengeType getType();



}
