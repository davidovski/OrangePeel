package com.mouldycheerio.discord.orangepeel.commands.coin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.PeelingUtils;

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

    public void incrementCoins(int a, IUser u, IGuild g, boolean announce) {
        setCoinsForUser(a + getCoinsForUser(u, g), u, g);

        if (a > 1 && announce) {
            u.getOrCreatePMChannel().sendMessage("You just gained " + a + emote + " on " + g.getName() + ". Your total balance for that server is now " + coins.get(g).get(u));
        }

    }

    public void incrementCoins(IUser u, IGuild g) {
        incrementCoins(1, u, g, false);
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
            savePeels();
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

    public void save() {

        saveCoins();
        savePeels();

    }

    public void saveCoins() {
        for (Entry<IGuild, Map<IUser, Integer>> entry : coins.entrySet()) {
            try {
                JSONObject g = new JSONObject();
                for (Entry<IUser, Integer> u : entry.getValue().entrySet()) {
                    try {
                        String stringID = u.getKey().getStringID();
                        Integer value = u.getValue();

                        g.put(stringID, value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                PeelingUtils.writeToFile(PeelingUtils.getDataFile(entry.getKey().getStringID(), "coins/money"), g.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void savePeels() {
        JSONObject peels = new JSONObject();
        Set<Entry<IUser, Integer>> entrySet = peelPoints.entrySet();
        for (Entry<IUser, Integer> u : entrySet) {
            try {
                String stringID = u.getKey().getStringID();
                Integer value = u.getValue();
                peels.put(stringID, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        PeelingUtils.writeToFile(PeelingUtils.getDataFile("peels", "coins"), peels.toString());
    }

    public void load() throws IOException {
        loadCoins();
        loadPeels();
    }

    public void loadCoins() {
        File folder = PeelingUtils.getDataFolder("coins/money/");
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            ArrayList<File> filelist = new ArrayList<File>();
            for (File f : files) {
                if (f.exists()) {
                    try {
                        FileReader fileReader = new FileReader(f);
                        JSONTokener parser = new JSONTokener(fileReader);

                        JSONObject obj = (JSONObject) parser.nextValue();

                        IGuild g = orangePeel.getClient().getGuildByID(Long.parseLong(f.getName().substring(0, f.getName().length() - 4)));
                        if (g != null) {
                            HashMap<IUser, Integer> map = new HashMap<IUser, Integer>();

                            Iterator<String> keys = obj.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                int value = obj.getInt(key);
                                IUser user = orangePeel.getClient().getUserByID(Long.parseLong(key));
                                if (user != null) {
                                    System.out.println(user.getStringID() + " //" + value);
                                    map.put(user, value);
                                }
                            }
                            coins.put(g, map);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void loadPeels() {

        try {
            FileReader fileReader = new FileReader(PeelingUtils.getDataFile("peels", "coins"));
            JSONTokener parser = new JSONTokener(fileReader);

            JSONObject obj = (JSONObject) parser.nextValue();

            Iterator<String> keys = obj.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                int value = obj.getInt(key);
                IUser user = orangePeel.getClient().getUserByID(Long.parseLong(key));
                if (user != null) {
                    peelPoints.put(user, value);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
