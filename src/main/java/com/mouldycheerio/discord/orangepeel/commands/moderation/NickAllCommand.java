package com.mouldycheerio.discord.orangepeel.commands.moderation;

import com.mouldycheerio.discord.orangepeel.Logger;
import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.commands.CommandCatagory;
import com.mouldycheerio.discord.orangepeel.commands.CommandDescription;
import com.mouldycheerio.discord.orangepeel.commands.OrangePeelCommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.RequestBuffer;

public class NickAllCommand extends OrangePeelCommand {
    public NickAllCommand() {
        setName("nickall");
        setDescription(new CommandDescription("nickall", "change the nickname of all the users to a specified string", "nickall <nickname>"));
        setCatagory(CommandCatagory.MODERATION);
    }

    @Override
    public void onCommand(OrangePeel bot, IDiscordClient client, IMessage commandMessage, String[] args) {
        IUser punisher = commandMessage.getAuthor();
        IGuild guild = commandMessage.getGuild();
        if (commandMessage.getAuthor().getPermissionsForGuild(commandMessage.getGuild()).contains(Permissions.ADMINISTRATOR)
                && commandMessage.getAuthor().getPermissionsForGuild(commandMessage.getGuild()).contains(Permissions.MANAGE_NICKNAMES)) {

            StringBuilder sb = new StringBuilder();
            if (args.length > 1) {
                for (int i = 1; i < args.length; i++) {
                    if (i > 1) {
                        sb.append(" ");
                    }
                    sb.append(args[i]);
                }

            }
            final String nick = sb.toString();

            IMessage m;
            if (args.length > 1) {
                m = commandMessage.getChannel().sendMessage("Changing everyone's nickname to **" + nick + "**...");
            } else {
                m = commandMessage.getChannel().sendMessage("Resetting everyone's nickname...");
            }
            for (IUser u : guild.getUsers()) {
                RequestBuffer.request(() -> {
                    try {
                        if (args.length > 1) {
                            guild.setUserNickname(u, nick);
                        } else {
                            guild.setUserNickname(u, u.getName());
                        }
                        Logger.info("changed " + u.getName() + "'s nick to " + nick);
                    } catch (MissingPermissionsException e) {
                        e.printStackTrace();
                    } catch (RateLimitException e) {
                        throw e; // This makes sure that RequestBuffer will do the retry for you
                    }
                });
            }

        } else {
            IPrivateChannel pm = client.getOrCreatePMChannel(punisher);
            pm.sendMessage("```You do not have permissions to execute this command```");
        }
    }
}
