package com.mouldycheerio.discord.web;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import com.mrpowergamerbr.temmiediscordauth.TemmieDiscordAuth;
import com.mrpowergamerbr.temmiediscordauth.utils.TemmieGuild;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class ClientHandler extends Thread {
    private Socket socket;
    private IDiscordClient client;
    private WebServer server;

    public ClientHandler(Socket s, IDiscordClient idc, WebServer server) {
        socket = s;
        this.client = idc;
        this.server = server;
        start();

    }

    @Override
    public void run() {
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream out = new PrintStream(new BufferedOutputStream(socket.getOutputStream()));

            String s = in.readLine();

            StringTokenizer st = new StringTokenizer(s);

            String ckie = "";
            boolean sendNew = true;

            String input = "";

            String line;
            line = in.readLine();
            input = input + "line";
            boolean isPost = line.startsWith("POST");
            int contentLength = 0;

            while (!(line = in.readLine()).equals("")) {
                input = input + "\n" + line;

                final String contentHeader = "Content-Length: ";
                if (line.startsWith(contentHeader)) {
                    contentLength = Integer.parseInt(line.substring(contentHeader.length()));

                }

                if (line.startsWith("Cookie: ")) {
                    String cookiesString = line.substring(8);
                    String[] cookies = cookiesString.split("; ");
                    for (String string : cookies) {
                        if (string.startsWith("OPID=")) {
                            String value = string.substring(5);
                            ckie = value;
                            sendNew = false;
                        }
                    }
                }
            }

            String data = "";
            int c = 0;

            for (int i = 0; i < contentLength; i++) {
                c = in.read();
                data = data + (char) c;
            }

            if (sendNew) {
                ckie = PeelUtil.getRandomToken();
            }

            String request = "";
            // System.out.println(input);
            String t = st.nextToken();
            if (st.hasMoreElements() && (t.equalsIgnoreCase("GET") || t.equalsIgnoreCase("POST")) && st.hasMoreElements()) {
                request = st.nextToken();
            } else {
                // throw new FileNotFoundException();
            }

            if (request.startsWith("/config/save")) {

                try {
                    String id = "";
                    String[] args = request.split("\\?");
                    id = args[1];
                    String re = "";

                    if (contentLength > 0) {
                        String[] values = URLDecoder.decode(data, "UTF-8").split("&");

                        TemmieDiscordAuth session = server.getSession(ckie);
                        boolean authorised = false;
                        if (session != null) {
                            for (IGuild g : client.getGuilds()) {
                                if (g.getStringID().equals(id)) {
                                    IUser u = g.getUserByID(Long.parseLong(session.getCurrentUserIdentification().getId()));
                                    if (u.getPermissionsForGuild(g).contains(Permissions.ADMINISTRATOR)) {
                                        authorised = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (authorised) {
                            ServerConfig config = new ServerConfig(id, client);
                            for (String v : values) {
                                if (v.startsWith("autorole=")) {
                                    config.setAutoRoleID("" + v.substring(9));
                                }
                                if (v.startsWith("muted=")) {
                                    config.setMutedRoleID("" + v.substring(6));
                                }
                                if (v.startsWith("greet=")) {
                                    config.setGreetChannelID("" + v.substring(6));
                                }
                                if (v.startsWith("join=")) {
                                    config.setGreetMessage("" + v.substring(5));
                                }
                                if (v.startsWith("leave=")) {
                                    config.setLeaveMessage("" + v.substring(6));
                                }
                                if (v.startsWith("mc=")) {
                                    config.getMagicChannels().clear();
                                    System.out.println(v);
                                    v = v.substring(3);
                                    if (v.length() > 0) {
                                        String[] list = v.split("and");
                                        for (int i = 0; i < list.length; i++) {
                                            if (list[i] != "") {
                                                String[] split = list[i].split("for");

                                                String channel = split[0];
                                                String role = split[1];
                                                System.out.println(channel + "for" + role);

                                                MagicChannel e = new MagicChannel(client, channel, role);
                                                System.out.println(e.getVoiceChannel().getStringID() + "for" + e.getRole().getStringID());
                                                System.out.println("---");

                                                config.getMagicChannels().add(e);
                                            }
                                        }
                                    }

                                }
                            }

                            config.save();
                            System.out.println("saved");
                            re = "Changes Saved";
                        } else {
                            re = "Error, you are not logged in!";

                        }

                    }

                    out.print("HTTP/1.0 200 OK\r\n" + "Content-type: " + "text/html" + "\r\n\r\n");
                    out.write(re.getBytes());

                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return;
            }

            if (request.startsWith("/auth")) {
                String code = request.split("=")[1];
                System.out.println(code);
                TemmieDiscordAuth temmie;
                if (WebServer.TEST) {
                    temmie = new TemmieDiscordAuth(code, "http://mc.mouldycheerio.com:8213/auth", "370240387455123457", "jaLok-rslhZr9SStroMqHCpollEi70_q");
                } else {
                    temmie = new TemmieDiscordAuth(code, "https://bot.mouldycheerio.com/auth", "370240387455123457", "jaLok-rslhZr9SStroMqHCpollEi70_q");
                }
                temmie.doTokenExchange();
                server.getSessions().put(ckie, temmie);

                // String re = "<meta http-equiv=\"refresh\" content=\"0; url=\"http://bot.mouldycheerio.com\" />";
                String re = "";
                re = PeelUtil.readFile("assets/panel_redirect.html");

                out.print("HTTP/1.0 200 OK\r\n" + "Content-type: " + "text/html" + "\r\n\r\n");
                out.write(re.getBytes());

                out.close();
                return;
            }

            if (request.startsWith("/logout")) {

                server.getSessions().remove(ckie);
                // String re = "<head><meta http-equiv=\"refresh\" content=\"0; url=http://mc.mouldycheerio.com:8213\" /></head>";

                String re = "<head><meta http-equiv=\"refresh\" content=\"0; url=https://bot.mouldycheerio.com\" /></head>";

                out.print("HTTP/1.0 200 OK\r\n" + "Content-type: " + "text/html" + "\r\n\r\n");
                out.write(re.getBytes());

                out.close();
                return;
            }

            // if (request.equals("/me")) {
            // String re = "";
            // try {
            // TemmieDiscordAuth temmie = server.getSession(socket.getInetAddress().toString().split(":")[0]);
            // for (TemmieGuild temmieGuild : temmie.getUserGuilds()) {
            // re = re + temmieGuild.getName() + "";
            // }
            // } catch (NullPointerException e) {
            // re = "401 unauthorised";
            // }
            // out.print("HTTP/1.0 200 OK\r\n" + "Content-type: " + "text/html" + "\r\n\r\n");
            // out.write(re.getBytes());
            //
            // out.close();
            // return;
            // }

            if (request.endsWith("favicon.ico")) {
                String mimeType = "image/png";
                out.print("HTTP/1.0 200 OK\r\n" + "Content-type: " + mimeType + "\r\n\r\n");

                byte[] bytes = Files.readAllBytes(Paths.get("assets/orangePeel.png"));
                out.write(bytes);
                out.close();

                return;
            }

            if (request.equals("/api/jackiechan")) {
                String mimeType = "image/png";
                out.print("HTTP/1.0 200 OK\r\n" + "Content-type: " + mimeType + "\r\n\r\n");
                File folder = new File("assets/jackiechan");
                if (folder.exists() && folder.isDirectory()) {
                    File[] files = folder.listFiles();
                    ArrayList<File> filelist = new ArrayList<File>();
                    for (File f : files) {
                        if (f.getName().startsWith("0")) {
                            filelist.add(f);
                        }
                    }
                    Random random = new Random();
                    File photo = filelist.get(random.nextInt(filelist.size()));
                    System.out.println(photo.getName());

                    byte[] bytes = Files.readAllBytes(Paths.get("assets/jackiechan/" + photo.getName()));
                    out.write(bytes);
                    out.close();
                }

                return;
            }

            if (request.equals("/api/peeled.jpg") || request.equals("/api/peeled")) {

                File folder = new File("assets/peeled");
                if (folder.exists() && folder.isDirectory()) {
                    File[] files = folder.listFiles();
                    ArrayList<File> filelist = new ArrayList<File>();
                    for (File f : files) {
                        if (f.getName().startsWith("0")) {
                            filelist.add(f);
                        }
                    }
                    Random random = new Random();
                    File photo = filelist.get(random.nextInt(filelist.size()));
                    System.out.println(photo.getName());

                    String mimeType = "image/png";
                    if (photo.getName().endsWith(".jpg")) {
                        mimeType = "image/jpeg";
                    }
                    out.print("HTTP/1.0 200 OK\r\n" + "Content-type: " + mimeType + "\r\n\r\n");
                    byte[] bytes = Files.readAllBytes(Paths.get("assets/peeled/" + photo.getName()));
                    out.write(bytes);
                    out.close();
                }

                return;
            }

            if (request.endsWith(".png")) {
                String mimeType = "image/png";
                out.print("HTTP/1.0 200 OK\r\n" + "Content-type: " + mimeType + "\r\n\r\n");

                byte[] bytes = Files.readAllBytes(Paths.get("assets/" + request));
                out.write(bytes);
                out.close();

                return;
            }

            String file = respond(request, ckie);

            String nav = PeelUtil.readFile("assets/navbar.html");
            file = file.replace("{{header}}", nav);

            TemmieDiscordAuth temmie = server.getSession(ckie);
            if (temmie != null) {
                String usr = PeelUtil.readFile("assets/loggedin.html");
                file = file.replace("{{usr}}", usr);
            } else {
                String usr = PeelUtil.readFile("assets/loggedout.html");
                file = file.replace("{{usr}}", usr);
            }

            String foot = PeelUtil.readFile("assets/footer.html");
            file = file.replace("{{footer}}", foot);

            file = file.replace("{{head}}", PeelUtil.readFile("assets/head.html"));
            file = file.replace("{{guilds}}", client.getGuilds().size() + "");
            file = file.replace("{{users}}", client.getUsers().size() + "");
            file = file.replace("{{invite}}", "https://discord.gg/XZB8kSx");
            file = file.replace("{{commands}}", "53");

            try {
                file = file.replace("{{guilds.table}}", getGuilds(client));
            } catch (Exception e) {
                e.printStackTrace();
            }

            String mimeType = "text/html";

            if (file.startsWith("#")) {
                mimeType = file.split("#")[1];
                file = file.substring(mimeType.length() + 2);
            }

            // in.close();

            out.print("HTTP/1.0 200 OK\r\n" + "Content-type: " + mimeType + "\r\n");
            if (sendNew) {
                out.println("Set-Cookie: OPID=" + ckie + ";\r\n");
            }
            out.print("\r\n");
            out.write(file.getBytes());
            out.close();
            socket.close();
        } catch (IOException x) {
            System.out.println(x);
        }

    }

    private static String getGuilds(IDiscordClient client) {
        String html = "<table class=\"table\">";
        html = html + "<tr><th>Icon</th><th>Users</th> <th>Name</th> <th>Owner</th> </tr>";

        List<IGuild> guilds = client.getGuilds();

        Collections.sort(guilds, new Comparator<IGuild>() {
            public int compare(IGuild o1, IGuild o2) {
                return o2.getUsers().size() - o1.getUsers().size();
            }
        });

        for (IGuild g : guilds) {
            if (!(g.getUsers().size() < 6)) {
                String iconURL = g.getIconURL();
                if (iconURL.contains("null")) {
                    iconURL = "https://cdn.discordapp.com/icons/313763491259351050/35216e6d5326c11e6f2cef435dfa08c3.jpg";
                }
                try {
                    html = html + "<tr><td> <img class=\"icon\"src=" + iconURL + "></td><td>" + g.getUsers().size() + "</td><td>" + g.getName() + "</td> <td>"
                            + g.getOwner().getName() + "</td> </tr>";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        html = html + "</table>";
        return html;
    }

    public String respond(String request, String ckie) {
        if ("/guilds".equals(request)) {
            return "<h1>I AM IN " + client.getGuilds().size() + " GUILDS;";
        }

        if ("/style.css".equals(request)) {
            return "#text/css#" + PeelUtil.readFile("assets/style.css");
        }

        if ("/barGraph.js".equals(request)) {
            return "#text/js#" + PeelUtil.readFile("assets/barGraph.js");
        }

        if ("/metrics".equals(request)) {
            return Metrics.getMetricsPage(client);
        }

        if (request.startsWith("/config")) {
            try {
                String id = "";
                String[] args = request.split("\\?");
                id = args[1];

                return ConfigPanel.getPanelPage(client, ckie, server, id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        if ("/me".equals(request) || "/panel".equals(request)) {
            return ControlPanel.getPanelPage(client, ckie, server);
        }

        // if ("/".equals(request)) {
        // return FileUtil.readFile("assets/main.html");
        // }

        String f = "assets/" + request;
        if (f.endsWith("/")) {
            f = f + "index.html";
        }
        if (!f.contains(".")) {
            f = f + ".html";
        }
        return PeelUtil.readFile(f);
    }

    public ArrayList<String> getSharedServers(String ckie) {
        ArrayList<String> guilds = new ArrayList<String>();
        try {
            TemmieDiscordAuth temmie = server.getSession(ckie);
            for (TemmieGuild temmieGuild : temmie.getUserGuilds()) {
                guilds.add(temmieGuild.getId());
            }
        } catch (NullPointerException e) {

        }
        return guilds;
    }

}
