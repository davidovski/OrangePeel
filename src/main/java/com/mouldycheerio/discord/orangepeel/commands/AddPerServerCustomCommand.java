package com.mouldycheerio.discord.orangepeel.commands;



import com.mouldycheerio.discord.orangepeel.CommandController;
import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;

public class AddPerServerCustomCommand extends OrangePeelCommand {
    public AddPerServerCustomCommand() {
        setCatagory(CommandCatagory.MODERATION);
        setName("cmd");
        setDescription(new CommandDescription("cmd",
                "makes a command that will return a specified string, example: \nuser -> !store\nbot -> here is the invite link for our store! http://google.com/",
                "cmd <command> <text>"));
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        if (commandMessage.getAuthor().getPermissionsForGuild(commandMessage.getGuild()).contains(Permissions.ADMINISTRATOR)) {
            CommandController commandController = orangepeel.getEventListener().getCommandController();

            if (args.length >= 2) {
                StringBuilder sb = new StringBuilder();
                if (args.length > 1) {
                    for (int i = 2; i < args.length; i++) {
                        if (i > 2) {
                            sb.append(" ");
                        }
                        sb.append(args[i]);
                    }
                }

                String text = sb.toString();
                String commandName = args[1];
                commandController.setToadd(new PerServerCustomCmd(commandName,
                        new CommandDescription(commandName, "A " + commandMessage.getGuild().getName() + " Command", commandName), text, commandMessage.getGuild()));
                commandMessage.reply("All members on this server can now use: `+" + commandName + "`");
            }
        }
        orangepeel.saveAll();
    }
}
