package com.mouldycheerio.discord.orangepeel.challenges;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IUser;

public class TimeChallenge extends OrangePeelChallenge {
    public long start = System.currentTimeMillis();
    public TimeChallenge(IUser user, IDiscordClient client) {
        setClient(client);
        setUser(user);
        setMaxProgress(20);
        setProgress(0);
        setStatus(ChallengeStatus.ACTIVE);
        setType(ChallengeType.TIME);
        setDescription(new ChallengeDescription("Just a sec...", "Wait one second"));
        start = System.currentTimeMillis();
    }
    public boolean check() {
        incrementProgress();
        if (getProgress() > getMaxProgress()) {
            win();
        }
        return false;
    }

}
