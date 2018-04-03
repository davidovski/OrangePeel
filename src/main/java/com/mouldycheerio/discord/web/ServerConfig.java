package com.mouldycheerio.discord.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.mouldycheerio.discord.orangepeel.MagicChannel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;

public class ServerConfig {

    public static final String PATH = "servers/";

    public String autoRoleID = "";
    public String mutedRoleID = "";
    private List<MagicChannel> magicChannels;

    private String greetMessage = "Welcome [user] to [server]";

    private String leaveMessage = "Goodbye [user]!";

    private boolean chargeNicks = true;


    public String greetChannelID = "";
    private IDiscordClient client;
    private IGuild guild;

    public ServerConfig(String id, IDiscordClient client) {
        this.client = client;
        try {
            guild = client.getGuildByID(Long.parseLong(id));
        } catch (Exception e) {
            throw new IllegalArgumentException("Please provide a valid server id!");
        }

        if (guild == null) {
            throw new IllegalArgumentException("Please provide a valid server id!");
        }

        load(guild.getStringID(), guild);
        save();

    }

    public ServerConfig(IDiscordClient client, IGuild guild) {
        this.client = client;
        this.guild = guild;
    }

    public void save() {
        JSONObject obj = new JSONObject();
        obj.put("autorole", autoRoleID);
        obj.put("join", greetMessage);
        obj.put("leave", leaveMessage);
        obj.put("nick", isChargeNicks());

        JSONObject list = new JSONObject();
        for (MagicChannel c : getMagicChannels()) {
            try {
                list.put(c.getVoiceChannel().getStringID(), c.getRole().getStringID());
            } catch (Exception e) {
            }
        }
        obj.put("magic", list);

        obj.put("greet", greetChannelID);
        obj.put("muted", mutedRoleID);
        try {
            File file = new File(PATH + getGuildID());
            FileWriter filew = new FileWriter(file);
            filew.write(obj.toString(1));
            filew.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void load(String filename, IGuild guild) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(ServerConfig.PATH + filename);
            JSONTokener parser = new JSONTokener(fileReader);

            JSONObject obj = (JSONObject) parser.nextValue();

            if (obj.has("autorole")) {
                autoRoleID = obj.getString("autorole");
            }
            if (obj.has("nick")) {
                setChargeNicks(obj.getBoolean("nick"));
            }
            if (obj.has("magic")) {
                JSONObject jo = obj.getJSONObject("magic");
                Iterator<?> keys = jo.keys();

                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    String value = jo.getString(key);

                    getMagicChannels().add(new MagicChannel(client, key, value));
                }
            }
            if (obj.has("join")) {
                greetMessage = obj.getString("join");
            }
            if (obj.has("leave")) {
                leaveMessage = obj.getString("leave");
            }
            if (obj.has("greet")) {
                greetChannelID = obj.getString("greet");
            }

            if (obj.has("muted")) {
                mutedRoleID = obj.getString("muted");
            }
            fileReader.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

        }
    }

    public IRole getRoleForChannel(IVoiceChannel c) {
        for (MagicChannel mc : magicChannels) {
            if (mc.getVoiceChannel().getLongID() == c.getLongID()) {
                return mc.getRole();
            }
        }
        return null;
    }



    public void reload() {
        load(guild.getStringID(), guild);
    }

    public boolean hasAutoRole() {
        return !autoRoleID.isEmpty();
    }

    public boolean hasGreetChannel() {
        return !greetChannelID.isEmpty();
    }

    public boolean hasMutedRole() {
        return !mutedRoleID.isEmpty();
    }

    public void setAutoRole(IRole r) {
        autoRoleID = r.getStringID();
    }

    public void setMutedRole(IRole r) {
        mutedRoleID = r.getStringID();
    }

    public void setGreetChannel(IChannel r) {
        greetChannelID = r.getStringID();
    }
    public IRole getAutoRole() {
        try {
            if (!autoRoleID.equals("")) {
                IRole roleByID = guild.getRoleByID(Long.parseLong(autoRoleID));
                return roleByID;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public IRole getMutedRole() {
        try {
            if (!mutedRoleID.equals("")) {
                IRole roleByID = guild.getRoleByID(Long.parseLong(mutedRoleID));
                return roleByID;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public IChannel getGreetChannel() {
        try {
            if (!greetChannelID.equals("")) {
                IChannel channelByID = guild.getChannelByID(Long.parseLong(greetChannelID));
                return channelByID;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public IGuild getGuild() {
        return guild;
    }

    public void setGuild(IGuild guild) {
        this.guild = guild;
    }

    public long getGuildID() {
        return guild.getLongID();
    }

    @Override
    public String toString() {
        return "autorole: " + autoRoleID + "\n" + "greetChannel: " + greetChannelID + "\n" + "mutedRole: " + mutedRoleID + "\n" + "join: " + getGreetMessage() + "\n" + "leave: "
                + getLeaveMessage();
    }

    public String getLeaveMessage(IGuild g, IUser u) {
        String s = leaveMessage;
        s = s.replace("[user]", u.getName() + "#" + u.getDiscriminator());
        s = s.replace("[server]", "**" + g.getName() + "**");

        if (leaveMessage.contains("[count]")) {
            String usernum = g.getUsers().size() + "";
            if (usernum.endsWith("1")) {
                usernum = usernum + "st";
            } else if (usernum.endsWith("2")) {
                usernum = usernum + "nd";
            } else if (usernum.endsWith("3")) {
                usernum = usernum + "rd";
            } else {
                usernum = usernum + "th";
            }
            s = s.replace("[count]", usernum);
        }
        return s;
    }

    public String getGreetMessage(IGuild g, IUser u) {
        String s = greetMessage;
        s = s.replace("[user]", u.mention());
        s = s.replace("[server]", "**" + g.getName() + "**");

        if (greetMessage.contains("[count]")) {
            String usernum = g.getUsers().size() + "";
            if (usernum.endsWith("1")) {
                usernum = usernum + "st";
            } else if (usernum.endsWith("2")) {
                usernum = usernum + "nd";
            } else if (usernum.endsWith("3")) {
                usernum = usernum + "rd";
            } else {
                usernum = usernum + "th";
            }
            s = s.replace("[count]", usernum);
        }
        return s;
    }

    public String getGreetMessage() {
        return greetMessage;
    }

    public void setGreetMessage(String greetMessage) {
        this.greetMessage = greetMessage;
    }

    public String getLeaveMessage() {
        return leaveMessage;
    }

    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
    }

    public List<MagicChannel> getMagicChannels() {
        if (magicChannels == null) {
            magicChannels = new ArrayList<MagicChannel>();
        }
        return magicChannels;
    }

    public void setMagicChannels(List<MagicChannel> magicChannels) {
        this.magicChannels = magicChannels;
    }

    public void setAutoRoleID(String string) {
        autoRoleID = string;

    }

    public void setMutedRoleID(String string) {
        mutedRoleID = string;
    }

    public void setGreetChannelID(String string) {
        greetChannelID = string;

    }

    public boolean isChargeNicks() {
        return chargeNicks;
    }

    public void setChargeNicks(boolean chargeNicks) {
        this.chargeNicks = chargeNicks;
    }

}
