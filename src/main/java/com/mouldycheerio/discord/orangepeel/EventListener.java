package com.mouldycheerio.discord.orangepeel;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.EnumSet;

import com.mouldycheerio.discord.orangepeel.challenges.Challenge;
import com.mouldycheerio.discord.orangepeel.challenges.ChallengeStatus;
import com.mouldycheerio.discord.orangepeel.challenges.OrangePeelChallenge;
import com.mouldycheerio.discord.orangepeel.commands.HangManCommand;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.guild.channel.ChannelCreateEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserLeaveEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;

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
    public void onChannelCreateEvent(ChannelCreateEvent event) {
        if (orangePeel.getMuted().containsKey(event.getGuild().getStringID())) {
            EnumSet<Permissions> toremove = EnumSet.of(Permissions.SEND_MESSAGES);
            EnumSet<Permissions> toadd = EnumSet.noneOf(Permissions.class);
            event.getChannel().overrideRolePermissions(event.getGuild().getRoleByID(Long.parseLong(orangePeel.getMuted().get(event.getGuild().getStringID()))), toadd, toremove);
        }
    }

    @EventSubscriber
    public void onInviteEvent(GuildCreateEvent event) throws InterruptedException {
        if (!orangePeel.getClient().isReady()) {
            orangePeel.getClient().getDispatcher().waitFor(ReadyEvent.class);
        }
        if (orangePeel.getLogChannel() != null) {
            IChannel logChannel = orangePeel.getLogChannel();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.withTitle("Joined Server!");
            embedBuilder.withDescription(event.getGuild().getName());
            embedBuilder.withThumbnail(event.getGuild().getIconURL());
            embedBuilder.withColor(new Color(54, 57, 62));
            embedBuilder.withAuthorName("discord");
            embedBuilder.appendField("ID", event.getGuild().getStringID(), true);

            embedBuilder.appendField("Members", " " + event.getGuild().getUsers().size(), true);
            embedBuilder.appendField("Channels", " " + event.getGuild().getChannels().size(), true);
            embedBuilder.appendField("Voice Channels", " " + event.getGuild().getVoiceChannels().size(), true);

            embedBuilder.appendField("Owner", " " + event.getGuild().getOwner().getName() + "#" + event.getGuild().getOwner().getDiscriminator(), false);
            embedBuilder.appendField("CreationDate", event.getGuild().getCreationDate().getDayOfMonth() + "/" + event.getGuild().getCreationDate().getMonthValue() + "/"
                    + event.getGuild().getCreationDate().getYear(), false);

            logChannel.sendMessage(embedBuilder.build());
        } else {
            System.out.println("Loaded server: " + event.getGuild().getName());
        }

    }

    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) {
        IUser user = event.getClient().getOurUser();
        Logger.raw("==========");
        Logger.raw(user.getName() + "#" + user.getDiscriminator());
        Logger.raw(user.getStringID());
        Logger.raw("==========");

        commandController.getCommands().add(new HangManCommand(orangePeel.getClient()));
        orangePeel.getClient().changePlayingText(orangePeel.getPlayingText());
        ourmention = "<@" + orangePeel.getClient().getOurUser().getStringID() + ">";
        ourmention2 = "<@!" + orangePeel.getClient().getOurUser().getStringID() + ">";
        orangePeel.loadAll();
        orangePeel.getStatsCounter().incrementStat("boots");

        if (orangePeel.getLogChannel() != null) {
            IChannel logChannel = orangePeel.getLogChannel();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.withTitle("Orange Peel is ready!");
            embedBuilder.withTimestamp(System.currentTimeMillis());
            embedBuilder.withDesc("Orange Peel has logged in as:");
            embedBuilder.appendField("Name", orangePeel.getClient().getOurUser().getName() + "#" + orangePeel.getClient().getOurUser().getDiscriminator(), true);
            embedBuilder.appendField("ID", orangePeel.getClient().getOurUser().getStringID(), true);
            LocalDateTime creationDate = orangePeel.getClient().getOurUser().getCreationDate();
            embedBuilder.appendField("Created", creationDate.getDayOfMonth() + "/" + creationDate.getMonthValue() + "/" + creationDate.getYear(), true);
            embedBuilder.withColor(new Color(54, 57, 62));
            logChannel.sendMessage(embedBuilder.build());
        }

    }

    @EventSubscriber
    public void onUserLeaveEvent(UserLeaveEvent event) {
        if (orangePeel.getGreet().containsKey(event.getGuild().getStringID())) {
            event.getGuild().getChannelByID(Long.parseLong(orangePeel.getGreet().get(event.getGuild().getStringID())))
                    .sendMessage("Aww, " + event.getUser().getName() + "#" + event.getUser().getDiscriminator() + "  has just left! :cry:");
        }
    }

    @EventSubscriber
    public void onUserJoinEvent(UserJoinEvent event) {
        if (orangePeel.getGreet().containsKey(event.getGuild().getStringID())) {
            event.getGuild().getChannelByID(Long.parseLong(orangePeel.getGreet().get(event.getGuild().getStringID())))
                    .sendMessage("Welcome, <@" + event.getUser().getStringID() + ">  to " + event.getGuild().getName() + "! :joy:");
        }

        if (orangePeel.getAutoRole().containsKey(event.getGuild().getStringID())) {
            event.getUser().addRole(event.getGuild().getRoleByID(Long.parseLong(orangePeel.getAutoRole().get(event.getGuild().getStringID()))));
        }
    }

    @EventSubscriber
    public void onReactionAddEvent(ReactionAddEvent event) throws InterruptedException {
        if (!event.getReaction().isCustomEmoji() && event.getReaction().getUnicodeEmoji().getAliases().size() > 0) {
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
    }

    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) throws Exception {

        if (event.getMessage().getContent().length() > 1) {
            Writer output = new BufferedWriter(new FileWriter("messages", true)); // clears file every time
            output.append(event.getAuthor().getName() + ": " + event.getMessage().getContent() + "\n");
            output.close();
        }

        for (Challenge challenge : orangePeel.getChallengeController().getChallanges()) {
            if (challenge instanceof OrangePeelChallenge && challenge.getStatus() == ChallengeStatus.ACTIVE) {
                ((OrangePeelChallenge) challenge).onMessage(event);
            }
        }
        orangePeel.getStatsCounter().incrementStat("messages");
        if (event.getMessage().getContent().startsWith(ourmention) || event.getMessage().getContent().startsWith(ourmention2)) {
            System.out.println("x");
            event.getChannel().sendMessage(orangePeel.getChatsession().think(event.getMessage().getContent().replace(ourmention, "").replace(ourmention2, "")));
        }

        // String g;
        // if (event.getGuild() == null) {
        // g = "DIRECTMESSAGE";
        // } else {
        // g = event.getGuild().getName();
        // }
        // System.out.println("[" + g + "] [" + event.getChannel().getName() + "] <" + event.getAuthor().getName() + "> " + event.getMessage().getContent() );
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
