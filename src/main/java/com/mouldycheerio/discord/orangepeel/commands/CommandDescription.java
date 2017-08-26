package com.mouldycheerio.discord.orangepeel.commands;

public class CommandDescription {
    private String title;
    private String text;
    private String usage;

    public CommandDescription(String title, String text, String usage) {
        this.title = title;
        this.text = text;
        this.usage = usage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    @Override
    public String toString() {
        return "**" + title + ":** " + text;
    }
}
