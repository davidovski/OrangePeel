package com.mouldycheerio.discord.web;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class ClientHandler extends Thread {
    private Socket socket;
    private IDiscordClient client;

    public ClientHandler(Socket s, IDiscordClient idc) {
        socket = s;
        this.client = idc;
        start();
    }

    @Override
    public void run() {
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream out = new PrintStream(new BufferedOutputStream(socket.getOutputStream()));

            String s = in.readLine();

            StringTokenizer st = new StringTokenizer(s);

            String request = "";
            if (st.hasMoreElements() && st.nextToken().equalsIgnoreCase("GET") && st.hasMoreElements()) {
                request = st.nextToken();
            } else {
                throw new FileNotFoundException();
            }

            if (request.endsWith("favicon.ico")) {
                String mimeType = "image/png";
                out.print("HTTP/1.0 200 OK\r\n" + "Content-type: " + mimeType + "\r\n\r\n");

                byte[] bytes = Files.readAllBytes(Paths.get("assets/orangePeel.png"));
                out.write(bytes);
                out.close();

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

            String file = respond(request);

            String nav = readFile("assets/navbar.html");
            file = file.replace("{{header}}", nav);

            String foot = readFile("assets/footer.html");
            file = file.replace("{{footer}}", foot);

            file = file.replace("{{head}}", readFile("assets/head.html"));
            file = file.replace("{{guilds}}", client.getGuilds().size() + "");
            file = file.replace("{{users}}", client.getUsers().size() + "");
            file = file.replace("{{invite}}", "https://discord.gg/XZB8kSx");
            file = file.replace("{{commands}}", "53");
            file = file.replace("{{guilds.table}}", getGuilds());

            String mimeType = "text/html";

            if (file.startsWith("#")) {
                mimeType = file.split("#")[1];
                file = file.substring(mimeType.length() + 2);
            }

            out.print("HTTP/1.0 200 OK\r\n" + "Content-type: " + mimeType + "\r\n\r\n");
            out.write(file.getBytes());

            out.close();
        } catch (IOException x) {
            System.out.println(x);
        }
    }

    private String getGuilds() {
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
                html = html + "<tr><td> <img class=\"icon\"src=" + iconURL + "></td><td>" + g.getUsers().size() + "</td><td>" + g.getName() + "</td> <td>" + g.getOwner().getName()
                        + "</td> </tr>";
            }
        }
        html = html + "</table>";
        return html;
    }

    public String respond(String request) {
        if ("/guilds".equals(request)) {
            return "<h1>I AM IN " + client.getGuilds().size() + " GUILDS;";
        }

        if ("/style.css".equals(request)) {
            return "#text/css#" + readFile("assets/style.css");
        }

        if ("/barGraph.js".equals(request)) {
            return "#text/js#" + readFile("assets/barGraph.js");
        }

        if ("/metrics".equals(request)) {
            String metricsHTML = readFile("assets/metrics.html");
            String metricsFile = readFile("../metrics/commands.all");
            List<String> metrics = Arrays.asList(metricsFile.split("\n"));

            String commandsList = makeList(metrics, 2, true);
            String usersList = makeListUsers(metrics, 1, false, 20);
            String serverslist = makeListServers(metrics, true);

            String times = getTimes(metrics);
            LinkedHashMap<String, Integer> commandslist = makeListCommands(metrics);


            metricsHTML = metricsHTML.replace("{{commands.list}}", commandsList);
            metricsHTML = metricsHTML.replace("{{commandValues}}", getValues(commandslist, 30));
            metricsHTML = metricsHTML.replace("{{commandKeys}}", getKeys(commandslist, 30, 8));


            metricsHTML = metricsHTML.replace("{{times}}", times);
            metricsHTML = metricsHTML.replace("{{days}}", getWeekDays(metrics));

            metricsHTML = metricsHTML.replace("{{servers}}", serverslist);

            metricsHTML = metricsHTML.replace("{{users}}", usersList);

            return metricsHTML;
        }

        // if ("/".equals(request)) {
        // return readFile("assets/main.html");
        // }

        String f = "assets/" + request;
        if (f.endsWith("/")) {
            f = f + "index.html";
        }
        if (!f.contains(".")) {
            f = f + ".html";
        }
        return readFile(f);
    }

    private String makeListUsers(List<String> metrics, int index, boolean stacking, int limit) {
        ArrayList<String> commands = new ArrayList<String>();
        for (String s : metrics) {
            String[] split = s.split("\\|");
            commands.add(split[index]);
        }

        final Map<String, Integer> mostCommon = mostCommon(commands);

        List<String> keys = new LinkedList<String>(mostCommon.keySet());

        Collections.sort(keys, new Comparator<String>() {

            public int compare(String o1, String o2) {
                return mostCommon.get(o2) - mostCommon.get(o1);
            }
        });

        LinkedHashMap<String, Integer> mostCommonSorted = new LinkedHashMap<String, Integer>();
        for (String key : keys) {
            mostCommonSorted.put(key, mostCommon.get(key));
        }

        String commandsList = "";

        int i = 0;
        for (Entry<String, Integer> entry : mostCommonSorted.entrySet()) {
            String name = entry.getKey();

            for (IUser u : client.getUsers()) {
                if (u.getLongID() == Long.parseLong(entry.getKey())) {
                    name = u.getName() + "#" + u.getDiscriminator();
                }
            }
            i++;
            if (i <= limit) {
                if (stacking) {
                    commandsList = commandsList + "<li> " + repeat("|", entry.getValue()) + " <b>" + name + "</b></li>";
                } else {
                    commandsList = commandsList + "<li><b>" + name + "</b> (" + entry.getValue() + ")</li>";

                }
            } else {
                return commandsList;
            }
        }
        return commandsList;
    }

    private String makeListServers(List<String> metrics, boolean stacking) {
        ArrayList<String> commands = new ArrayList<String>();
        for (String s : metrics) {
            String[] split = s.split("\\|");
            if (split.length > 3) {
                commands.add(split[3]);
            }
        }

        final Map<String, Integer> mostCommon = mostCommon(commands);

        List<String> keys = new LinkedList<String>(mostCommon.keySet());

        Collections.sort(keys, new Comparator<String>() {

            public int compare(String o1, String o2) {
                return mostCommon.get(o2) - mostCommon.get(o1);
            }
        });

        LinkedHashMap<String, Integer> mostCommonSorted = new LinkedHashMap<String, Integer>();
        for (String key : keys) {
            mostCommonSorted.put(key, mostCommon.get(key));
        }

        String commandsList = "";

        int i = 0;
        for (Entry<String, Integer> entry : mostCommonSorted.entrySet()) {
            String name = entry.getKey();

            for (IGuild u : client.getGuilds()) {
                if (u.getLongID() == Long.parseLong(entry.getKey())) {
                    name = u.getName();
                }
            }
            i++;
            if (stacking) {
                commandsList = commandsList + "<li> " + repeat("|", entry.getValue()) + " <b>" + name + "</b></li>";
            } else {
                commandsList = commandsList + "<li><b>" + name + "</b> (" + entry.getValue() + ")</li>";

            }
        }
        return commandsList;
    }

    private String makeList(List<String> metrics, int index, boolean stacking) {
        ArrayList<String> commands = new ArrayList<String>();
        for (String s : metrics) {
            String[] split = s.split("\\|");
            commands.add(split[index]);
        }

        final Map<String, Integer> mostCommon = mostCommon(commands);

        List<String> keys = new LinkedList<String>(mostCommon.keySet());

        Collections.sort(keys, new Comparator<String>() {

            public int compare(String o1, String o2) {
                return mostCommon.get(o2) - mostCommon.get(o1);
            }
        });

        LinkedHashMap<String, Integer> mostCommonSorted = new LinkedHashMap<String, Integer>();
        for (String key : keys) {
            mostCommonSorted.put(key, mostCommon.get(key));
        }

        String commandsList = "";

        int i = 0;
        for (Entry<String, Integer> entry : mostCommonSorted.entrySet()) {
            i++;
            if (stacking) {
                commandsList = commandsList + "<li> " + repeat("|", entry.getValue()) + " <b>" + entry.getKey() + "</b></li>";
            } else {
                commandsList = commandsList + "<li><b>" + entry.getKey() + "</b> (" + entry.getValue() + ")</li>";

            }
        }
        return commandsList;
    }

    private String getValues(LinkedHashMap<String, Integer> map, int limit) {
        String t = "[";
        int i = 0;
        for (Integer string : map.values()) {
            if (i <= limit) {

            t = t + "\"" + string + "\"" + ",";
            }
            i++;
        }
        return t + "]";
    }

    private String getKeys(LinkedHashMap<String, Integer> map, int limit, int crop) {
        String t = "[";
        int i = 0;
        for (String string : map.keySet()) {
            if (i <= limit) {
                if (crop != -1 && string.length() > crop) {
                    string = string.substring(0, crop) + "-";
                }

            t = t + "\"" + string + "\"" + ",";
            }
            i++;
        }
        return t + "]";
    }

    private LinkedHashMap<String, Integer> makeListCommands(List<String> metrics) {
        ArrayList<String> commands = new ArrayList<String>();
        for (String s : metrics) {
            String[] split = s.split("\\|");
            commands.add(split[2]);
        }

        final Map<String, Integer> mostCommon = mostCommon(commands);

        List<String> keys = new LinkedList<String>(mostCommon.keySet());

        Collections.sort(keys, new Comparator<String>() {

            public int compare(String o1, String o2) {
                return mostCommon.get(o2) - mostCommon.get(o1);
            }
        });

        LinkedHashMap<String, Integer> mostCommonSorted = new LinkedHashMap<String, Integer>();
        for (String key : keys) {
            mostCommonSorted.put(key, mostCommon.get(key));
        }

        return mostCommonSorted;
    }

    private String getTimes(List<String> metrics) {
        ArrayList<Long> times = new ArrayList<Long>();
        for (String s : metrics) {
            String[] split = s.split("\\|");
            times.add(Long.parseLong(split[0]));
        }

        LinkedList<Integer> hours = new LinkedList<Integer>();
        for (Long time : times) {
            DateFormat dateFormat = new SimpleDateFormat("HH");
            Date date = new Date(time);
            hours.add(Integer.parseInt(dateFormat.format(date)));
        }

        Map<Integer, Integer> mostCommon = mostCommon(hours);
        String t = "[";

        for (int i = 0; i < 24; i++) {
            if (mostCommon.containsKey(i)) {
                t = t + mostCommon.get(i) + ",";
            } else {
                t = t + "0, ";
            }
        }
        return t + "]";
    }

    private String getWeekDays(List<String> metrics) {
        ArrayList<Long> times = new ArrayList<Long>();
        for (String s : metrics) {
            String[] split = s.split("\\|");
            times.add(Long.parseLong(split[0]));
        }

        LinkedList<Integer> hours = new LinkedList<Integer>();
        for (Long time : times) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(time));
            hours.add(calendar.get(Calendar.DAY_OF_WEEK));
        }

        Map<Integer, Integer> mostCommon = mostCommon(hours);
        String t = "[";

        for (int i = 0; i < 7; i++) {
            if (mostCommon.containsKey(i)) {
                t = t + mostCommon.get(i) + ",";
            } else {
                t = t + "0, ";
            }
        }
        return t + "]";
    }

    public static String repeat(String s, int times) {
        String r = "";
        for (int i = 0; i < times; i++) {
            r = r + s;
        }
        return r;
    }

    public static <T> Map<T, Integer> mostCommon(List<T> list) {
        Map<T, Integer> map = new HashMap<T, Integer>();

        for (T t : list) {
            Integer val = map.get(t);
            map.put(t, val == null ? 1 : val + 1);
        }

        Entry<T, Integer> max = null;

        for (Entry<T, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }

        return map;
    }

    public String readFile(String name) {

        InputStream is;
        try {
            is = new FileInputStream(name);

            BufferedReader buf = new BufferedReader(new InputStreamReader(is));

            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();

            while (line != null) {
                sb.append(line).append("\n");
                line = buf.readLine();
            }

            String fileAsString = sb.toString();
            return fileAsString;
        } catch (Exception e) {
            e.printStackTrace();
            return "404";
        }
    }
}
