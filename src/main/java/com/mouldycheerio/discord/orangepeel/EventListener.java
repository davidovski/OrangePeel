package com.mouldycheerio.discord.orangepeel;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.obj.IUser;

public class EventListener {
    private String prefix;
    private OrangePeel orangePeel;
    private CommandController commandController;
    private String ourmention;
    private String ourmention2;

    public EventListener(String prefix, OrangePeel orangePeel) {
        this.prefix = prefix;
        this.orangePeel = orangePeel;
        commandController = new CommandController(orangePeel);

    }

    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) {
        IUser user = event.getClient().getOurUser();
        Logger.raw("==========");
        Logger.raw(user.getName() + "#" + user.getDiscriminator());
        Logger.raw(user.getStringID());
        Logger.raw("==========");

        orangePeel.getClient().changePlayingText(orangePeel.getPlayingText());
        ourmention = "<@" + orangePeel.getClient().getOurUser().getStringID() + ">";
        ourmention2 = "<@!" + orangePeel.getClient().getOurUser().getStringID() + ">";


    }

    @EventSubscriber
    public void onReactionAddEvent(ReactionAddEvent event) throws InterruptedException {
            String emoji = event.getReaction().getUnicodeEmoji().getAliases().get(0);
            int number = 0;
            if (emoji.equals("one")) {
                number = 1;
            } else if (emoji.equals("two")) {
                number = 2;
            } else if (emoji.equals("three")) {
                number = 3;
            } else if (emoji.equals("four")) {
                number = 4;
            } else if (emoji.equals("five")) {
                number = 5;
            } else if (emoji.equals("six")) {
                number = 6;
            } else if (emoji.equals("seven")) {
                number = 7;
            } else if (emoji.equals("eight")) {
                number = 8;
            } else if (emoji.equals("nine")) {
                number = 9;
            }

            if (number != 0) {
                orangePeel.xoxYourTurn(event.getMessage(), emoji, event.getReaction());
            }
            for (RPSgame g : orangePeel.getRps()) {
                if (g.onReaction(event)) {
                    break;
                }

            }
    }

    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) throws Exception {


        if (event.getMessage().getContent().startsWith(ourmention) || event.getMessage().getContent().startsWith(ourmention2)) {
            System.out.println("x");
            event.getChannel().sendMessage(orangePeel.getChatsession().think(event.getMessage().getContent().replace(ourmention, "").replace(ourmention2, "")));
        }


//        String g;
//        if (event.getGuild() == null) {
//            g = "DIRECTMESSAGE";
//        } else {
//            g = event.getGuild().getName();
//        }
//        System.out.println("[" + g + "] [" + event.getChannel().getName() + "] <" + event.getAuthor().getName() + "> " + event.getMessage().getContent() );
        if (event.getMessage().getContent().startsWith(prefix)) {
            commandController.onMessageReceivedEvent(event, prefix);
        }
    }

    public CommandController getCommandController() {
        return commandController;
    }

    public void setCommandController(CommandController commandController) {
        this.commandController = commandController;
    }
}
