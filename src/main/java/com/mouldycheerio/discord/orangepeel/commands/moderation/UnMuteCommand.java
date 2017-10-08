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
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class UnMuteCommand extends OrangePeelCommand {
    public UnMuteCommand() {
        setName("unmute");
        setDescription(new CommandDescription("unmute", "unmute a user", "unmute @user"));
        setCatagory(CommandCatagory.MODERATION);
    }

    public void onCommand(OrangePeel bot, IDiscordClient client, IMessage commandMessage, String[] args) {
        IUser punisher = commandMessage.getAuthor();
        IGuild guild = commandMessage.getGuild();
        if (commandMessage.getAuthor().getPermissionsForGuild(commandMessage.getGuild()).contains(Permissions.VOICE_MUTE_MEMBERS)) {

            IUser user = PeelingUtils.mentionToUser(args[1], commandMessage.getGuild());
            if (bot.getMuted().containsKey(guild.getStringID())) {
                String roleID = bot.getMuted().get(guild.getStringID());
                IRole r = guild.getRoleByID(Long.parseLong(roleID));
                user.removeRole(r);

                IPrivateChannel pm = client.getOrCreatePMChannel(user);
                pm.sendMessage("You have been unmuted on " + guild.getName() + " by " + punisher.getName() + "");
                commandMessage.getChannel().sendMessage(args[1] + " can speak again. :cry:");
            }

        } else {
            IPrivateChannel pm = client.getOrCreatePMChannel(punisher);
            pm.sendMessage("```You do not have permissions to execute this command```");
        }
    }
}
