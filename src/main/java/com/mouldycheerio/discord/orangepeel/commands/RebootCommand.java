package com.mouldycheerio.discord.orangepeel.commands;

import com.mouldycheerio.discord.orangepeel.BotStatus;
import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class RebootCommand extends OrangePeelAdminCommand {
    public RebootCommand() {
        setName("reboot");
        setDescription(new CommandDescription("reboot", "reboot the bot", "reboot "));
        setCommandlvl(5);
        addAlias("restart");
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
            IMessage m = commandMessage.getChannel().sendMessage("Restarting Bot...");

            orangepeel.saveAll();
            m.edit(":recycle: Rebooting... :recycle:");
            orangepeel.getClient().logout();

            orangepeel.setStatus(BotStatus.REBOOTING);
    }
}
