package com.mouldycheerio.discord.orangepeel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

public class MetricsSystem {
    public static void logCommand(String user, String command) {
        File file = new File("metrics/commands.all");
        file.mkdirs();
        try {
            Writer output = new BufferedWriter(new FileWriter(file, true));
            output.append(System.currentTimeMillis() + "|" + user + "|" + command + "\n");
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
