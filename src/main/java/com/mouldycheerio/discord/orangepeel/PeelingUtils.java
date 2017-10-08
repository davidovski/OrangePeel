package com.mouldycheerio.discord.orangepeel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class PeelingUtils {
    public static String mentionToId(String mention, IGuild server) {
        String id = "";
        for (char c : mention.toCharArray()) {
            if (Character.isDigit(c)) {
                id = id + c;
            }
        }
        if (id.length() <= 4) {

            for (IUser u : server.getUsers()) {
                if (u.getDiscriminator().equals(id) && u.getName().equals((mention.split("#")[0]))) {
                    id = u.getStringID();
                    break;
                }
            }
        }
        return id;
    }

    public static IUser mentionToUser(String mention, IGuild server) {
        String id = "";
        for (char c : mention.toCharArray()) {
            if (Character.isDigit(c)) {
                id = id + c;
            }
        }
        if (id.length() <= 4) {

            for (IUser u : server.getUsers()) {
                if (u.getDiscriminator().equals(id) && u.getName().equals((mention.split("#")[0]))) {
                    id = u.getStringID();
                    break;
                }
            }
        }
        for (IUser iUser : server.getUsers()) {
            if (iUser.getStringID().equals(id)) {
                return iUser;
            }
        }
        return null;
    }

    public static String mentionToIdEz(String mention) {
        String id = "";
        for (char c : mention.toCharArray()) {
            if (Character.isDigit(c)) {
                id = id + c;
            }
        }
        return id;
    }

    public static String getHTTP(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line + "\n");
        }
        rd.close();
        return result.toString();
    }
}
