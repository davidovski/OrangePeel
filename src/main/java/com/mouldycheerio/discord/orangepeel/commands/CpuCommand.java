package com.mouldycheerio.discord.orangepeel.commands;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;

public class CpuCommand extends OrangePeelAdminCommand {
    public CpuCommand() {
        setName("cpu");
        setDescription(new CommandDescription("CPU", "Prints the stats regarding the cpu and memory usage of the bot.", "cpu"));
        setCommandlvl(1);
        addAlias("nerdstats");
        addAlias("nerd");
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        try {
            double cpu = getProcessCpuLoad();
            long currentTime = System.currentTimeMillis();
            long freeMemory = Runtime.getRuntime().freeMemory();
            long totalMemory = Runtime.getRuntime().totalMemory();

            int processors = Runtime.getRuntime().availableProcessors();
            long usedMemoryMB = (totalMemory - freeMemory) / 1000000;
            long totalMemoryMB = totalMemory / 1000000;
            File[] roots = File.listRoots();

            long yourmilliseconds = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
            Date resultdate = new Date(yourmilliseconds);

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.withTimestamp(currentTime);
            embedBuilder.withThumbnail(orangepeel.getClient().getOurUser().getAvatarURL());
            embedBuilder.withTitle("Orange Peel's nerd stats");
            embedBuilder.withDescription("Running since " + sdf.format(resultdate));
            embedBuilder.withColor(Color.ORANGE.darker());
            embedBuilder.appendField("Memory: ", usedMemoryMB + "mb / " + totalMemoryMB + "mb", true);
            embedBuilder.appendField("CPU usage:", (cpu * 100) + "%", true);
            embedBuilder.appendField("Processors", processors + " cores", true);
            embedBuilder.appendField("OS Name:", System.getProperty("os.name"), true);
            embedBuilder.appendField("OS Version:", System.getProperty("os.version"), true);
            embedBuilder.appendField("OS Arch:", System.getProperty("os.arch"), true);

            embedBuilder.appendField("Java Verion:", System.getProperty("java.version"), true);
            embedBuilder.appendField("Java Vendor:", System.getProperty("java.vendor"), true);
            embedBuilder.appendField("User Executing", System.getProperty("user.name"), true);
            embedBuilder.appendField("Client->Discord latency", pingUrl("https://discordapp.com/") + "ms", true);
            embedBuilder.appendField("Client->Discord latency", pingUrl("https://discordapp.com/") + "ms", true);


            EmbedBuilder embedBuilder2 = new EmbedBuilder();
            embedBuilder2.withThumbnail("https://media-elerium.cursecdn.com/avatars/33/19/635889567578916779.png");
            embedBuilder2.withColor(Color.ORANGE.darker());
            embedBuilder2.withTitle("Storage Info");
            embedBuilder2.withDescription("Found " + roots.length + " storage devices.");

            int index = 0;
            for (File root : roots) {
                index++;
                root.getAbsolutePath();
                long used_space = ((root.getTotalSpace() - root.getFreeSpace()) / 1000000000);
                embedBuilder2.appendField(index + ") " + root.getName(), used_space + "gb / " + (root.getTotalSpace() / 1000000000) + "gb", false);
            }

            commandMessage.getChannel().sendMessage(embedBuilder.build());
            commandMessage.getChannel().sendMessage(embedBuilder2.build());

        } catch (Exception e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            sw.toString();
            commandMessage.getChannel().sendMessage("lolwut? \n```" + sw + "```");
            orangepeel.logError(e,commandMessage);
        }

    }

    public static double getProcessCpuLoad() throws Exception {

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
        AttributeList list = mbs.getAttributes(name, new String[] { "ProcessCpuLoad" });

        if (list.isEmpty())
            return Double.NaN;

        Attribute att = (Attribute) list.get(0);
        Double value = (Double) att.getValue();

        // usually takes a couple of seconds before we get real values
        if (value == -1.0)
            return Double.NaN;
        // returns a percentage value with 1 decimal point precision
        return ((int) (value * 1000) / 10.0);
    }

    public static long pingUrl(final String address) {
        try {
            final URL url = new URL(address);
            final HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setConnectTimeout(1000 * 10); // mTimeout is in seconds
            final long startTime = System.currentTimeMillis();
            urlConn.connect();
            final long endTime = System.currentTimeMillis();
            return (endTime - startTime);
        } catch (final MalformedURLException e1) {
            e1.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
