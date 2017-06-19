package com.mouldycheerio.discord.orangepeel;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONTokener;

public class Run {
    private static JSONObject config;

    public static void main(String[] args) throws InterruptedException {
        config = new JSONObject();
        loadAll();

        OrangePeel orangepeel = new OrangePeel(config.getString("token"));

        long a = 0;


        while (true) {
            orangepeel.loop(a);
            a++;
            //Logger.raw(a + "");
            Thread.sleep(50);
        }
        //
    }


    public static void loadAll() {

        try {
            JSONTokener parser = new JSONTokener(new FileReader("config.json"));

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

