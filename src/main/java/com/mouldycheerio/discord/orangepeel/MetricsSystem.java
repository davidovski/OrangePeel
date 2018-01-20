package com.mouldycheerio.discord.orangepeel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

public class MetricsSystem {
    public static void logCommand(long l, String command, long m) {
        File file = new File("metrics/commands.all");
        file.mkdirs();
        try {
            Writer output = new BufferedWriter(new FileWriter(file, true));
            output.append(System.currentTimeMillis() + "|" + l + "|" + command + "|" + m + "\n");
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
