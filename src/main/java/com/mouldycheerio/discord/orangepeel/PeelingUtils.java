package com.mouldycheerio.discord.orangepeel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Random;

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

    public static String getRandomToken() {
        Random r = new Random();
        String token = "";
        String alphabet = "1234567890qwertyuiopasdfghjklzxcvbnm_.";

        for (int i = 0; i < 10; i++) {
            token = token + alphabet.charAt(r.nextInt(alphabet.length()));
        }
        return token;
    }

    public static String writeToFile(File filename, String data) {
        try {
            FileWriter file = new FileWriter(filename);
            file.write(data);
            file.flush();

            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static File getDataFile(String filename, String catagory) {
        File folder = new File("data/" + catagory);
        folder.mkdirs();

        File file = new File(folder, filename + ".opf");

        return file ;

    }

    public static File getDataFolder( String catagory) {
        File folder = new File("data/" + catagory);
        folder.mkdirs();
        return folder;

    }

    public static File getFileDenarySearch(String id, String catagory, String name) {
        File folder = new File("data/" + catagory);
        folder.mkdirs();
        String path = "";
        for (char c : id.toCharArray()) {
            path = path + c +"/";
        }
        File location = new File(folder, path);
        location.mkdirs();
        File file = new File(location, name + ".opf");

        return file ;

    }

    public static void saveUrl(String output, String urlString) {
        try {
            URL website = new URL(urlString);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(output);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            rbc.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
