package com.mouldycheerio.discord.web;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class PeelUtil {

    public static HashMap<String, String> cache = new HashMap<String, String>();
    public static int cachereset = 99;

    public static String repeat(String s, int times) {
        String r = "";
        for (int i = 0; i < times; i++) {
            r = r + s;
        }
        return r;
    }

    public static String getRandomToken() {
        Random r = new Random();
        String token = "";
        String alphabet = "1234567890qwertyuiopasdfghjklzxcvbnm";

        for (int i = 0; i < 10; i++) {
            token = token + alphabet.charAt(r.nextInt(alphabet.length()));
        }
        return token;
    }

    public static <T> Map<T, Integer> mostCommon(List<T> list) {
        Map<T, Integer> map = new HashMap<T, Integer>();

        for (T t : list) {
            Integer val = map.get(t);
            map.put(t, val == null ? 1 : val + 1);
        }

        Entry<T, Integer> max = null;

        for (Entry<T, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }

        return map;
    }

    public static String readFile(String name) {
        cachereset++;
        if (cachereset > 100) {
            cachereset = 0;
            System.out.println("resetcache");
            cache.clear();
            return readFile(name, true);
        }
        return readFile(name, false);

    }

    public static String readFile(String name, boolean resetCache) {
        if (!resetCache) {
            if (cache.containsKey(name)) {
                return cache.get(name);
            }
        }
        InputStream is;
        try {
            is = new FileInputStream(name);

            BufferedReader buf = new BufferedReader(new InputStreamReader(is));

            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();

            while (line != null) {
                sb.append(line).append("\n");
                line = buf.readLine();
            }

            buf.close();

            String fileAsString = sb.toString();
            cache.put(name, fileAsString);
            return fileAsString;
        } catch (Exception e) {
            e.printStackTrace();
            return "404";
        }

    }
}
