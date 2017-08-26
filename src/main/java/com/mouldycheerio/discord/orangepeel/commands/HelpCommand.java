package com.mouldycheerio.discord.orangepeel.commands;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.util.EmbedBuilder;

public class HelpCommand extends OrangePeelCommand {
    private List<Command> commands;

    public HelpCommand(List<Command> commands) {
        this.commands = commands;
        setName("help");
        setDescription(new CommandDescription("Help", "Display this message", "help"));
        addAlias("commands");
        addAlias("?");
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        String stringID = commandMessage.getAuthor().getStringID();

        if (args.length > 1) {
            for (Command c : commands) {
                if (c.getName().equalsIgnoreCase(args[1])) {
                    String m = "__" + c.getName() + "__\n";
                    m = m + "**Name:** " + c.getDescription().getTitle() + ";\n";
                    m = m + "**Description:** " + c.getDescription().getText() + ";\n";
                    m = m + "**Usage:** `" + c.getDescription().getUsage() + "`;\n";

                    if (c.getAlias().size() > 0) {
                        m = m + "**Aliases:** ";
                        for (String string : c.getAlias()) {
                            m = m + string + ",";
                        }
                    }
                    commandMessage.getChannel().sendMessage(m);
                }
            }
        } else {
            commandMessage.reply("Check your PMs! ;)");

            IPrivateChannel pm = client.getOrCreatePMChannel(commandMessage.getAuthor());



            String message = "";
            try {
                message = message + "```"+getHTTP("http://artii.herokuapp.com/make?text=OrangePeel&font=weird") + "```\n";
            } catch (Exception e) {
                message = "``` Orange Peel```";
            }
            message = message + "**by davidovski**\n\n";
            String message1 = "***__Commands__***\n\n";
            String message2 = "**__admin commands__**\n\n";
            String message3 = "**__custom commands__**\n\n";

            for (Command c : commands) {
                if (c instanceof OrangePeelAdminCommand) {
                    if (orangepeel.getAdmins().has(stringID)) {
                        OrangePeelAdminCommand cmd = (OrangePeelAdminCommand) c;
                        if (orangepeel.getAdmins().getInt(stringID) >= cmd.getCommandlvl()) {

                            message2 = addCommandToMessage(message2, c, ">");
                        }
                    }
                } else {
                    if (c instanceof SimpleCustomCmd) {
                        if (((SimpleCustomCmd) c).isShowInHelp()) {
                            message1 = addCommandToMessage(message1, c, ">");
                        } else {
                            message3 = addCommandToMessage(message3, c, ">");
                        }
                    } else {
                        message1 = addCommandToMessage(message1, c, ">");
                    }

                }

            }
//            message = message + "```";
//            message2 = message2 + "```";
//            message3 = message3 + "```";
            EmbedBuilder one = new EmbedBuilder();
            EmbedBuilder two = new EmbedBuilder();
            EmbedBuilder three = new EmbedBuilder();
            one.withColor(new Color(255, 177, 3));
            two.withColor(new Color(255, 177, 3));
            three.withColor(new Color(255, 177, 3));

            one.withDesc(message1);
            two.withDesc(message2);
            three.withDesc(message3);

            orangepeel.getStatsCounter().incrementStat("helps");
            pm.sendMessage(message);
            pm.sendMessage(one.build());
            if (orangepeel.getAdmins().has(stringID)) {
                pm.sendMessage(two.build());

            }
            pm.sendMessage(three.build());
//            pm.sendMessage("**Add me to your server: ** https://goo.gl/ZcLxNJ\n" + "Join the help server: https://discord.me/OrangePeel");
            orangepeel.getStatsCounter().incrementStat("helped");
            // https://discordapp.com/oauth2/authorize?client_id=306115875784622080&scope=bot
        }
    }

    public String addCommandToMessage(String message, Command c, String prefix) {
//         message = message + c.getDescription().toString() + "\n\n";
        message = message + "**" + c.getName() + ":** " + c.getDescription().getText() + "\n";
        return message;

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
}
