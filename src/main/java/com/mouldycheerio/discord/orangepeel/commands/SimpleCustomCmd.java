package com.mouldycheerio.discord.orangepeel.commands;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class SimpleCustomCmd extends OrangePeelCommand {
    private String text;
    private boolean showInHelp;
    private String command;
    public SimpleCustomCmd(String command, CommandDescription description, String text) {
        this.text = text;
        setName(command);
        setDescription(description);
        showInHelp = true;
    }

    public SimpleCustomCmd(String command, String desc, String text) {
        this.text = text;
        setName(command);
        setDescription(new CommandDescription(command, desc, command));
        showInHelp = true;

    }

    @Override
    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        String toSay = compute(orangepeel, commandMessage, args);
        commandMessage.getChannel().sendMessage(toSay);
    }

    public String compute(OrangePeel bot, IMessage commandMessage, String[] args) {
        String toSay = text.replace("[@user]", commandMessage.getAuthor().mention());
        Random r = new Random();
        toSay = toSay.replace("[random.dice]", (1 + r.nextInt(6)) + "");

        toSay = toSay.replace("[random.decimal]", Math.random() + "");
        toSay = toSay.replace("[bot.servers]", "" + bot.getClient().getGuilds().size());
        toSay = toSay.replace("[bot.users]", "" + bot.getClient().getUsers().size());
        toSay = toSay.replace("[bot.channels]", "" + bot.getClient().getChannels().size());
        toSay = toSay.replace("[bot.uptime]", "" + bot.getUptime());

        String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String dateStamp = new SimpleDateFormat("dd/MM/yyyy").format(new Date());



        toSay = toSay.replace("[time]", timeStamp);
        toSay = toSay.replace("[date]", dateStamp);
        toSay = toSay.replace("[sever]", commandMessage.getGuild().getName());
        toSay = toSay.replace("[channel]", commandMessage.getChannel().getName());
        toSay = toSay.replace("[sever]", commandMessage.getGuild().getName());

        int i = 0;
        for (String arg : args) {
            toSay = toSay.replace("[@" + i + "]", arg);
            i++;
        }

        return toSay;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isShowInHelp() {
        return showInHelp;
    }

    public void setShowInHelp(boolean showInHelp) {
        this.showInHelp = showInHelp;
    }
}
