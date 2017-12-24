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

public class UnBanCommand extends OrangePeelCommand {
    public UnBanCommand() {
        setName("unban");
        setDescription(new CommandDescription("unban", "unban someone of the server", "unban @user [reason]"));
        setCatagory(CommandCatagory.MODERATION);
        addAlias("pardon");
    }

    @Override
    public void onCommand(OrangePeel bot, IDiscordClient client, IMessage commandMessage, String[] args) {
        IUser punisher = commandMessage.getAuthor();
        IGuild guild = commandMessage.getGuild();
        if (commandMessage.getAuthor().getPermissionsForGuild(commandMessage.getGuild()).contains(Permissions.BAN)) {

            String id = PeelingUtils.mentionToIdEz(args[1]);
            guild.pardonUser(Long.parseLong(id));
            IPrivateChannel pm = client.getOrCreatePMChannel(client.getUserByID(Long.parseLong(id)));
            pm.sendMessage("Wow! looks like you have been unbanned on " + guild.getName() + " by " + punisher.getName() + ", thank them for doing this!");
            commandMessage.getChannel().sendMessage(args[1] + " has been unbanned.");

        } else {
            IPrivateChannel pm = client.getOrCreatePMChannel(punisher);
            pm.sendMessage("```You do not have permissions to execute this command```");
        }
    }
}
