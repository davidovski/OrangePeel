package com.mouldycheerio.discord.orangepeel.commands;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;

public class FakeUserCommand extends OrangePeelAdminCommand {
    public FakeUserCommand() {
        setName("generateUser");
        setDescription(new CommandDescription("Generate User", "Generates a random user.", "generateUser"));
        setCommandlvl(1);
        setNoPermText("You can't do this! (but yours friend might be able to...");
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        orangepeel.getStatsCounter().incrementStat("coords");
        try {
            IMessage m = commandMessage.getChannel().sendMessage("Generating...");
            String json = getHTTP("https://randomuser.me/api/");
            JSONTokener parser = new JSONTokener(json);
            JSONObject obj = (JSONObject) parser.nextValue();
            JSONObject jsonObject = obj.getJSONArray("results").getJSONObject(0);
            String fullname = jsonObject.getJSONObject("name").getString("first") + " " + jsonObject.getJSONObject("name").getString("last");
            String username = jsonObject.getJSONObject("login").getString("username");
            String icon = jsonObject.getJSONObject("picture").getString("large");
            String icon2 = jsonObject.getJSONObject("picture").getString("thumbnail");

            String email = jsonObject.getString("email");
            String phone = jsonObject.getString("phone");
            String dob = jsonObject.getString("dob");

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.withTitle(fullname);
            embedBuilder.withColor(new Color((int) (Math.random() * 255), (int) (Math.random() * 255),(int) (Math.random() * 255)));
            embedBuilder.appendField("username", username, false);
            embedBuilder.appendField("email", email, false);
            embedBuilder.appendField("phone number", phone, false);
            embedBuilder.appendField("Date Of Birth", dob, true);
            embedBuilder.withImage(icon);
            embedBuilder.withAuthorName("[randomuser.me]");
            embedBuilder.withAuthorUrl("https://randomuser.me/");
            embedBuilder.withAuthorIcon(client.getOurUser().getAvatarURL());
            embedBuilder.withThumbnail(icon2);



            m.edit(embedBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            sw.toString();
            commandMessage.getChannel().sendMessage("lolwut? \n```" + sw + "```");

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
