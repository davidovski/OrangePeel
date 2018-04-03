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
    public HashMap<IChannel, Long> lastMessages = new HashMap<IChannel, Long>();

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

    @Override
    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        this.orangepeel = orangepeel;
        this.client = client;
        if (!registered) {
            client.getDispatcher().registerListener(this);
            registered = true;
        }
        if (commandMessage.getAuthor().getPermissionsForGuild(commandMessage.getGuild()).contains(Permissions.ADMINISTRATOR)) {
            running.put(commandMessage.getChannel(), commandMessage.getAuthor());
            IMessage mg = commandMessage.getChannel().sendMessage(firstMessage);
            lastMessages.put(mg.getChannel(), mg.getLongID());

        }
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
                    String m = event.getChannel().getMessageByID(lastMessages.get(event.getChannel())).getContent();

                    if (m.equals(firstMessage)) {
                        if (message.equalsIgnoreCase("no")) {
                            it.remove();
                        } else {
                            IMessage mg = event.getMessage().getChannel().sendMessage(secondMessage);
                            lastMessages.put(mg.getChannel(), mg.getLongID());
                        }
                        break;
                    } else if (m.equals(secondMessage)) {
                        if (message.equalsIgnoreCase("no")) {
                            IMessage mg = event.getMessage().getChannel().sendMessage(thirdMessage);
                            lastMessages.put(mg.getChannel(), mg.getLongID());
                        } else {
                            IMessage mg = event.getMessage().getChannel().sendMessage(autoroleMessage);
                            lastMessages.put(mg.getChannel(), mg.getLongID());
                        }
                        break;
                    } else if (m.equals(autoroleMessage)) {
                        String roleID = PeelingUtils.mentionToIdEz(message);
                        orangepeel.getConfig(server).setAutoRole(event.getGuild().getRoleByID(Long.parseLong(roleID)));
                        IMessage mg = event.getMessage().getChannel().sendMessage(thirdMessage);
                        lastMessages.put(mg.getChannel(), mg.getLongID());
                        break;
                    } else if (m.equals(thirdMessage)) {
                        if (message.equalsIgnoreCase("yes")) {
                            IMessage mg = event.getMessage().getChannel().sendMessage(greetchannelMessage);
                            lastMessages.put(mg.getChannel(), mg.getLongID());
                            break;
                        } else {
                            IMessage mg = event.getMessage().getChannel().sendMessage(fourthMessage);
                            lastMessages.put(mg.getChannel(), mg.getLongID());
                            break;
                        }

                    } else if (m.equals(greetchannelMessage)) {
                        String channelID = PeelingUtils.mentionToIdEz(message);
                        orangepeel.getConfig(server).setGreetChannel(event.getGuild().getChannelByID(Long.parseLong(channelID)));
                        IMessage mg = event.getMessage().getChannel().sendMessage(fourthMessage);
                        lastMessages.put(mg.getChannel(), mg.getLongID());
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
                            orangepeel.getConfig(server).setMutedRole(role);
                        }
                        IMessage mg = event.getMessage().getChannel().sendMessage(done);
                        lastMessages.put(mg.getChannel(), mg.getLongID());
                        it.remove();
                        orangepeel.getConfig(server).save();
                        break;

                    }
                }
            }
        }

    }
}
