package com.mouldycheerio.discord.orangepeel.challenges;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.mouldycheerio.discord.orangepeel.Logger;
import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IUser;

public class ChallengeController {
    private OrangePeel orangePeel;
    private IDiscordClient client;

    private List<Challenge> challanges;

    public ChallengeController(OrangePeel orangePeel) {
        this.orangePeel = orangePeel;
        client = orangePeel.getClient();
        challanges = new ArrayList<Challenge>();
    }

    public void giveUserAllChallenges(IUser u) {
        System.out.println(u.getName());
        for (ChallengeType type : ChallengeType.values()) {
            boolean add = true;
            for (Challenge c : getChallengesForUser(u)) {
                if (c.getType() == type) {
                    add = false;
                    break;
                }
            }
            if (add) {
                System.out.println("added innactive challenge: " + type.toString());
                OrangePeelChallenge c = newChallange(u, type);
                c.setStatus(ChallengeStatus.INACTIVE);
                challanges.add(c);
            }
        }
    }

    public void update() {
        client = orangePeel.getClient();
        for (Challenge challenge : challanges) {
            if (challenge.getStatus() == ChallengeStatus.ACTIVE) {
                challenge.check();
            }

        }
    }

    public void appointChallenge(IUser u, ChallengeType type) {
        giveUserAllChallenges(u);
        for (Challenge challenge : challanges) {
            if (u.getStringID().equals(challenge.getUser().getStringID())) {
                if (challenge.getType() == type) {
                    if (challenge instanceof OrangePeelChallenge) {
                        ((OrangePeelChallenge) challenge).setStatus(ChallengeStatus.ACTIVE);
                        ((OrangePeelChallenge) challenge).setProgress(0);
                    }
                }
            }
        }
    }

    public void loadAll() throws FileNotFoundException {
        JSONTokener parser = new JSONTokener(new FileReader("challenges.opf"));
        JSONObject obj = (JSONObject) parser.nextValue();
        JSONArray b = obj.getJSONArray("challenges");

        Logger.info("challenges...");
        for (int i = 0; i < b.length(); i++) {
            Logger.info("loading " + i);
            JSONObject challenge = b.getJSONObject(i);
            JSONObject userObj = challenge.getJSONObject("user");
            long userID = userObj.getLong("id");
            JSONObject descObj = challenge.getJSONObject("desc");
            ChallengeDescription description = new ChallengeDescription(descObj.getString("name"), descObj.getString("description"));
            long progress = challenge.getLong("progress");
            long maxProgress = challenge.getLong("maxProgress");
            ChallengeType type = ChallengeType.getById(challenge.getInt("typeID"));
            ChallengeStatus status = ChallengeStatus.getById(challenge.getInt("status"));
            OrangePeelChallenge c = newChallange(client.getUserByID(userID), type);
            c.setClient(client);
            c.setDescription(description);
            c.setMaxProgress(maxProgress);
            c.setProgress(progress);
            c.setType(type);
            c.setStatus(status);
            challanges.add(c);
        }
    }

    public List<Challenge> getChallengesForUser(IUser u) {
        List<Challenge> cu = new ArrayList<Challenge>();
        for (Challenge challenge : challanges) {
            if (u.equals(challenge.getUser())) {
                cu.add(challenge);
            }
        }
        return cu;
    }

    public OrangePeelChallenge newChallange(IUser user, ChallengeType type) {
        if (type == ChallengeType.TIME) {
            return new TimeChallenge(user, client);
        } else if (type == ChallengeType.ONLINE) {
            return new StayOnlineChallenge(user, client);
        } else if (type == ChallengeType.NO_AVATAR) {
            return new NoAvatarChallenge(user, client);
        } else if (type == ChallengeType.NO_MENTIONS) {
            return new NoMentionsChallenge(user, client);
        } else if (type == ChallengeType.OFFLINE) {
            return new StayOfflineChallenge(user, client);
        } else {
            OrangePeelChallenge c = new OrangePeelChallenge() {
                public boolean check() {
                    win();
                    return false;
                }
            };
            c.setUser(user);
            c.setClient(client);
            c.setDescription(new ChallengeDescription(type.toString(), "just be alive"));
            c.setMaxProgress(0);
            c.setProgress(0);
            c.setType(type);
            c.setStatus(ChallengeStatus.INACTIVE);
            return c;
        }
    }

    public void saveAll() {
        JSONObject obj = new JSONObject();
        JSONArray array = new JSONArray();
        for (Challenge challenge : challanges) {
            if (challenge instanceof OrangePeelChallenge) {
                array.put(((OrangePeelChallenge) challenge).toJson());
            }
        }
        obj.put("challenges", array);

        try {
            FileWriter file = new FileWriter("challenges.opf");
            file.write(obj.toString(1));
            file.flush();

            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<Challenge> getChallanges() {
        return challanges;
    }

    public void setChallanges(List<Challenge> challanges) {
        this.challanges = challanges;
    }

}
