package com.mouldycheerio.discord.orangepeel.commands.moderation;

import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.PeelingUtils;
import com.mouldycheerio.discord.orangepeel.commands.CommandCatagory;
import com.mouldycheerio.discord.orangepeel.commands.CommandDescription;
import com.mouldycheerio.discord.orangepeel.commands.OrangePeelCommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class KickCommand extends OrangePeelCommand {
    public KickCommand() {
        setName("kick");
        setDescription(new CommandDescription("kick", "kick someone of the server", "kick @user [reason]"));
        setCatagory(CommandCatagory.MODERATION);
    }

    public void onCommand(OrangePeel bot, IDiscordClient client, IMessage commandMessage, String[] args) {
        IUser punisher = commandMessage.getAuthor();
        IGuild guild = commandMessage.getGuild();
        if (commandMessage.getAuthor().getPermissionsForGuild(commandMessage.getGuild()).contains(Permissions.KICK)) {

            IUser user = PeelingUtils.mentionToUser(args[1], commandMessage.getGuild());
            guild.kickUser(user, commandMessage.getContent());
            IPrivateChannel pm = client.getOrCreatePMChannel(user);
            pm.sendMessage("Bya! you have been kicked on " + guild.getName() + " by " + punisher.getName() + "!");
            commandMessage.getChannel().sendMessage(args[1] + " has been removed from the server :D");

        } else {
            IPrivateChannel pm = client.getOrCreatePMChannel(punisher);
            pm.sendMessage("```You do not have permissions to execute this command```");
        }
    }
}
