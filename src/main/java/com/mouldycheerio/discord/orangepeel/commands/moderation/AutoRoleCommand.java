package com.mouldycheerio.discord.orangepeel.commands.moderation;

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
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class AutoRoleCommand extends OrangePeelCommand {

    public HashMap<IChannel, IUser> running = new HashMap<IChannel, IUser>();
    private IDiscordClient client;
    private OrangePeel orangepeel;
    private String firstMessage;
    private String secondMessage;
    private String done;
    private boolean registered = false;

    public AutoRoleCommand() {
        setCatagory(CommandCatagory.MODERATION);
        setName("autorole");
        setDescription(new CommandDescription("autorole", "sets up autorole", "autorole [@role]"));

        firstMessage = "Would you like autorole on this server?";
        secondMessage = "Please tag the role you want now";
        done = "Ok, we are all done, thank you.";
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
                    for (IMessage iMessage : channel.getMessageHistoryFrom(client.getOurUser().getLongID(), 1)) {
                        String m = iMessage.getContent();
                        if (iMessage.getAuthor().getStringID().equals(client.getOurUser().getStringID())) {

                            if (m.equals(firstMessage)) {
                                if (message.equalsIgnoreCase("no")) {
                                    it.remove();
                                } else {
                                    event.getMessage().getChannel().sendMessage(secondMessage);
                                }
                                break;
                            } else if (m.equals(secondMessage)) {
                                String roleID = PeelingUtils.mentionToIdEz(message);
                                orangepeel.getConfig(server).setAutoRole(event.getGuild().getRoleByID(Long.parseLong(roleID)));
                                event.getMessage().getChannel().sendMessage(done);
                                it.remove();
                                orangepeel.getConfig(server).save();
                                break;

                            }
                        }
                    }
                }
            }
        }

    }
}
