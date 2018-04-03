package com.mouldycheerio.discord.orangepeel.commands.coin;

import java.util.ArrayList;
import java.util.Iterator;

import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.commands.CommandCatagory;
import com.mouldycheerio.discord.orangepeel.commands.CommandDescription;
import com.mouldycheerio.discord.orangepeel.commands.OrangePeelCommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class NickCommand extends OrangePeelCommand {
    private boolean registered = false;
    public java.util.List<NickChangeConfirmation> confirms = new ArrayList<NickChangeConfirmation>();
    private OrangePeel orangepeel;

    public NickCommand() {
        setName("nick");
        setDescription(new CommandDescription("nick", "nick", "nick"));
        setCatagory(CommandCatagory.COINS);

    }

    @Override
    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        this.orangepeel = orangepeel;
        if (!registered) {
            client.getDispatcher().registerListener(this);
            registered = true;
        }
        StringBuilder sb = new StringBuilder();
        if (args.length > 1) {
            for (int i = 1; i < args.length; i++) {
                if (i > 1) {
                    sb.append(" ");
                }
                sb.append(args[i]);
            }
        }
        String nick = sb.toString();
        if (nick.length() < 32) {
            if (orangepeel.getConfig(commandMessage.getGuild()).isChargeNicks()) {
                int price = calculatePrice(nick);
                int coinsForUser = orangepeel.coinController().getCoinsForUser(commandMessage.getAuthor(), commandMessage.getGuild());
                if (coinsForUser - price > 0) {
                    JackieChan.sendJackieChanMessage("This nickname will cost you " + price + CoinController.emote + ".\nYou have " + coinsForUser + CoinController.emote
                            + "\nAre you sure you want to buy `" + nick + "`?", commandMessage.getChannel());
                    confirms.add(new NickChangeConfirmation(commandMessage.getAuthor(), commandMessage.getGuild(), commandMessage.getChannel(), nick, price));
                } else {
                    JackieChan.sendJackieChanMessage("This nickname will cost you " + price + CoinController.emote + ".\nYou only have " + coinsForUser + CoinController.emote
                            + "\nYou do not have enough money!", commandMessage.getChannel());

                }
            } else {

                commandMessage.getGuild().setUserNickname(commandMessage.getAuthor(), nick);
                commandMessage.getChannel().sendMessage("I have changed your nickname on this server to `" + nick + "`");

            }
        } else {
            commandMessage.getChannel().sendMessage(":x: This name is too long :x:");

        }

    }

    public int calculatePrice(String nick) {
        int price = 0;
        for (char c : nick.toCharArray()) {
            price += 1;
            if (Character.isAlphabetic(c)) {
                if (Character.isLowerCase(c)) {
                    price += 10;
                } else {
                    price += 20;
                }
            } else if (Character.isDigit(c)) {
                price += 25;
            } else if (Character.isWhitespace(c)) {
                price += 50;
            } else {
                price += 40;
            }
        }
        return price;
    }

    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) {
        IUser user = event.getAuthor();
        IGuild server = event.getGuild();
        String message = event.getMessage().getContent();
        Iterator<NickChangeConfirmation> iterator = confirms.iterator();
        while (iterator.hasNext()) {
            NickChangeConfirmation c = iterator.next();
            if (c.getChannel().equals(event.getChannel()) && user.equals(c.getUser())) {
                if (message.toLowerCase().contains("yes")) {
                    if (orangepeel.coinController().getCoinsForUser(user, server) - c.price > 0) {
                        server.setUserNickname(user, c.getNick());
                        orangepeel.coinController().setCoinsForUser(orangepeel.coinController().getCoinsForUser(user, server) - c.price, user, server);
                        JackieChan.sendJackieChanMessage("Transaction complete!", c.getChannel());

                    } else {
                        JackieChan.sendJackieChanMessage(":x: You do not have enough " + CoinController.emote + "coins to buy this! :x:", c.getChannel());
                    }
                    iterator.remove();
                } else if (message.toLowerCase().contains("no")) {
                    JackieChan.sendJackieChanMessage("Transaction canceled!", c.getChannel());
                    iterator.remove();
                } else if (message.startsWith(">")){
                    iterator.remove();
                }

            }
        }
    }

    public class NickChangeConfirmation {
        private IUser user;
        private IGuild guild;
        private String nick;
        private int price;
        private IChannel channel;

        public NickChangeConfirmation(IUser user, IGuild guild, IChannel channel, String nick, int price) {
            this.setChannel(channel);
            this.setPrice(price);
            this.setUser(user);
            this.setGuild(guild);
            this.setNick(nick);
        }

        public IUser getUser() {
            return user;
        }

        public void setUser(IUser user) {
            this.user = user;
        }

        public IGuild getGuild() {
            return guild;
        }

        public void setGuild(IGuild guild) {
            this.guild = guild;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public IChannel getChannel() {
            return channel;
        }

        public void setChannel(IChannel channel) {
            this.channel = channel;
        }
    }
}
