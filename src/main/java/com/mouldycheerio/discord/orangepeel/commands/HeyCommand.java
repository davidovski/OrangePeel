package com.mouldycheerio.discord.orangepeel.commands;

import java.util.ArrayList;
import java.util.Iterator;

import com.mouldycheerio.discord.orangepeel.Logger;
import com.mouldycheerio.discord.orangepeel.Member;
import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class HeyCommand extends OrangePeelCommand {
    public java.util.List<Member> games = new ArrayList<Member>();

    public HeyCommand() {
        setName("hey");
        setDescription(new CommandDescription("Hey", "Hey!", "hey"));
        setCatagory(CommandCatagory.DEBUG);

    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        client.getDispatcher().registerListener(this);
        commandMessage.getChannel().sendMessage("heyo!");
//        games.add(new Member(commandMessage.getGuild(), commandMessage.getAuthor()));
        Logger.info("found");
    }

    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) {
        IUser user = event.getAuthor();
        IGuild server = event.getGuild();
        String message = event.getMessage().getContent();
        Iterator<Member> iterator = games.iterator();
        while (iterator.hasNext()) {
            Member member = iterator.next();

            if (member.getUser().getStringID().equals(user.getStringID())) {
                if (member.getServer().getStringID().equals(server.getStringID())) {
                    if ("stop".equals(message)) {
                        event.getMessage().getChannel().sendMessage("st–... ok. :(");
                        iterator.remove();
                        break;
                    }
                    event.getMessage().getChannel().sendMessage(message);
                }
            }
        }
    }
}
