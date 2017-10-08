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
        setCatagory(CommandCatagory.ABOUT);
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
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            IPrivateChannel pm = client.getOrCreatePMChannel(commandMessage.getAuthor());

            String message = "";
            try {
                message = message + "```" + getHTTP("http://artii.herokuapp.com/make?text=OrangePeel&font=weird") + "```\n";
            } catch (Exception e) {
                message = "``` Orange Peel```";
            }
            message = message + "**by davidovski**\n\n";
            String message1 = "***__Commands__***\n\n";
            String messageabout = "\n**__bot commands__**\n\n";
            String messagemoderation = "\n**__Server Moderation commands__**\n\n";
            String messagefun = "\n**__Fun Commands__**\n\n";
            String messagevoting = "\n**__Voting Commands__**\n\n";
            String messageutil = "\n**__Util Commands__**\n\n";
            String messagegames = "\n**__Games Commands__**\n\n";
            String messagecustom = "\n**__Custom commands__**\n\n";
            String messageother = "\n**__other commands__**\n\n";

            String messageadmin = "\n**__admin commands__**\n\n";

            try {
                if (commandMessage.getGuild() != null) {
                    messagecustom = messagecustom + "*(only work on " + commandMessage.getGuild().getName() + ")*\n";
                }
            } catch (Exception e) {

            }

            for (Command c : commands) {
                if (c instanceof OrangePeelCommand) {
                    OrangePeelCommand cmd = (OrangePeelCommand) c;
                    if (cmd.getCatagory() == CommandCatagory.ABOUT) {
                        messageabout = addCommandToMessage(messageabout, c, ">");
                    } else if (cmd.getCatagory() == CommandCatagory.MODERATION) {
                        messagemoderation = addCommandToMessage(messagemoderation, c, ">");
                    } else if (cmd.getCatagory() == CommandCatagory.FUN) {
                        messagefun = addCommandToMessage(messagefun, c, ">");
                    } else if (cmd.getCatagory() == CommandCatagory.VOTING) {
                        messagevoting = addCommandToMessage(messagevoting, c, ">");
                    } else if (cmd.getCatagory() == CommandCatagory.UTIL) {
                        messageutil = addCommandToMessage(messageutil, c, ">");
                    } else if (cmd.getCatagory() == CommandCatagory.GAMES) {
                        messagegames = addCommandToMessage(messagegames, c, ">");
                    } else if (cmd.getCatagory() == CommandCatagory.CUSTOM) {
                        if (cmd instanceof PerServerCustomCmd) {
                            try {
                                if (((PerServerCustomCmd) cmd).isOnServer(commandMessage.getGuild())) {
                                    messagecustom = addCommandToMessage(messagecustom, c, ">");
                                }
                            } catch (Exception e) {

                            }
                        }
                    } else if (cmd.getCatagory() == CommandCatagory.OTHER) {
                        messageother = addCommandToMessage(messageother, c, ">");
                    } else if (cmd.getCatagory() == CommandCatagory.BOT_ADMIN) {
                        messageadmin = addCommandToMessage(messageadmin, c, ">");
                    } else {
                        messageother = addCommandToMessage(messageother, c, ">");
                    }
                }
            }
            // message = message + "```";
            // message2 = message2 + "```";
            // message3 = message3 + "```";
            EmbedBuilder one = new EmbedBuilder();
            EmbedBuilder two = new EmbedBuilder();
            EmbedBuilder three = new EmbedBuilder();
            one.withColor(new Color(255, 177, 3));
            two.withColor(new Color(255, 177, 3));
            three.withColor(new Color(255, 177, 3));

            one.withDesc(messageabout + messagemoderation + messagefun + messagevoting + messageutil + messagegames + messageother);
            two.withDesc(messagecustom);
            three.withDesc(messageadmin);

            orangepeel.getStatsCounter().incrementStat("helps");
            pm.sendMessage(message);
            pm.sendMessage(one.build());
            if (orangepeel.getAdmins().has(stringID)) {
                pm.sendMessage(two.build());

            }
            pm.sendMessage(three.build());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            pm.sendMessage("https://discord.gg/W7EATbm");
            orangepeel.getStatsCounter().incrementStat("helped");
            // https://discordapp.com/oauth2/authorize?client_id=306115875784622080&scope=bot
        }
    }

    public String addCommandToMessage(String message, Command c, String prefix) {
        // message = message + c.getDescription().toString() + "\n\n";
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
