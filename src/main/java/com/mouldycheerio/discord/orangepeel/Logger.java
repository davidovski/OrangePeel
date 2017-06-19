package com.mouldycheerio.discord.orangepeel;

public class Logger {
    public static void info(String s) {
        System.out.println("[INFO]" + s);
    }
    public static void error(String s) {
        System.out.println("[ERROR]" + s);
    }
    public static void warn(String s) {
        System.out.println("[WARN]" + s);
    }
    public static void raw(String s) {
        System.out.println(s);
    }
}
