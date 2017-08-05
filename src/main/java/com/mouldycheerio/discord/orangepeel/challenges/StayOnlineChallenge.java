package com.mouldycheerio.discord.orangepeel.challenges;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.StatusType;

public class StayOnlineChallenge extends OrangePeelChallenge {
    public long start = System.currentTimeMillis();

    public StayOnlineChallenge(IUser user, IDiscordClient client) {
        setClient(client);
        setUser(user);
        setMaxProgress(20*60*60*24);
        setProgress(0);
        setStatus(ChallengeStatus.ACTIVE);
        setType(ChallengeType.ONLINE);
        setDescription(new ChallengeDescription("Stay Online", "Stay online for a whole day."));
        getDescription().setLongdescription("Stay online for 24 whole hours. This does include being on idle and do not disturb. Being atall offline, will fail you the challenge.");
        start = System.currentTimeMillis();
    }

    public boolean check() {
        if (getUser().getPresence().getStatus() == StatusType.OFFLINE) {
            fail();
        } else {
            incrementProgress();
            if (getProgress() > getMaxProgress()) {
                win();
            }
        }
        return false;
    }

}
