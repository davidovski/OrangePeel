package com.mouldycheerio.discord.orangepeel.commands.coin;

import java.awt.Color;

import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.commands.CommandCatagory;
import com.mouldycheerio.discord.orangepeel.commands.CommandDescription;
import com.mouldycheerio.discord.orangepeel.commands.OrangePeelCommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;

public class ShopCommand extends OrangePeelCommand {

    public ShopCommand() {
        setName("shop");
        setDescription(new CommandDescription("shop", "check out the store", "shop"));
        setCatagory(CommandCatagory.COINS);
        addAlias("store");
    }

    @Override
    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        if (args.length == 1) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.withTitle("The Peel Shop.");

            embedBuilder.withAuthorName("Jackie Chan");
            embedBuilder.withAuthorUrl("http://jackiechan.com/");
            embedBuilder.withImage("https://pmcvariety.files.wordpress.com/2017/09/jackie_chan.png");

            embedBuilder.withDescription("Welcome to My Store, Please take a look around!\n\n" + "**1 Peel Point** - 1000 coins\n" + "**1000 coins** - 1 Peel Point"
                    + "\n\nuse `>shop conv peel` to convert 1000 coins into one peel point\nand `>shop conv coins` to convert your 1 peel point back into 1000 coins\n"
                    + "Peel Points are a global currency, meaning that you always have **" + orangepeel.coinController().getPeelsForUser(commandMessage.getAuthor())
                    + "** peel points.\n" + "Coins, however, are only per server, and are gained by being active. On this server, " + commandMessage.getGuild().getName()
                    + ", you have **" + orangepeel.coinController().getCoinsForUser(commandMessage.getAuthor(), commandMessage.getGuild()) + "** coins. " + CoinController.emote);

            // embedBuilder.appendField("1 Peel Point", "1000" + CoinController.emote, true);
            // embedBuilder.appendField("1000" + CoinController.emote, "1 Peel Point", true);

            embedBuilder.withColor(new Color(54, 57, 62));

            commandMessage.getChannel().sendMessage(embedBuilder.build());

        } else if (args.length > 1) {
            if ("buy".equals(args[1]) || "b".equals(args[1])) {

            } else if ("convert".equals(args[1]) || "conv".equals(args[1])) {

                if ("c".equals(args[2]) || "coin".equals(args[2]) || "coins".equals(args[2])) {
                    if (orangepeel.coinController().getPeelsForUser(commandMessage.getAuthor()) >= 1) {
                        orangepeel.coinController().incrementCoins(1000, commandMessage.getAuthor(), commandMessage.getGuild());
                        orangepeel.coinController().incrementPeels(-1, commandMessage.getAuthor());
                        sendJackieChanMessage("Well done! You just converted 1 peel point into 1000 coins, now you can spend those coins on this server!",
                                commandMessage.getChannel());
                    } else {
                        sendJackieChanMessage("You don't have enough peel points for that.", commandMessage.getChannel());
                    }

                } else if ("peel".equals(args[2]) || "p".equals(args[2])) {
                    if (orangepeel.coinController().getCoinsForUser(commandMessage.getAuthor(), commandMessage.getGuild()) >= 1000) {
                        orangepeel.coinController().incrementPeels(1, commandMessage.getAuthor());
                        orangepeel.coinController().incrementCoins(-1000, commandMessage.getAuthor(), commandMessage.getGuild());
                        sendJackieChanMessage("Well done! You just converted 1000 coins points into 1 Peel point!", commandMessage.getChannel());
                    } else {
                        sendJackieChanMessage("You don't have enough coins for that.", commandMessage.getChannel());
                    }
                } else {
                    sendJackieChanMessage("Please specify either `peel` or `coin` to convert", commandMessage.getChannel());
                }
            } else {

            }
        } else {
            sendJackieChanMessage("hmm", commandMessage.getChannel());
        }
    }

    public void sendJackieChanMessage(String content, IChannel channel) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.withAuthorName("Jackie Chan");
        embedBuilder.withAuthorUrl("http://jackiechan.com/");
        embedBuilder.withImage("https://pmcvariety.files.wordpress.com/2017/09/jackie_chan.png");

        embedBuilder.withDescription(content);

        embedBuilder.withColor(new Color(54, 57, 62));

        channel.sendMessage(embedBuilder.build());
    }
}