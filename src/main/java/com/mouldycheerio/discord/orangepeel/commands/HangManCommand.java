package com.mouldycheerio.discord.orangepeel.commands;

import java.util.ArrayList;
import java.util.Iterator;

import com.mouldycheerio.discord.orangepeel.Logger;
import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.games.HangManGame;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;

public class HangManCommand extends OrangePeelCommand {
    public java.util.List<HangManGame> games = new ArrayList<HangManGame>();

    public HangManCommand(IDiscordClient client) {
        client.getDispatcher().registerListener(this);
        setName("hang");
        setDescription(new CommandDescription("Hangman", "Start a game of hangman.", "hang"));
        addAlias("hangman");
        addAlias("hm");
        setCatagory(CommandCatagory.GAMES);
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {

        commandMessage.delete();
        // commandMessage.getChannel().sendMessage("");
        try {
            games.add(new HangManGame(commandMessage.getChannel()));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Logger.info("found");
    }

    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) {
        IChannel channel = event.getChannel();
        IGuild server = event.getGuild();
        String message = event.getMessage().getContent();
        if (!event.getMessage().getAuthor().isBot()) {
            Iterator<HangManGame> iterator = games.iterator();
            while (iterator.hasNext()) {
                HangManGame game = iterator.next();
                if (message.length() == 1) {
                    event.getMessage().delete();
                    game.guess(new Character(event.getMessage().getContent().charAt(0)));
                    if (!game.isPlaying()) {
                        iterator.remove();
                    }
                }
            }
        }
    }
}
