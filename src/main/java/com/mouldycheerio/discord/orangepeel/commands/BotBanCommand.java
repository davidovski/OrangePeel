package com.mouldycheerio.discord.orangepeel.commands;

import java.util.HashMap;
import java.util.Iterator;

import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.PeelingUtils;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class BotBanCommand extends OrangePeelAdminCommand {
    private HashMap<String, String> vars;

    public BotBanCommand() {
        setName("botban");
        setDescription(new CommandDescription("BotBan", "Bans a user from using the bot", "botban [mention]"));
        setCommandlvl(3);
        vars = new HashMap<String, String>();
        vars.put("pi", "" + Math.PI);
        addAlias("bb");
        setCatagory(CommandCatagory.BOT_ADMIN);
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        IUser user = orangepeel.getClient().getUserByID(PeelingUtils.mentionToId(args[1], commandMessage.getGuild()));
        if (user != null) {
            if (args.length == 3 && "pardon".equals(args[2])) {
                Iterator<Long> it = orangepeel.getBanned().iterator();
                while (it.hasNext()) {
                    if (it.next().equals(user.getLongID())) {
                        it.remove();
                    }
                }
                orangepeel.getClient().getOrCreatePMChannel(user).sendMessage("ok, fine you have been removed from my black list... you can use my commands again >:D");
            } else {
                if (orangepeel.getAdmins().has(user.getStringID())) {
                    if (orangepeel.getAdmins().getInt(commandMessage.getAuthor().getStringID()) > orangepeel.getAdmins().getInt(user.getStringID())) {
                        orangepeel.getBanned().add(user.getLongID());
                        orangepeel.getClient().getOrCreatePMChannel(user)
                                .sendMessage("Congratulations! You have been banned from using the orangepeel bot! Any commands that you do will just be ignored!");
                    } else {
                        commandMessage.reply("No, you cant ban that person...");
                    }
                } else {
                    orangepeel.getBanned().add(user.getLongID());
                    orangepeel.getClient().getOrCreatePMChannel(user)
                            .sendMessage("Congratulations! You have been banned from using the orangepeel bot! Any commands that you do will just be ignored!");
                }
            }
        }
    }

}
