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

public class BanCommand extends OrangePeelCommand {
    public BanCommand() {
        setName("ban");
        setDescription(new CommandDescription("ban", "ban someone of the server", "ban @user [reason]"));
        setCatagory(CommandCatagory.MODERATION);
    }

    public void onCommand(OrangePeel bot, IDiscordClient client, IMessage commandMessage, String[] args) {
        IUser punisher = commandMessage.getAuthor();
        IGuild guild = commandMessage.getGuild();
        if (commandMessage.getAuthor().getPermissionsForGuild(commandMessage.getGuild()).contains(Permissions.BAN)) {

            IUser user = PeelingUtils.mentionToUser(args[1], commandMessage.getGuild());
            guild.banUser(user, commandMessage.getContent());
            IPrivateChannel pm = client.getOrCreatePMChannel(user);
            pm.sendMessage("Yikes! You have been deleted from " + guild.getName() + " forever! Please don't seak revenge on " + punisher.getName() + " for doing this!");
            commandMessage.getChannel().sendMessage(args[1] + " has been removed from the server, ***FOREVER*** *(maybe)*");

        } else {
            IPrivateChannel pm = client.getOrCreatePMChannel(punisher);
            pm.sendMessage("```You do not have permissions to execute this command```");
        }
    }
}
