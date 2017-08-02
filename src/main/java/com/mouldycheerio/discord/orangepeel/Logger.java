package com.mouldycheerio.discord.orangepeel;

import sx.blah.discord.Discord4J;

public class Logger {
    public static void info(String s) {
        Discord4J.LOGGER.info(s);
    }
    public static void error(String s) {
        Discord4J.LOGGER.error(s);
        }
    public static void warn(String s) {
        Discord4J.LOGGER.warn(s);
    }
    public static void raw(String s) {
        System.out.println(s);
    }
}
