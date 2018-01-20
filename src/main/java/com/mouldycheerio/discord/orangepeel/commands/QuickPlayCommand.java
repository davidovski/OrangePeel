package com.mouldycheerio.discord.orangepeel.commands;

import java.util.ArrayList;
import java.util.Iterator;

import com.mouldycheerio.discord.orangepeel.Logger;
import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.games.QuickPlayGame;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;

public class QuickPlayCommand extends OrangePeelCommand {
    public java.util.List<QuickPlayGame> games = new ArrayList<QuickPlayGame>();

    public QuickPlayCommand() {
        setName("qta");
        setDescription(new CommandDescription("Start Quick Think Act", "Start a game of quick! Think! Act!.\nAn admin must run this command to avoid spam.\nARGS:\n• players: players to wait for to join before starting the game\n• score: the score that a player must reach to trigger the end of the game.", "qta ["));
        addAlias("startQTA");
        addAlias("startQuickGame");
        setCatagory(CommandCatagory.GAMES);
    }

    @Override
    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        if (commandMessage.getAuthor().getPermissionsForGuild(commandMessage.getGuild()).contains(Permissions.ADMINISTRATOR)) {
            client.getDispatcher().registerListener(this);
            commandMessage.delete();
            // commandMessage.getChannel().sendMessage("");
            int players = 5;
            if (args[1] != null) {
                try {
                    players = Integer.parseInt(args[1]);
                }catch (Exception e) {
                }
            }
            int score = 10;
            if (args[2] != null) {
                try {
                    score = Integer.parseInt(args[2]);
                }catch (Exception e) {
                }
            }
            games.add(new QuickPlayGame(commandMessage.getChannel(), orangepeel, players, score));
            Logger.info("found");
        } else {
            commandMessage.reply("I'm sorry, but you must be a server admin to run this, as this command will clog the channel up with messages.");
        }
    }

    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) throws InterruptedException {
        IChannel channel = event.getChannel();
        IGuild server = event.getGuild();
        String message = event.getMessage().getContent();
        Iterator<QuickPlayGame> iterator = games.iterator();
        if (!event.getMessage().getAuthor().isBot()) {
            while (iterator.hasNext()) {
                QuickPlayGame game = iterator.next();

                if (game.getChannel().getStringID().equals(channel.getStringID())) {
                    if (game.isPlaying()) {
                        boolean equals = message.equals(game.getCurrentWord());
                        if (game.isContains()) {
                            equals = message.toLowerCase().contains(game.getCurrentWord().toLowerCase());
                        }
                        if (equals) {
                            game.nextWord(event.getMessage().getAuthor());
                        }
                    } else {
                        if (message.equalsIgnoreCase("ready")) {
                            event.getMessage().delete();
                            if (!game.getScores().containsKey(event.getMessage().getAuthor())) {
                                game.addPlayer();
                            }
                            game.getScores().put(event.getMessage().getAuthor(), 0);

                        }
                    }
                }
            }
        }
    }
}
