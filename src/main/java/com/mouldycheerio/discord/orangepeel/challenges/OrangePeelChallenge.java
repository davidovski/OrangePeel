package com.mouldycheerio.discord.orangepeel.challenges;

import org.json.JSONObject;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

public abstract class OrangePeelChallenge implements Challenge {

    private ChallengeStatus status = ChallengeStatus.INACTIVE;
    private ChallengeType type = ChallengeType.LIVE;
    private ChallengeDescription description;
    private long maxProgress = 0;
    private long progress = 0;
    private IDiscordClient client;
    private IUser user;
    private OrangePeel orangePeel;


    public OrangePeelChallenge(OrangePeel orangePeel) {
        this.orangePeel = orangePeel;

    }

    public void onMessage(MessageReceivedEvent event) {
    }

    public void fail() {
        getClient().getOrCreatePMChannel(getUser()).sendMessage("You have failed your challenge: `" + getDescription().getName() + "`" );

        setStatus(ChallengeStatus.INACTIVE);
    }

    public void win() {
        getClient().getOrCreatePMChannel(getUser()).sendMessage("You have completed your challenge: `" + getDescription().getName() + "`"+ "\n You have recieved 1 Peel point, check your peel points with `>balance`");
        orangePeel.coinController().incrementPeels(getUser());
        setStatus(ChallengeStatus.COMPLETE);
    }

    public long getMaxProgress() {
        return maxProgress;
    }

    public long getProgress() {
        return progress;
    }

    public IUser getUser() {
        return user;
    }

    public IDiscordClient getClient() {
        return client;
    }

    public ChallengeDescription getDescription() {
        return description;
    }

    public ChallengeStatus getStatus() {
        return status;
    }

    public void setStatus(ChallengeStatus status) {
        this.status = status;
    }

    public void setMaxProgress(long maxProgress) {
        this.maxProgress = maxProgress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public void incrementProgress() {
        progress++;
    }

    public void setClient(IDiscordClient client) {
        this.client = client;
    }

    public void setUser(IUser user) {
        this.user = user;
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        JSONObject userObj = new JSONObject();
        if (user != null) {
            userObj.put("id", user.getLongID());
        }
        obj.put("user", userObj);
        JSONObject desc = new JSONObject();
        desc.put("name", description.getName());
        desc.put("description", description.getDescription());
        obj.put("desc", desc);
        obj.put("progress", progress);
        obj.put("maxProgress", maxProgress);
        obj.put("typeID", type.getId());
        obj.put("status", status.getNumVal());
        return obj;
    }

    public ChallengeType getType() {
        return type;
    }

    public void setType(ChallengeType type) {
        this.type = type;
    }

    public void setDescription(ChallengeDescription description) {
        this.description = description;
    }

}
