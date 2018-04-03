package com.mouldycheerio.discord.orangepeel.commands.moderation;

import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.PeelingUtils;
import com.mouldycheerio.discord.orangepeel.commands.CommandCatagory;
import com.mouldycheerio.discord.orangepeel.commands.CommandDescription;
import com.mouldycheerio.discord.orangepeel.commands.OrangePeelCommand;
import com.mouldycheerio.discord.web.ServerConfig;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class MuteCommand extends OrangePeelCommand {
    public MuteCommand() {
        setName("mute");
        setDescription(new CommandDescription("mute", "mute a user", "mute @user"));
        setCatagory(CommandCatagory.MODERATION);
    }

    @Override
    public void onCommand(OrangePeel bot, IDiscordClient client, IMessage commandMessage, String[] args) {
        IUser punisher = commandMessage.getAuthor();
        IGuild guild = commandMessage.getGuild();
        if (commandMessage.getAuthor().getPermissionsForGuild(commandMessage.getGuild()).contains(Permissions.VOICE_MUTE_MEMBERS)) {

            IUser user = PeelingUtils.mentionToUser(args[1], commandMessage.getGuild());
            ServerConfig config = bot.getConfig(commandMessage.getGuild());
            if (config.hasMutedRole()) {
                IRole r = config.getMutedRole();
                user.addRole(r);

                IPrivateChannel pm = client.getOrCreatePMChannel(user);
                pm.sendMessage("Zip it! You have been muted on " + guild.getName() + " by " + punisher.getName() + "");
                commandMessage.getChannel().sendMessage(args[1] + " will not speak again");
            } else {
                commandMessage.reply("This functionallity has not yet been setup, please configure it with >setupMute");
            }

        } else {
            IPrivateChannel pm = client.getOrCreatePMChannel(punisher);
            pm.sendMessage("```You do not have permissions to execute this command```");
        }
    }
}
