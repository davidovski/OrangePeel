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
        setDescription(new CommandDescription("Start Quick Think Act", "Start a game of quick! Think! Act!.\nAn admin must run this command to avoid spam.", "qta"));
        addAlias("startQTA");
        addAlias("startQuickGame");
        setCatagory(CommandCatagory.GAMES);
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        if (commandMessage.getAuthor().getPermissionsForGuild(commandMessage.getGuild()).contains(Permissions.ADMINISTRATOR)) {
            client.getDispatcher().registerListener(this);
            commandMessage.delete();
            // commandMessage.getChannel().sendMessage("");
            games.add(new QuickPlayGame(commandMessage.getChannel()));
            Logger.info("found");
        }
    }

    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) {
        IChannel channel = event.getChannel();
        IGuild server = event.getGuild();
        String message = event.getMessage().getContent();
        Iterator<QuickPlayGame> iterator = games.iterator();
        Logger.info("found");
        if (!event.getMessage().getAuthor().isBot()) {
            while (iterator.hasNext()) {
                QuickPlayGame game = iterator.next();

                if (game.getChannel().getStringID().equals(channel.getStringID())) {
                    if (game.isPlaying()) {
                        Logger.info("testing against " + game.getCurrentWord());
                        boolean equals = message.equals(game.getCurrentWord());
                        if (game.isContains()) {
                            equals = message.toLowerCase().contains(game.getCurrentWord().toLowerCase());
                        }
                        if (equals) {
                            Logger.info("tested");
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
