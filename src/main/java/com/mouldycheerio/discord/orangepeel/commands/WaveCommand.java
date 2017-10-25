package com.mouldycheerio.discord.orangepeel.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.mouldycheerio.discord.orangepeel.Logger;
import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class WaveCommand extends OrangePeelCommand {
    public WaveCommand() {
        setName("wave");
        setDescription(new CommandDescription("wave", "Makes your text Wavey", "wavey <txt>"));
        setCatagory(CommandCatagory.UTIL);
    }

    @Override
    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {

        StringBuilder sb = new StringBuilder();
        if (args.length > 1) {
            for (int i = 1; i < args.length; i++) {
                if (i > 1) {
                    sb.append(" ");
                }
                sb.append(args[i]);
            }
        }
        String s = sb.toString();

        String m = ".";

        int i = 0;
        double amplitiude = 4.0;

        for (char c : s.toCharArray()) {

            Logger.info("" + (Math.sin(i) * amplitiude) + amplitiude);
            for (int j = 0; j < (Math.sin(i) * amplitiude) + (amplitiude); j++) {

                m = m + " ";
            }
            m = m + c;
            m = m + "\n";
            i++;
        }

        if (m.length() < 2000) {
            commandMessage.getChannel().sendMessage(m);
        } else {
            commandMessage.getChannel().sendMessage("```[ERROR] String too long!```");
        }
    }

    public static String getHTTP(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line + "\n");
        }
        rd.close();
        return result.toString();
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
