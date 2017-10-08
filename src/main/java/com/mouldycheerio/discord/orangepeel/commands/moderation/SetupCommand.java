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

public class SetupCommand extends OrangePeelCommand {

    public HashMap<IChannel, IUser> running = new HashMap<IChannel, IUser>();
    private IDiscordClient client;
    private OrangePeel orangepeel;
    private String firstMessage;
    private String secondMessage;
    private String autoroleMessage;
    private String thirdMessage;
    private String greetchannelMessage;
    private String done;
    private String fourthMessage;
    private boolean registered = false;

    public SetupCommand() {
        setCatagory(CommandCatagory.MODERATION);
        setName("setup");
        setDescription(new CommandDescription("setup", "sets the server up for you", "setup"));

        firstMessage = "Ok, so in this setup we will be covering my main features, these include: \n"
                + "     :arrow_forward: Autorole\n     :arrow_forward: Greetings\n     :arrow_forward: muting\nDo you wish to proceed, a yes or no will be great.";
        secondMessage = "Ok then, would you like me to give a role to all new members that join the server, something like a member role. **yes** or **no**?";
        autoroleMessage = "Please tag that role now!";
        thirdMessage = "Next, would you like me to send a message in a channel when a member joins/leaves?";
        greetchannelMessage = "Fine, please tag the channel you want me to do this in now.";
        fourthMessage = "Would you like to allow your staff members to mute people with the `mute` command? **yes** or **no**?";
        done = "Ok, we are all done, thank you.";
    }

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
                    for (IMessage iMessage : channel.getMessageHistory(10)) {
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
                                if (message.equalsIgnoreCase("no")) {
                                    event.getMessage().getChannel().sendMessage(thirdMessage);
                                } else {
                                    event.getMessage().getChannel().sendMessage(autoroleMessage);
                                }
                                break;
                            } else if (m.equals(autoroleMessage)) {
                                String roleID = PeelingUtils.mentionToIdEz(message);
                                orangepeel.getAutoRole().put(event.getGuild().getStringID(), roleID);
                                event.getMessage().getChannel().sendMessage(thirdMessage);
                                break;
                            } else if (m.equals(thirdMessage)) {
                                if (message.equalsIgnoreCase("yes")) {
                                    event.getMessage().getChannel().sendMessage(greetchannelMessage);
                                    break;
                                } else {
                                    event.getMessage().getChannel().sendMessage(fourthMessage);
                                    break;
                                }

                            } else if (m.equals(greetchannelMessage)) {
                                String channelID = PeelingUtils.mentionToIdEz(message);
                                orangepeel.getGreet().put(event.getGuild().getStringID(), channelID);
                                event.getMessage().getChannel().sendMessage(fourthMessage);
                                break;

                            } else if (m.equals(fourthMessage)) {
                                if (message.equalsIgnoreCase("yes")) {
                                    IRole role = event.getGuild().createRole();
                                    role.changeName("Muted");
                                    role.changeColor(Color.black);
                                    for (IChannel iChannel : event.getGuild().getChannels()) {
                                        EnumSet<Permissions> toremove = EnumSet.of(Permissions.SEND_MESSAGES);
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
