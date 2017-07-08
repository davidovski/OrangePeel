package com.mouldycheerio.discord.orangepeel.commands;

import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.PeelingUtils;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class GiveAdminCommand extends OrangePeelAdminCommand {
    public GiveAdminCommand() {
        setName("givePermLvl");
        setDescription(new CommandDescription("givePermLvl", "Give a user admin privelages\n"
                + "0 - no admin [vote, top, xox...]\n"
                + "1 - Special features \n"
                + "2 - Jr Dev or tester \n"
                + "3 - Admin [setVotes, setPlaying]  \n"
                + "4 - System Admin [reload, loadAll, saveAll]  \n"
                + "5 - Sytem Owner [Reboot, shutdown]", "givePermLvl <@user> <0-5>"));
        setCommandlvl(4);
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        orangepeel.getAdmins().put(PeelingUtils.mentionToId(args[1], commandMessage.getGuild()), Integer.parseInt(args[2]));
        orangepeel.saveAll();
        commandMessage.reply(args[1] + " shall hereby be given premission level " + args[2] + ", meaning that he can do more (or less) than before");
    }
}
