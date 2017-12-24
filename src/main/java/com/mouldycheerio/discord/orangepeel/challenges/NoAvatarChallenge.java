package com.mouldycheerio.discord.orangepeel.challenges;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IUser;

public class NoAvatarChallenge extends OrangePeelChallenge {
    public long start = System.currentTimeMillis();

    public NoAvatarChallenge(OrangePeel orangePeel, IUser user, IDiscordClient client) {
        super(orangePeel);
        setClient(client);
        setUser(user);
        setMaxProgress(20*60*60*24);
        setProgress(0);
        setStatus(ChallengeStatus.ACTIVE);
        setType(ChallengeType.NO_AVATAR);
        setDescription(new ChallengeDescription(        "No Avatar", "Remove your avatar, and keep it that was for a day."));
        getDescription().setLongdescription("To win the challenge, you must not have an avatar at all (only the default) for 24 whole hours. ");
        start = System.currentTimeMillis();
    }



    public boolean check() {
        try {
        if (getUser().getAvatarURL().startsWith("https://cdn.discordapp.com/embed/avatars/")) {
            incrementProgress();
        } else {
            fail();
        }
        if (getProgress() > getMaxProgress()) {
            win();
        }
        } catch (Exception e) {}
        return false;
    }

}
