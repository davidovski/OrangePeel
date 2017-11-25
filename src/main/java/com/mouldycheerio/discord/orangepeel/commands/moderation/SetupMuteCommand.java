package com.mouldycheerio.discord.orangepeel.commands.moderation;

import java.awt.Color;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.PeelingUtils;
import com.mouldycheerio.discord.orangepeel.commands.CommandCatagory;
import com.mouldycheerio.discord.orangepeel.commands.CommandDescription;
import com.mouldycheerio.discord.orangepeel.commands.OrangePeelCommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class SetupMuteCommand extends OrangePeelCommand {

    public HashMap<IChannel, IUser> running = new HashMap<IChannel, IUser>();
    private IDiscordClient client;
    private OrangePeel orangepeel;
    private String firstMessage;
    private String done;
    private boolean registered = false;

    public SetupMuteCommand() {
        setCatagory(CommandCatagory.MODERATION);
        setName("setupMute");
        setDescription(new CommandDescription("setupMute", "sets the server up for you", "setupMute"));

        firstMessage = "Would you like me to create a new muted role so i can mute your members? Say `new`, if you wish to make a new one, or just tag the role you whish me to use.";
        done = "I've done that, you can go home now.";
    }

    @Override
    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        this.orangepeel = orangepeel;
        this.client = client;
        if (!registered) {
            client.getDispatcher().registerListener(this);
            registered  = true;
        }
        if (commandMessage.getAuthor().getPermissionsForGuild(commandMessage.getGuild()).contains(Permissions.ADMINISTRATOR)) {
            running.put(commandMessage.getChannel(), commandMessage.getAuthor());
            commandMessage.getChannel().sendMessage(firstMessage);
        }
        orangepeel.saveAll();
    }

    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) {
        IChannel channel = event.getChannel();
        IGuild server = event.getGuild();
        String message = event.getMessage().getContent();
        Iterator<Entry<IChannel, IUser>> it = running.entrySet().iterator();
        while (it.hasNext()) {
            Entry<IChannel, IUser> entry = it.next();
            if (entry.getValue().equals(event.getAuthor())) {
                if (entry.getKey().equals(event.getChannel())) {
                    for (IMessage iMessage : channel.getMessageHistory(5)) {
                        String m = iMessage.getContent();
                        if (iMessage.getAuthor().getStringID().equals(client.getOurUser().getStringID())) {

                            if (m.equals(firstMessage)) {
                                if (message.equalsIgnoreCase("cancel")) {
                                    it.remove();
                                } else if (message.equalsIgnoreCase("new")) {
                                        IRole role = event.getGuild().createRole();
                                        role.changeName("Muted");
                                        role.changeColor(Color.black);
                                        for (IChannel iChannel : event.getGuild().getChannels()) {
                                            EnumSet<Permissions> toremove = EnumSet.of(Permissions.SEND_MESSAGES, Permissions.VOICE_SPEAK, Permissions.ADD_REACTIONS);
                                            EnumSet<Permissions> toadd = EnumSet.noneOf(Permissions.class);
                                            iChannel.overrideRolePermissions(role, toadd, toremove);
                                        }
                                        orangepeel.getMuted().put(event.getGuild().getStringID(), role.getStringID());
                                } else {
                                        IRole role = event.getGuild().getRoleByID(Long.parseLong(PeelingUtils.mentionToIdEz(message)));
                                        role.changeName("Muted");
                                        role.changeColor(Color.black);
                                        for (IChannel iChannel : event.getGuild().getChannels()) {
                                            EnumSet<Permissions> toremove = EnumSet.of(Permissions.SEND_MESSAGES, Permissions.VOICE_SPEAK, Permissions.ADD_REACTIONS);
                                            EnumSet<Permissions> toadd = EnumSet.noneOf(Permissions.class);
                                            iChannel.overrideRolePermissions(role, toadd, toremove);
                                        }
                                        orangepeel.getMuted().put(event.getGuild().getStringID(), role.getStringID());
                                }

                                event.getMessage().getChannel().sendMessage(done);
                                it.remove();
                                orangepeel.saveAll();
                                break;

                            }
                        }
                    }
                }
            }
        }

    }
}
