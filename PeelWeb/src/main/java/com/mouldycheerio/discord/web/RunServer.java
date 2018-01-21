package com.mouldycheerio.discord.web;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONTokener;

public class RunServer {

    public static int port = 8080;
    private static JSONObject config;
    private static String configname = "config.json";
    public static void main(String[] args) throws IOException {

        for (String s : args) {
            if (s.startsWith("-p=")) {
                port = Integer.parseInt(s.substring(3));
            }
            if (s.startsWith("-config=")) {
                configname = s.substring(8);
                System.out.println(configname);
            }
        }

        config = new JSONObject();
        load();
        WebServer webServer = new WebServer(config.getString("token"));

        webServer.run(port);
    }

    public static void load() {

        try {
            JSONTokener parser = new JSONTokener(new FileReader(configname));

            JSONObject obj = (JSONObject) parser.nextValue();

            config = obj;
            System.out.println(config);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
