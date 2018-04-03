package com.mouldycheerio.discord.orangepeel;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Scanner;

import org.json.JSONObject;
import org.json.JSONTokener;

public class Run {
    private static JSONObject config;

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            config = new JSONObject();
            load();

            OrangePeel orangepeel = null;
            try {
                orangepeel = new OrangePeel(config.getString("token"), config.getString("prefix"));
                String auth = config.getString("listAuth");
                String dbl = config.getString("dblToken");
                orangepeel.setDbltoken(dbl);
                System.out.println("bot list auth: " + auth);
                orangepeel.setBotListToken(auth);
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

                    if (orangepeel.getLogChannel() != null) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        e.printStackTrace(pw);
                        sw.toString();
                        orangepeel.logError(e);
                    }
                }
                a++;
                // Logger.raw(a + "");
                Thread.sleep(50);
                if (orangepeel.getStatus() == BotStatus.SHUTTINGDOWN) {
                    if (orangepeel.getLogChannel() != null) {
                        orangepeel.getLogChannel().sendMessage("**SHUTTING DOWN**");
                    }
                    System.exit(0);
                }

                if (orangepeel.getStatus() == BotStatus.REBOOTING) {
                    System.out.println("RESTARTING!!");
                    if (orangepeel.getLogChannel() != null) {
                        orangepeel.getLogChannel().sendMessage("**REBOOTING**");
                    }
                    break;
                }
            }
            orangepeel = null;


        }

        //
    }

    public static void load() {

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
