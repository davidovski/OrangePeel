package com.mouldycheerio.discord.orangepeel;

import java.awt.Color;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;

import com.mouldycheerio.discord.orangepeel.challenges.Challenge;
import com.mouldycheerio.discord.orangepeel.challenges.ChallengeStatus;
import com.mouldycheerio.discord.orangepeel.challenges.OrangePeelChallenge;
import com.mouldycheerio.discord.orangepeel.commands.HangManCommand;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.guild.GuildLeaveEvent;
import sx.blah.discord.handle.impl.events.guild.channel.ChannelCreateEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserLeaveEvent;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelJoinEvent;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelLeaveEvent;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelMoveEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.RequestBuffer;

public class EventListener {
    private String prefix;
    private OrangePeel orangePeel;
    private CommandController commandController;
    private String ourmention = "<@306115875784622080>";
    private String ourmention2 = "<@!306115875784622080>";

    public EventListener(String prefix, OrangePeel orangePeel) {
        this.prefix = prefix;

        this.orangePeel = orangePeel;
        commandController = new CommandController(orangePeel);

    }


    @EventSubscriber
    public void onUserVoiceChannelJoinEvent(UserVoiceChannelJoinEvent event) {
        IRole r = orangePeel.getConfig(event.getGuild()).getRoleForChannel(event.getVoiceChannel());
        if (r != null) {
            RequestBuffer.request(() -> {
                try {
                    event.getUser().addRole(r);
                } catch (MissingPermissionsException e) {
                    e.printStackTrace();
                } catch (RateLimitException e) {
                    throw e;
                }
            });
        }
    }

    @EventSubscriber
    public void onUserVoiceChannelLeaveEvent(UserVoiceChannelLeaveEvent event) {
        IRole r = orangePeel.getConfig(event.getGuild()).getRoleForChannel(event.getVoiceChannel());
        if (r != null) {
            RequestBuffer.request(() -> {
                try {
                    event.getUser().removeRole(r);
                } catch (MissingPermissionsException e) {
                    e.printStackTrace();
                } catch (RateLimitException e) {
                    throw e;
                }
            });
        }
    }

    @EventSubscriber
    public void onUserVoiceChannelMoveEvent(UserVoiceChannelMoveEvent event) {
        IRole r = orangePeel.getConfig(event.getGuild() ).getRoleForChannel(event.getOldChannel());
        IRole r2 = orangePeel.getConfig(event.getGuild()).getRoleForChannel(event.getNewChannel());
        if (r2 != null && r != null && r.getStringID().equals(r2.getStringID())) {
            return;
        }
        if (r != null) {

            RequestBuffer.request(() -> {
                try {
                    event.getUser().removeRole(r);
                } catch (MissingPermissionsException e) {
                    e.printStackTrace();
                } catch (RateLimitException e) {
                    throw e;
                }
            });
        }
        if (r2 != null) {
            RequestBuffer.request(() -> {
                try {
                    event.getUser().addRole(r2);
                } catch (MissingPermissionsException e) {
                    e.printStackTrace();
                } catch (RateLimitException e) {
                    throw e;
                }
            });
        }
    }


    @EventSubscriber
    public void onChannelCreateEvent(ChannelCreateEvent event) {
        if (orangePeel.getConfig(event.getGuild()).hasMutedRole()) {
            EnumSet<Permissions> toremove = EnumSet.of(Permissions.SEND_MESSAGES, Permissions.ADD_REACTIONS, Permissions.VOICE_SPEAK, Permissions.VOICE_CONNECT);

            EnumSet<Permissions> toadd = EnumSet.noneOf(Permissions.class);
            event.getChannel().overrideRolePermissions(orangePeel.getConfig(event.getGuild()).getMutedRole(), toadd, toremove);
        }
    }

    @EventSubscriber
    public void onGuildLeaveEvent(GuildLeaveEvent event) throws InterruptedException {
            IGuild g = event.getGuild();
            if (orangePeel.getLogChannel() != null) {
                IChannel logChannel = orangePeel.getLogChannel();
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.withAuthorName("Left guild!");
                embedBuilder.withTitle(g.getName()+ " (" + g.getStringID() + ")");
                embedBuilder.withThumbnail(g.getIconURL());

                embedBuilder.withColor(new Color(54, 57, 62));
                logChannel.sendMessage(embedBuilder.build());
        }
    }


    @EventSubscriber
    public void onGuildCreateEvent(GuildCreateEvent event) throws InterruptedException {
        if (orangePeel.getUptime() > 10 * 60 * 1000) {
            IGuild g = event.getGuild();
            if (orangePeel.getLogChannel() != null) {
                IChannel logChannel = orangePeel.getLogChannel();
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.withAuthorName("Joined guild!");
                embedBuilder.withTitle(g.getName()+ " (" + g.getStringID() + ")");
                embedBuilder.withThumbnail(g.getIconURL());
                embedBuilder.appendField("Owner", g.getOwner().getName() + "#" + g.getOwner().getDiscriminator() + " (" + g.getOwner().getStringID() + ")", true);
                embedBuilder.appendField("Users", g.getUsers().size() + "", true);
                embedBuilder.appendField("Creation Date", g.getCreationDate().format(DateTimeFormatter.BASIC_ISO_DATE) + "", true);

                embedBuilder.withColor(new Color(54, 57, 62));
                logChannel.sendMessage(embedBuilder.build());
            }
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


        orangePeel.updatePlaying();
        orangePeel.loadConfigs();
        if (orangePeel.getLogChannel() != null) {
            IChannel logChannel = orangePeel.getLogChannel();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.withTitle("Orange Peel is ready!");
            embedBuilder.withTimestamp(System.currentTimeMillis());
            embedBuilder.withDesc("Orange Peel has logged in as: " + orangePeel.getClient().getOurUser().getName() + "#" + orangePeel.getClient().getOurUser().getDiscriminator());
            embedBuilder.appendField("Custom Commands Loaded", orangePeel.getCmdadded() + "", true);
            embedBuilder.appendField("Challenges Loaded", orangePeel.getChallengeController().getChallanges().size() + "", true);

            embedBuilder.appendField("ID", orangePeel.getClient().getOurUser().getStringID(), true);
            // LocalDateTime creationDate = orangePeel.getClient().getOurUser().getCreationDate();
            // embedBuilder.appendField("Created", creationDate.getDayOfMonth() + "/" + creationDate.getMonthValue() + "/" + creationDate.getYear(), true);

            embedBuilder.withColor(new Color(54, 57, 62));
            logChannel.sendMessage(embedBuilder.build());
        }
    }

    @EventSubscriber
    public void onUserLeaveEvent(UserLeaveEvent event) {
        if (orangePeel.getConfig(event.getGuild()).hasGreetChannel()) {
            orangePeel.getConfig(event.getGuild()).getGreetChannel()
                    .sendMessage(orangePeel.getConfig(event.getGuild()).getLeaveMessage(event.getGuild(), event.getUser()));
        }
    }

    @EventSubscriber
    public void onUserJoinEvent(UserJoinEvent event) throws InterruptedException {
        orangePeel.getConfig(event.getGuild()).reload();
        orangePeel.coinController().incrementCoins(100, event.getUser(), event.getGuild(), false);
        if (orangePeel.getConfig(event.getGuild()).hasGreetChannel()) {
            orangePeel.getConfig(event.getGuild()).getGreetChannel().sendMessage(orangePeel.getConfig(event.getGuild()).getGreetMessage(event.getGuild(), event.getUser()));
        }

        if (orangePeel.getConfig(event.getGuild()).hasAutoRole()) {
            RequestBuffer.request(() -> {
                try {
                    event.getUser().addRole(orangePeel.getConfig(event.getGuild()).getAutoRole());
                } catch (MissingPermissionsException e) {
                    e.printStackTrace();
                } catch (RateLimitException e) {
                    throw e;
                }
            });
        }

        if (event.getUser().getStringID().equals(orangePeel.getClient().getOurUser().getStringID())) {
            if (!orangePeel.getClient().isReady()) {
                orangePeel.getClient().getDispatcher().waitFor(ReadyEvent.class);
            }
            if (orangePeel.getLogChannel() != null) {
                IChannel logChannel = orangePeel.getLogChannel();
                EmbedBuilder embedBuilder = new EmbedBuilder();
                Logger.info("joined server.");
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
    }

    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) throws Exception {
        orangePeel.coinController().incrementCoins(event.getAuthor(), event.getGuild());

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
