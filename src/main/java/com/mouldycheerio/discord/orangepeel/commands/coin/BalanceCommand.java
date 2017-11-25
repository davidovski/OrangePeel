package com.mouldycheerio.discord.orangepeel.commands.coin;

import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.commands.CommandCatagory;
import com.mouldycheerio.discord.orangepeel.commands.CommandDescription;
import com.mouldycheerio.discord.orangepeel.commands.OrangePeelCommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class BalanceCommand extends OrangePeelCommand {

    public BalanceCommand() {
        setName("balance");
        setDescription(new CommandDescription("balance", "check your coin balance!", "balance"));
        setCatagory(CommandCatagory.COINS);
        addAlias("$");
        addAlias("bal");
    }

    @Override
    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        if (commandMessage.getChannel().isPrivate()) {
            commandMessage.getChannel().sendMessage("" + "You have **" + orangepeel.coinController().getPeelsForUser(commandMessage.getAuthor()) + "** peel points on this server. \n" + "`Buy peel points at the >shop at the price of 1000 coins each!`");
            return;
        }
        commandMessage.getChannel().sendMessage(CoinController.emote + " You have **" + orangepeel.coinController().getCoinsForUser(commandMessage.getAuthor(), commandMessage.getGuild()) + "** coins on this server." +
    "\n" + "You have **" + orangepeel.coinController().getPeelsForUser(commandMessage.getAuthor()) + "** peel points on this server. \n" + "`Buy peel points at the >shop at the price of 1000 coins each!`");
    }
}
