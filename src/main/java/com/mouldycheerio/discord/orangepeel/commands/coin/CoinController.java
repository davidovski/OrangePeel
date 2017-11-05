package com.mouldycheerio.discord.orangepeel.commands.coin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class CoinController {
    public static String emote = "<:coin:376493562092716033>";


    private Map<IGuild, Map<IUser, Integer>> coins;

    private Map<IUser, Integer> peelPoints;

    private OrangePeel orangePeel;

    public CoinController(OrangePeel orangePeel) {
        this.orangePeel = orangePeel;
        coins = new HashMap<IGuild, Map<IUser, Integer>>();
        peelPoints = new HashMap<IUser, Integer>();
    }

    public void setCoinsForUser(int c, IUser u, IGuild g) {
        try {
            if (coins.containsKey(g)) {
                coins.get(g).put(u, c);
            } else {
                coins.put(g, new HashMap<IUser, Integer>());
                coins.get(g).put(u, c);
            }
        } catch (Exception e) {

        }
    }

    public void incrementCoins(int a, IUser u, IGuild g) {
        setCoinsForUser(a + getCoinsForUser(u, g), u, g);
    }

    public void incrementCoins(IUser u, IGuild g) {
        incrementCoins(1, u, g);
    }

    public int getCoinsForUser(IUser u, IGuild g) {
        try {
            return coins.get(g).get(u);
        } catch (Exception e) {
            setCoinsForUser(0, u, g);
            return 0;
        }
    }

    public void setPeelsForUser(int c, IUser u) {
        try {
            peelPoints.put(u, c);
        } catch (Exception e) {

        }
    }

    public void incrementPeels(int a, IUser u) {
        setPeelsForUser(a + getPeelsForUser(u), u);
    }

    public void incrementPeels(IUser u) {
        incrementPeels(1, u);
    }

    public int getPeelsForUser(IUser u) {
        try {
            return peelPoints.get(u);
        } catch (Exception e) {
            setPeelsForUser(0, u);
            return 0;
        }
    }

    public JSONObject save(JSONObject obj) {
        JSONObject coinz = new JSONObject();
        for (Entry<IGuild, Map<IUser, Integer>> entry : coins.entrySet()) {
            JSONObject g = new JSONObject();
            for (Entry<IUser, Integer> u : entry.getValue().entrySet()) {
                g.put(u.getKey().getStringID(), u.getValue().intValue());
            }
            coinz.put(entry.getKey().getStringID(), g);
        }

        JSONObject peels = new JSONObject();
        for (Entry<IUser, Integer> u : peelPoints.entrySet()) {
            peels.put(u.getKey().getStringID(), u.getValue());
        }

        obj.put("peels", peels);
        obj.put("coins", coinz);

        return obj;
    }

    public void load(JSONObject obj) {
        if (obj.has("peels")) {
            JSONObject o = obj.getJSONObject("peels");
            Iterator<String> keys = o.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                peelPoints.put(orangePeel.getClient().getUserByID(Long.parseLong(next)), o.getInt(next));
            }
        }
        if (obj.has("coins")) {
            JSONObject jo = obj.getJSONObject("coins");
            Iterator<String> keys = jo.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                JSONObject jjo = jo.getJSONObject(next);

                HashMap<IUser, Integer> hashMap = new HashMap<IUser, Integer>();

                Iterator<String> keysj = jjo.keys();
                while (keysj.hasNext()) {
                    String nextj = keysj.next();
                    hashMap.put(orangePeel.getClient().getUserByID(Long.parseLong(nextj)), jjo.getInt(nextj));

                }

                coins.put(orangePeel.getClient().getGuildByID(Long.parseLong(next)), hashMap);

            }
        }
    }
}
