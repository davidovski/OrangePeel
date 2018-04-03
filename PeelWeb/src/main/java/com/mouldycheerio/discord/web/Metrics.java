package com.mouldycheerio.discord.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class Metrics {
    public static String getMetricsPage(IDiscordClient client) {
        String metricsHTML;
        metricsHTML = PeelUtil.readFile("assets/metrics.html");
        String metricsFile;
        metricsFile = PeelUtil.readFile("metrics/commands.all");
        List<String> metrics = Arrays.asList(metricsFile.split("\n"));

        String commandsList = "NONE";
        try {
            commandsList = makeList(metrics, 2, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String usersList = "NONE";
        try {
            usersList = makeListUsers(metrics, 1, false, 20, client);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String serverslist = "NONE";
        try {
            serverslist = makeListServers(metrics, true, client);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String times = getTimes(metrics);
        LinkedHashMap<String, Integer> commandslist = makeListCommands(metrics);

        metricsHTML = metricsHTML.replace("{{commands.list}}", commandsList);
        metricsHTML = metricsHTML.replace("{{guildcount}}", client.getGuilds().size()+ "");
        metricsHTML = metricsHTML.replace("{{usercount}}", client.getUsers().size()+ "");
        metricsHTML = metricsHTML.replace("{{guildaverageusercount}}", (client.getUsers().size() / client.getGuilds().size())+ "");

        metricsHTML = metricsHTML.replace("{{commandValues}}", getValues(commandslist, 25));
        metricsHTML = metricsHTML.replace("{{commandKeys}}", getKeys(commandslist, 25, 8));

        metricsHTML = metricsHTML.replace("{{times}}", times);
        metricsHTML = metricsHTML.replace("{{days}}", getWeekDays(metrics));
        metricsHTML = metricsHTML.replace("{{pingData}}", getPingData());
        metricsHTML = metricsHTML.replace("{{guildData}}", getGuildCountData());
        metricsHTML = metricsHTML.replace("{{avgData}}", getAvgUsercountData());

        metricsHTML = metricsHTML.replace("{{servers}}", serverslist);

        metricsHTML = metricsHTML.replace("{{users}}", usersList);

        return metricsHTML;
    }

    private static String makeListUsers(List<String> metrics, int index, boolean stacking, int limit, IDiscordClient client) {
        ArrayList<String> commands = new ArrayList<String>();
        for (String s : metrics) {
            String[] split = s.split("\\|");
            if (split.length > index)
                commands.add(split[index]);
        }

        final Map<String, Integer> mostCommon = PeelUtil.mostCommon(commands);

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
                    commandsList = commandsList + "<li> " + PeelUtil.repeat("|", entry.getValue()) + " <b>" + name + "</b></li>";
                } else {
                    commandsList = commandsList + "<li><b>" + name + "</b> (" + entry.getValue() + ")</li>";

                }
            } else {
                return commandsList;
            }
        }
        return commandsList;
    }

    private static String makeListServers(List<String> metrics, boolean stacking, IDiscordClient client) {
        ArrayList<String> commands = new ArrayList<String>();
        for (String s : metrics) {
            String[] split = s.split("\\|");
            if (split.length > 3) {
                commands.add(split[3]);
            }
        }

        final Map<String, Integer> mostCommon = PeelUtil.mostCommon(commands);

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
                commandsList = commandsList + "<li> " + PeelUtil.repeat("|", entry.getValue()) + " <b>" + name + "</b></li>";
            } else {
                commandsList = commandsList + "<li><b>" + name + "</b> (" + entry.getValue() + ")</li>";

            }
        }
        return commandsList;
    }

    private static String makeList(List<String> metrics, int index, boolean stacking) {
        ArrayList<String> commands = new ArrayList<String>();
        for (String s : metrics) {
            String[] split = s.split("\\|");
            if (split.length > index)
                commands.add(split[index]);
        }

        final Map<String, Integer> mostCommon = PeelUtil.mostCommon(commands);

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
                commandsList = commandsList + "<li> " + PeelUtil.repeat("|", entry.getValue()) + " <b>" + entry.getKey() + "</b></li>";
            } else {
                commandsList = commandsList + "<li><b>" + entry.getKey() + "</b> (" + entry.getValue() + ")</li>";

            }
        }
        return commandsList;
    }

    private static String getValues(LinkedHashMap<String, Integer> map, int limit) {
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

    private static String getKeys(LinkedHashMap<String, Integer> map, int limit, int crop) {
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

    private static LinkedHashMap<String, Integer> makeListCommands(List<String> metrics) {
        ArrayList<String> commands = new ArrayList<String>();
        for (String s : metrics) {
            String[] split = s.split("\\|");
            commands.add(split[2]);
        }

        final Map<String, Integer> mostCommon = PeelUtil.mostCommon(commands);

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

    private static String getTimes(List<String> metrics) {
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

        Map<Integer, Integer> mostCommon = PeelUtil.mostCommon(hours);
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

    private static String getPingData() {
        String t = "";
        String f = PeelUtil.readFile("metrics/ping");
        for (String line : f.split("\n")) {

            String[] split = line.split("\\|");
            String time = split[0];
            String ping = split[1];

            String obj = "{x: new Date("+time+"), y: "+ping+"},";

            t = t + obj;
        }

        return t ;
    }

    private static String getGuildCountData() {
        String t = "";
        String f = PeelUtil.readFile("metrics/guilds");
        for (String line : f.split("\n")) {

            String[] split = line.split("\\|");
            String time = split[0];
            String ping = split[1];

            String obj = "{x: new Date("+time+"), y: "+ping+"},";

            t = t + obj;
        }

        return t ;
    }

    private static String getAvgUsercountData() {
        String t = "";
        String f = PeelUtil.readFile("metrics/guilds");
        for (String line : f.split("\n")) {

            String[] split = line.split("\\|");
            String time = split[0];
            String a = split[1];
            String b = split[2];


            String obj = "{x: new Date("+time+"), y: "+( Integer.parseInt(b) / Integer.parseInt(a))+"},";

            t = t + obj;
        }

        return t ;
    }

    private static String getWeekDays(List<String> metrics) {
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

        Map<Integer, Integer> mostCommon = PeelUtil.mostCommon(hours);
        String t = "[";

        for (int i = 1; i < 7 + 1; i++) {
            if (mostCommon.containsKey(i)) {
                t = t + mostCommon.get(i) + ",";
            } else {
                t = t + "0, ";
            }
        }
        return t + "]";
    }
}
