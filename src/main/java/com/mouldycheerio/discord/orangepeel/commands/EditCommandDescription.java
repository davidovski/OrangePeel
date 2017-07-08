package com.mouldycheerio.discord.orangepeel.commands;

import com.mouldycheerio.discord.orangepeel.CommandController;
import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class EditCommandDescription extends OrangePeelAdminCommand {
    public EditCommandDescription() {
        setName("editCmd");
        setDescription(new CommandDescription("editCmd", "edit a custom command\n"
                + "`-d` edit the description\n"
                + "`-n` edit the name\n"
                + "`-t` edit the text\n"
                + "`-r` remove whole command (perm required `3`)", "newCmd -[d, n, t, r] <command> <new >"));
        setCommandlvl(2);
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
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
                commandController.setToadd(new SimpleCustomCmd(commandName, new CommandDescription(commandName, "A custom added Command", commandName), text));
                commandMessage.reply("I've added that command to the command list! have fun with `>" + commandName + "` !");
        }
        orangepeel.saveAll();
    }
}
