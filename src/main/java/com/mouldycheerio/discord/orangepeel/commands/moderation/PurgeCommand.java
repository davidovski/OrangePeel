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
import sx.blah.discord.util.MessageHistory;

public class PurgeCommand extends OrangePeelCommand {
    public PurgeCommand() {
        setName("purge");
        setDescription(new CommandDescription("purge", "delete the last x messages sent on that channel", "purge [messageCount]"));
        setCatagory(CommandCatagory.MODERATION);
    }

    public void onCommand(OrangePeel bot, IDiscordClient client, IMessage commandMessage, String[] args) {
//        commandMessage.delete();
        IUser punisher = commandMessage.getAuthor();
        IGuild guild = commandMessage.getGuild();
        if (commandMessage.getAuthor().getPermissionsForGuild(commandMessage.getGuild()).contains(Permissions.MANAGE_MESSAGES)) {
            if (args.length == 1) {
                MessageHistory messageHistory = commandMessage.getChannel().getMessageHistory(2);
                for (IMessage m : messageHistory) {
                    System.out.println(m);
                }
                commandMessage.getChannel().bulkDelete();
                commandMessage.getChannel().sendMessage("```Deleted " + messageHistory.size() + " messages!```");

            } else if (args.length == 2) {
                MessageHistory messageHistory = commandMessage.getChannel().getMessageHistory(Integer.parseInt(args[1]));
                commandMessage.getChannel().bulkDelete(messageHistory);
                commandMessage.getChannel().sendMessage("```Deleted " + messageHistory.size() + " messages!```");
            } else if (args.length == 3) {
                try {
                    IUser user = PeelingUtils.mentionToUser(args[1], commandMessage.getGuild());

                } catch (Exception e) {

                }
            }

        } else {
            IPrivateChannel pm = client.getOrCreatePMChannel(punisher);
            pm.sendMessage("```You do not have permissions to execute this command```");
        }
    }
}
