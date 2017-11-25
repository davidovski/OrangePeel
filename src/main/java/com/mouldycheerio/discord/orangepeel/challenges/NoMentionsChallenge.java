package com.mouldycheerio.discord.orangepeel.challenges;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

public class NoMentionsChallenge extends OrangePeelChallenge {
    public long start = System.currentTimeMillis();

    public NoMentionsChallenge(OrangePeel orangePeel, IUser user, IDiscordClient client) {
        super(orangePeel);
        setClient(client);
        setUser(user);
        setMaxProgress(100);
        setProgress(0);
        setStatus(ChallengeStatus.ACTIVE);
        setType(ChallengeType.NO_MENTIONS);
        setDescription(new ChallengeDescription("No Mentions", "Do not mention anyone in the next 100 of your messages."));
        getDescription().setLongdescription("In the next 100 messages that you post on a server that you share with me, if there is an @ symbol you will fail the challenge.");
        start = System.currentTimeMillis();
    }

    @Override
    public void onMessage(MessageReceivedEvent event) {
        if (event.getAuthor().getStringID().equals(getUser().getStringID())) {
            if (!event.getMessage().getContent().contains("<@")) {
                incrementProgress();
            } else {
                fail();
            }
        }
    }

    public boolean check() {

        if (getProgress() > getMaxProgress()) {
            win();
        }
        return false;
    }

}
