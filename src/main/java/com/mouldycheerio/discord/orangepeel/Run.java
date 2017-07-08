package com.mouldycheerio.discord.orangepeel;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Run {
    private static JSONObject config;

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            config = new JSONObject();
            loadAll();

            OrangePeel orangepeel = null;
            try {
                orangepeel = new OrangePeel(config.getString("token"), config.getString("prefix"));
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (Exception e1) {
                // TODO Auto-generated catch block

                e1.printStackTrace();
                System.exit(0);
            }

            long a = 0;
            Scanner in = new Scanner(System.in);
            while (true) {

                try {
                    orangepeel.loop(a);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                a++;
                // Logger.raw(a + "");
                Thread.sleep(50);
                if (orangepeel.getStatus() == BotStatus.SHUTTINGDOWN) {
                    System.exit(0);
                }

                if (orangepeel.getStatus() == BotStatus.REBOOTING) {
                    System.out.println("RESTARTING!!");
                    break;
                }
            }

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
