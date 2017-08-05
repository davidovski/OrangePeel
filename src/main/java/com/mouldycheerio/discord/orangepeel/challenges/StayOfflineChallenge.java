package com.mouldycheerio.discord.orangepeel.challenges;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.StatusType;

public class StayOfflineChallenge extends OrangePeelChallenge {
    public long start = System.currentTimeMillis();

    public StayOfflineChallenge(IUser user, IDiscordClient client) {
        setClient(client);
        setUser(user);
        setMaxProgress(20 * 60 * 60 * 24);
        setProgress(0);
        setStatus(ChallengeStatus.ACTIVE);
        setType(ChallengeType.OFFLINE);
        setDescription(new ChallengeDescription("Stay Offline", "Do not go on discord for 24 hours"));
        getDescription().setLongdescription("Be offline for 24 hours, you will have one minute before the challenge starts, so you can say your goodbyes to discord for a day.");
        start = System.currentTimeMillis();
    }

    public boolean check() {
        if (getProgress() < 20 * 60) {
            incrementProgress();
        } else {
            if (getUser().getPresence().getStatus() != StatusType.OFFLINE) {
                fail();
            } else {
                incrementProgress();
                if (getProgress() > getMaxProgress()) {
                    win();
                }
            }
        }
        return false;
    }

}
