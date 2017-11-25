package com.mouldycheerio.discord.orangepeel.commands.coin;

import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.PeelingUtils;
import com.mouldycheerio.discord.orangepeel.commands.CommandCatagory;
import com.mouldycheerio.discord.orangepeel.commands.CommandDescription;
import com.mouldycheerio.discord.orangepeel.commands.OrangePeelCommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.StatusType;

public class PayCommand extends OrangePeelCommand {

    public PayCommand() {
        setName("pay");
        setDescription(new CommandDescription("pay", "Pay Someone [x] ammount of coins", "pay [user] [x]"));
        setCatagory(CommandCatagory.COINS);
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {

        IGuild guild = commandMessage.getGuild();
        int ammount = Integer.parseInt(args[2]);
        String id = PeelingUtils.mentionToId(args[1], guild);
        IUser from = commandMessage.getAuthor();
        if (id.equals(from.getStringID())) {
            commandMessage.getChannel().sendMessage("Wow! you just gave your self some money. How boring.");
            return;
        }
        IUser to = client.getUserByID(Long.parseLong(id));
        if (orangepeel.coinController().getCoinsForUser(from, guild) >= ammount) {
            orangepeel.coinController().incrementCoins(-ammount, from, guild);
            orangepeel.coinController().incrementCoins(ammount, to, guild);
            commandMessage.getChannel().sendMessage("**" + from.getName() + "** ~~---" + CoinController.emote + " " + ammount + " ---~~> **" + to.getName() + "**");
            if (to.getPresence().getStatus() == StatusType.OFFLINE) {
                try {
                    to.getOrCreatePMChannel().sendMessage(from.getName() + " has just sent you " + ammount + " on " + guild.getName() + ".\nYour total balance is now "
                            + orangepeel.coinController().getCoinsForUser(to, guild) + "coins");
                } catch (Exception e) {

                }
            }
        } else {
            commandMessage.getChannel().sendMessage(CoinController.emote + ": `You don't have " + ammount + " coins to give away.` " + CoinController.emote );
        }

    }
}
