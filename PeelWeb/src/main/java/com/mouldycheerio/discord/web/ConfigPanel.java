package com.mouldycheerio.discord.web;

import java.util.ArrayList;

import com.mrpowergamerbr.temmiediscordauth.TemmieDiscordAuth;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.Permissions;

public class ConfigPanel {

    public static String cacheID;
    public static ArrayList<String> servercache;

    public static String getPanelPage(IDiscordClient client, String ckie, WebServer server, String serverID) {
        String panel;
        panel = PeelUtil.readFile("assets/config.html");

        // if (server.getSession(ckie) != null) {

        TemmieDiscordAuth session = server.getSession(ckie);
        boolean authorised = false;
        if (session != null) {
            for (IGuild g : client.getGuilds()) {
                if (g.getStringID().equals(serverID)) {
                    IUser u = g.getUserByID(Long.parseLong(session.getCurrentUserIdentification().getId()));
                    if (u.getPermissionsForGuild(g).contains(Permissions.ADMINISTRATOR)) {
                        authorised = true;
                        break;
                    }
                }
            }
        }

        if (authorised) {
            ServerConfig c = new ServerConfig(serverID, client);
            panel = panel.replace("{{server}}", c.getGuild().getName());
            panel = panel.replace("{{id}}", c.getGuild().getStringID());

            panel = panel.replace("{{autorole}}", makeAutoroleList(c));
            panel = panel.replace("{{muted}}", makeMutedRoleList(c));
            panel = panel.replace("{{channels}}", makeChannelList(c));
            panel = panel.replace("{{roles}}", makeRoleList(c));
            panel = panel.replace("{{magic}}", makeMagicChannelsList(c));


            panel = panel.replace("{{greetChannel}}", makeGreeterChannelList(c));

            if (c.hasAutoRole()) {
                panel = panel.replace("{{autoroleOn}}", "checked");
            } else {
                panel = panel.replace("{{autoroleOn}}", "");
            }

            if (c.getMagicChannels().size() > 0) {
                panel = panel.replace("{{magicChannelOn}}", "checked");
            } else {
                panel = panel.replace("{{magicChannelOn}}", "");
            }

            if (c.hasGreetChannel()) {
                panel = panel.replace("{{greeterOn}}", "checked");
            } else {
                panel = panel.replace("{{greeterOn}}", "");
            }

            if (c.hasMutedRole()) {
                panel = panel.replace("{{mutedOn}}", "checked");
            } else {
                panel = panel.replace("{{mutedOn}}", "");
            }

            panel = panel.replace("{{joinMessage}}", c.getGreetMessage());
            panel = panel.replace("{{leaveMessage}}", c.getLeaveMessage());
            return panel;
        }
        // }

        panel = "401 unauthorised";

        return panel;
    }

    public static String makeConfig() {
        return "";
    }

    public static String makeAutoroleList(ServerConfig c) {
        String list = "";
        for (IRole r : c.getGuild().getRoles()) {
            if (c.hasAutoRole()) {
                if (c.getAutoRole().getStringID().equals(r.getStringID())) {
                    list = list + "<option selected value=\"" + r.getStringID() + "\">" + r.getName() + "</option>";
                } else {
                    list = list + "<option value=\"" + r.getStringID() + "\">" + r.getName() + "</option>";
                }
            } else {
                list = list + "<option value=\"" + r.getStringID() + "\">" + r.getName() + "</option>";

            }
        }
        return list;
    }

    public static String makeGreeterChannelList(ServerConfig c) {
        String list = "";
        for (IChannel ch : c.getGuild().getChannels()) {
            if (c.hasGreetChannel()) {

                if (c.getGreetChannel().getStringID().equals(ch.getStringID())) {
                    list = list + "<option selected value=\"" + ch.getStringID() + "\"> #" + ch.getName() + "</option>";
                } else {
                    list = list + "<option value=\"" + ch.getStringID() + "\"> #" + ch.getName() + "</option>";

                }
            } else {
                list = list + "<option value=\"" + ch.getStringID() + "\"> #" + ch.getName() + "</option>";

            }
        }
        return list;
    }

    public static String makeMutedRoleList(ServerConfig c) {
        String list = "";
        for (IRole r : c.getGuild().getRoles()) {
            if (c.hasMutedRole()) {

                if (c.getMutedRole().getStringID().equals(r.getStringID())) {
                    list = list + "<option selected value=\"" + r.getStringID() + "\">" + r.getName() + "</option>";
                } else {
                    list = list + "<option value=\"" + r.getStringID() + "\">" + r.getName() + "</option>";

                }
            } else {
                list = list + "<option value=\"" + r.getStringID() + "\">" + r.getName() + "</option>";

            }

        }
        return list;
    }

    public static String makeMagicChannelsList(ServerConfig c) {
        String s = "";
        for (int i = 0; i < c.getMagicChannels().size(); i++) {
            MagicChannel magicChannel = c.getMagicChannels().get(i);
            String t = "<li>\n" + "<button onclick=\"$(this).parent().remove()\" class=\"minus\">x</button>\n" + "<br>\n" + "<br>\n" + "<div class=\"options\">\n"
                    + "<h5>Voice Channel:</h5>\n" + "<div class=\"custom-select\">\n" + "<select class=\"magicChannel\">\n" + "{{channels}}\n" + "</select>\n" + "  </div>\n"
                    + "  <h5>Role:</h5>\n" + "   <div class=\"custom-select\">\n" + " <select class=\"magicRole\">\n" + "      {{roles}}\n" + "   </select>\n" + " </div>\n"
                    + "   </div>\n" + " </li>";
            t = t.replace("{{channels}}", makeMagicChannelList(c, i));
            t = t.replace("{{roles}}", makeMagicChannelRoleList(c, i));
            s = s + t;
        }
        return s;
    }

    public static String makeMagicChannelRoleList(ServerConfig c, int i) {
        String list = "";
        for (IRole r : c.getGuild().getRoles()) {
            if (c.getMagicChannels().size() > i) {
                if (c.getMagicChannels().get(i).getRole().getStringID().equals(r.getStringID())) {
                    list = list + "<option selected value=\"" + r.getStringID() + "\">" + r.getName() + "</option>";

                } else {
                    list = list + "<option value=\"" + r.getStringID() + "\">" + r.getName() + "</option>";

                }
            } else {
                list = list + "<option value=\"" + r.getStringID() + "\">" + r.getName() + "</option>";

            }

        }
        return list;
    }

    public static String makeMagicChannelList(ServerConfig c, int i) {
        String list = "";
        for (IVoiceChannel ch : c.getGuild().getVoiceChannels()) {
            if (c.getMagicChannels().size() > i) {
                if (c.getMagicChannels().get(i).getVoiceChannel().getStringID().equals(ch.getStringID())) {
                    list = list + "<option selected value=\"" + ch.getStringID() + "\"> " + ch.getName() + "</option>";
                } else {
                    list = list + "<option value=\"" + ch.getStringID() + "\"> " + ch.getName() + "</option>";

                }
            } else {
                list = list + "<option value=\"" + ch.getStringID() + "\"> " + ch.getName() + "</option>";

            }
        }
        return list;
    }

    public static String makeChannelList(ServerConfig c) {
        String list = "";
        for (IVoiceChannel ch : c.getGuild().getVoiceChannels()) {
            list = list + "<option value=\"" + ch.getStringID() + "\"> " + ch.getName() + "</option>";
        }
        return list;
    }

    public static String makeRoleList(ServerConfig c) {
        String list = "";
        for (IRole r : c.getGuild().getRoles()) {
            list = list + "<option value=\"" + r.getStringID() + "\">" + r.getName() + "</option>";
        }
        return list;
    }

}
