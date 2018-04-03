package com.mouldycheerio.discord.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mrpowergamerbr.temmiediscordauth.TemmieDiscordAuth;
import com.mrpowergamerbr.temmiediscordauth.responses.CurrentUserResponse;
import com.mrpowergamerbr.temmiediscordauth.utils.TemmieGuild;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class ControlPanel {

    public static String cacheID;
    public static ArrayList<String> servercache;

    public static String getPanelPage(IDiscordClient client, String ckie, WebServer server) {
        String panel;
        panel = PeelUtil.readFile("assets/panel.html");

        if (server.getSession(ckie) != null) {
            TemmieDiscordAuth session = server.getSession(ckie);
            CurrentUserResponse u = session.getCurrentUserIdentification();
            panel = panel.replace("{{user}}", u.getUsername());

            // panel = panel.replace("{{mutual}}", getGuilds(client, server, ckie, false));
             panel = panel.replace("{{guilds}}", makeServerBubbles(client, u.getId()));

            String metricsFile = PeelUtil.readFile("metrics/commands.all");
            List<String> metrics = Arrays.asList(metricsFile.split("\n"));
            int commands = getCommandNumber(metrics, client, u.getId());
            int sharedGuilds = getGuildsNumber(client, server, ckie);
            String favouriteCommand = getFavouriteCommand(metrics, client, u.getId());
            String stats = "<ul>";
            stats = stats + "<li><b>Commands Executed: </b>" + commands + "</li>";
            stats = stats + "<li><b>Guilds Shared: </b>" + sharedGuilds + "</li>";
            stats = stats + "<li><b>Most Used Command: </b>>" + favouriteCommand + "</li>";
            stats = stats + "</ul>";
            panel = panel.replace("{{stats}}", stats);

            return panel;
        }
        panel = panel.replace("{{guilds}}", "You must login to view this content!");
        panel = panel.replace("{{stats}}", "You must login to view this content!");
        panel = panel.replace("{{mutual}}", "You must login to view this content!");
        panel = panel.replace("{{user}}", "n/a");

        panel = "<head><meta http-equiv=\"refresh\" content=\"0; url=https://bot.mouldycheerio.com\" /></head>";

        return panel;
    }

    public static String makeConfig() {
        return "";
    }

    public static ArrayList<String> getServers(WebServer server, String ckie) {

        ArrayList<String> guilds = new ArrayList<String>();
        try {
            TemmieDiscordAuth temmie = server.getSession(ckie);
            for (TemmieGuild temmieGuild : temmie.getUserGuilds()) {
                guilds.add(temmieGuild.getId());
            }
        } catch (NullPointerException e) {

        }
        cacheID = ckie;
        servercache = guilds;

        return guilds;
    }

    private static String getGuilds(IDiscordClient client, WebServer server, String ckie, boolean adminonly) {
        String html = "<table class=\"table\">";
        if (adminonly) {

            html = html + "<tr><th>Icon</th><th>Users</th> ><th>Name</th> <th>Rank</th></tr>";
        } else {
            html = html + "<tr><th>Icon</th><th>Name</th> <th>Rank</th> <th>Admin</th> </tr>";

        }
        List<IGuild> guilds = new LinkedList<IGuild>();
        for (String string : getServers(server, ckie)) {
            for (IGuild g : client.getGuilds()) {
                if (g.getStringID().equals(string)) {
                    guilds.add(g);
                }
            }
        }

        TemmieDiscordAuth session = server.getSession(ckie);
        System.out.println("length=" + guilds.size());

        Collections.sort(guilds, new Comparator<IGuild>() {
            public int compare(IGuild o1, IGuild o2) {
                return o2.getUsers().size() - o1.getUsers().size();
            }
        });

        for (IGuild g : guilds) {
            String iconURL = g.getIconURL();
            if (iconURL.contains("null")) {
                iconURL = "https://cdn.discordapp.com/icons/313763491259351050/74655abead6eadc626a4b17e6a076054.jpg";
            }
            try {
                String rank = "";

                String admin = "";

                String id = session.getCurrentUserIdentification().getId();
                IUser usr = g.getUserByID(Long.parseLong(id));
                if (usr != null) {
                    IRole highest = null;
                    for (IRole iRole : g.getRolesForUser(usr)) {
                        if (highest != null) {
                            if (iRole.getPosition() > highest.getPosition()) {
                                highest = iRole;
                            }
                        } else {
                            highest = iRole;
                        }
                    }
                    if (highest != null) {
                        rank = highest.getName();
                    } else {
                        rank = "null";
                    }

                    if (usr.getPermissionsForGuild(g).contains(Permissions.ADMINISTRATOR)) {
                        admin = "yes";
                    } else {
                        admin = "no";
                    }

                }
                if (adminonly) {
                    if (admin.equals("yes")) {
                        html = html + "<tr><td> <img class=\"icon\"src=" + iconURL + "></td><td>" + g.getUsers().size() + "</td> <td>" + g.getName() + "</td> <td>" + rank
                                + "</td> </tr>";
                    }

                } else {
                    html = html + "<tr><td> <img class=\"icon\"src=" + iconURL + "></td><td>" + g.getName() + "</td> <td>" + rank + "</td> <td>" + admin + "</td> </tr>";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        html = html + "</table>";
        return html;
    }

    private static int getGuildsNumber(IDiscordClient client, WebServer server, String ckie) {
        String html = "<table class=\"table\">";
        html = html + "<tr><th>Icon</th><th>Name</th> <th>Rank</th> <th>Admin</th> </tr>";
        List<IGuild> guilds = new LinkedList<IGuild>();
        for (String string : getServers(server, ckie)) {
            for (IGuild g : client.getGuilds()) {
                if (g.getStringID().equals(string)) {
                    guilds.add(g);
                }
            }
        }
        return guilds.size();
    }

    private static int getCommandNumber(List<String> metrics, IDiscordClient client, String id) {
        ArrayList<String> commands = new ArrayList<String>();
        for (String s : metrics) {
            String[] split = s.split("\\|");
            if (split.length > 1)
                commands.add(split[1]);
        }

        int i = 0;
        for (String string : commands) {
            if (string.equals(id)) {
                i++;
            }
        }
        return i;
    }

    private static String getFavouriteCommand(List<String> metrics, IDiscordClient client, String id) {
        ArrayList<String> commands = new ArrayList<String>();
        for (String s : metrics) {
            String[] split = s.split("\\|");
            if (split.length > 1) {
                if (split[1].equals(id)) {
                    commands.add(s);
                }
            }
        }

        List<String> cmds = new ArrayList<String>();
        for (String string : commands) {
            cmds.add(string.split("\\|")[2]);
        }

        Map<String, Integer> mostCommon = PeelUtil.mostCommon(cmds);
        for (Entry<String, Integer> entry : mostCommon.entrySet()) {
            return entry.getKey();
        }
        return "";
    }

    public static String makeServerBubbles(IDiscordClient client, String id) {
        String html = "";

        for (IGuild g : client.getGuilds()) {

            for (IUser u : g.getUsers()) {
                if (u.getStringID().equals(id)) {

                    if (u.getPermissionsForGuild(g).contains(Permissions.ADMINISTRATOR)) {
                        String iconURL = g.getIconURL();
                        if (iconURL == null) {
                            iconURL = "https://cdn.discordapp.com/icons/313763491259351050/74655abead6eadc626a4b17e6a076054.jpg";
                        }

                        html = html + "<a class=\"serverbutton\" href=\"config?" + g.getStringID() + "\"><img class=\"servericon\" src=\""+ iconURL + "\"></a>";

                    }
                    break;
                }
            }
        }

        return html;
    }
}
