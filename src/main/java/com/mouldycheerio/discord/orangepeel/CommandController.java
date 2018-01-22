package com.mouldycheerio.discord.orangepeel;

import java.util.ArrayList;
import java.util.List;

import com.mouldycheerio.discord.orangepeel.challenges.ChallengesCommand;
import com.mouldycheerio.discord.orangepeel.commands.AddCustomCommand;
import com.mouldycheerio.discord.orangepeel.commands.AddPerServerCustomCommand;
import com.mouldycheerio.discord.orangepeel.commands.AnnounceCommand;
import com.mouldycheerio.discord.orangepeel.commands.ArtCommand;
import com.mouldycheerio.discord.orangepeel.commands.AsciiCommand;
import com.mouldycheerio.discord.orangepeel.commands.BotBanCommand;
import com.mouldycheerio.discord.orangepeel.commands.ChannelsCommand;
import com.mouldycheerio.discord.orangepeel.commands.ChopperCommand;
import com.mouldycheerio.discord.orangepeel.commands.Command;
import com.mouldycheerio.discord.orangepeel.commands.CommandCatagory;
import com.mouldycheerio.discord.orangepeel.commands.CpuCommand;
import com.mouldycheerio.discord.orangepeel.commands.DownVoteCommand;
import com.mouldycheerio.discord.orangepeel.commands.EmbedCommand;
import com.mouldycheerio.discord.orangepeel.commands.EndGamesCommand;
import com.mouldycheerio.discord.orangepeel.commands.EvalCommand;
import com.mouldycheerio.discord.orangepeel.commands.FakeUserCommand;
import com.mouldycheerio.discord.orangepeel.commands.GeoLocateCommand;
import com.mouldycheerio.discord.orangepeel.commands.GetIDCommand;
import com.mouldycheerio.discord.orangepeel.commands.GiveAdminCommand;
import com.mouldycheerio.discord.orangepeel.commands.HelpCommand;
import com.mouldycheerio.discord.orangepeel.commands.HeyCommand;
import com.mouldycheerio.discord.orangepeel.commands.ImageCommand;
import com.mouldycheerio.discord.orangepeel.commands.LoadAllCommand;
import com.mouldycheerio.discord.orangepeel.commands.LogCommand;
import com.mouldycheerio.discord.orangepeel.commands.MirrorMirrorCommand;
import com.mouldycheerio.discord.orangepeel.commands.NicknameCommand;
import com.mouldycheerio.discord.orangepeel.commands.OrangePeelAdminCommand;
import com.mouldycheerio.discord.orangepeel.commands.PerServerCustomCmd;
import com.mouldycheerio.discord.orangepeel.commands.QuickPlayCommand;
import com.mouldycheerio.discord.orangepeel.commands.RateCommand;
import com.mouldycheerio.discord.orangepeel.commands.RebootCommand;
import com.mouldycheerio.discord.orangepeel.commands.ReloadCommand;
import com.mouldycheerio.discord.orangepeel.commands.ReportBugCommand;
import com.mouldycheerio.discord.orangepeel.commands.ResponseTimeCommand;
import com.mouldycheerio.discord.orangepeel.commands.SaveAllCommand;
import com.mouldycheerio.discord.orangepeel.commands.SendFileCommand;
import com.mouldycheerio.discord.orangepeel.commands.ServersCommand;
import com.mouldycheerio.discord.orangepeel.commands.SetChannelCommand;
import com.mouldycheerio.discord.orangepeel.commands.SetMusicCommand;
import com.mouldycheerio.discord.orangepeel.commands.SetPlayingTextCommand;
import com.mouldycheerio.discord.orangepeel.commands.SetVotesCommand;
import com.mouldycheerio.discord.orangepeel.commands.ShutdownCommand;
import com.mouldycheerio.discord.orangepeel.commands.SimpleCustomCmd;
import com.mouldycheerio.discord.orangepeel.commands.StatsCommand;
import com.mouldycheerio.discord.orangepeel.commands.StoryCommand;
import com.mouldycheerio.discord.orangepeel.commands.StreamCommand;
import com.mouldycheerio.discord.orangepeel.commands.SuggestionCommand;
import com.mouldycheerio.discord.orangepeel.commands.SummonCommand;
import com.mouldycheerio.discord.orangepeel.commands.TopCommand;
import com.mouldycheerio.discord.orangepeel.commands.UpTimeCommand;
import com.mouldycheerio.discord.orangepeel.commands.VoteCommand;
import com.mouldycheerio.discord.orangepeel.commands.VotesCommand;
import com.mouldycheerio.discord.orangepeel.commands.WahCommand;
import com.mouldycheerio.discord.orangepeel.commands.WaveCommand;
import com.mouldycheerio.discord.orangepeel.commands.coin.BalanceCommand;
import com.mouldycheerio.discord.orangepeel.commands.coin.JackieChan;
import com.mouldycheerio.discord.orangepeel.commands.coin.PayCommand;
import com.mouldycheerio.discord.orangepeel.commands.coin.ShopCommand;
import com.mouldycheerio.discord.orangepeel.commands.debug.ReactCommand;
import com.mouldycheerio.discord.orangepeel.commands.moderation.AutoRoleCommand;
import com.mouldycheerio.discord.orangepeel.commands.moderation.BanCommand;
import com.mouldycheerio.discord.orangepeel.commands.moderation.BanHammerCommand;
import com.mouldycheerio.discord.orangepeel.commands.moderation.KickCommand;
import com.mouldycheerio.discord.orangepeel.commands.moderation.MuteCommand;
import com.mouldycheerio.discord.orangepeel.commands.moderation.NickAllCommand;
import com.mouldycheerio.discord.orangepeel.commands.moderation.PurgeCommand;
import com.mouldycheerio.discord.orangepeel.commands.moderation.SetupCommand;
import com.mouldycheerio.discord.orangepeel.commands.moderation.SetupMuteCommand;
import com.mouldycheerio.discord.orangepeel.commands.moderation.UnBanCommand;
import com.mouldycheerio.discord.orangepeel.commands.moderation.UnMuteCommand;
import com.mouldycheerio.discord.orangepeel.games.RPScommand;
import com.mouldycheerio.discord.orangepeel.games.XOXCommand;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandController {
    private List<Command> commands;
    private OrangePeel orangePeel;
    private SimpleCustomCmd toadd;

    public CommandController(OrangePeel orangePeel) {
        this.orangePeel = orangePeel;
        commands = new ArrayList<Command>();
        commands.add(new EmbedCommand());

        commands.add(new HeyCommand());
        commands.add(new GetIDCommand());

        commands.add(new UpTimeCommand());
        commands.add(new VoteCommand());
        commands.add(new MuteCommand());
        commands.add(new UnMuteCommand());

        commands.add(new BanHammerCommand());

        commands.add(new KickCommand());
        commands.add(new BanCommand());
        commands.add(new UnBanCommand());
        commands.add(new PurgeCommand());

        commands.add(new DownVoteCommand());
        commands.add(new ChopperCommand());
        commands.add(new SetupCommand());
        commands.add(new SetupMuteCommand());
        commands.add(new AutoRoleCommand());

        commands.add(new ResponseTimeCommand());
        commands.add(new XOXCommand(orangePeel.getClient()));
        commands.add(new RPScommand());
        commands.add(new TopCommand());
        commands.add(new StatsCommand());
        commands.add(new VotesCommand());
        commands.add(new ReportBugCommand());
        commands.add(new SuggestionCommand());
        commands.add(new VotesCommand());
        commands.add(new MirrorMirrorCommand());
        commands.add(new StoryCommand());
        commands.add(new RateCommand());
        commands.add(new ChallengesCommand());

        commands.add(new NickAllCommand());
        commands.add(new ReactCommand());

        commands.add(new AddPerServerCustomCommand());

        commands.add(new SummonCommand());
        commands.add(new QuickPlayCommand());

        commands.add(new ImageCommand());
        commands.add(new ArtCommand());
        commands.add(new JackieChan());
        commands.add(new ServersCommand());
        commands.add(new FakeUserCommand());
        commands.add(new ChannelsCommand());

        commands.add(new WaveCommand());
        commands.add(new WahCommand());
        commands.add(new AsciiCommand());
        commands.add(new GeoLocateCommand());

        commands.add(new EvalCommand());
        commands.add(new CpuCommand());

        commands.add(new AddCustomCommand());
        commands.add(new SetChannelCommand());
        commands.add(new NicknameCommand());
        commands.add(new StreamCommand());
        commands.add(new LogCommand());

        commands.add(new SetVotesCommand());
        commands.add(new SetPlayingTextCommand());
        commands.add(new SetMusicCommand());
        commands.add(new BotBanCommand());
        commands.add(new AnnounceCommand());
        commands.add(new EndGamesCommand());
        commands.add(new LoadAllCommand());
        commands.add(new SaveAllCommand());
        commands.add(new GiveAdminCommand());
        commands.add(new ReloadCommand());
        commands.add(new SendFileCommand());

        commands.add(new BalanceCommand());
        commands.add(new PayCommand());
        commands.add(new ShopCommand());

        commands.add(new ShutdownCommand());
        commands.add(new RebootCommand());
        commands.add(new SimpleCustomCmd(">>", "<<-->>", "<<<"));
        SimpleCustomCmd e = new SimpleCustomCmd("invite", "Invite me!!", "Add me to your own server! https://goo.gl/ZcLxNJ");
        e.setCatagory(CommandCatagory.ABOUT);
        commands.add(e);
        commands.add(new HelpCommand(commands));

    }

    public void onMessageReceivedEvent(MessageReceivedEvent event, String prefix) {
        if (!orangePeel.getBanned().contains(event.getAuthor().getLongID())) {
            String msg = event.getMessage().getContent();
            String[] parts = msg.split(" ");
            String commandname = parts[0].substring(prefix.length());
            for (Command c : commands) {
                boolean isCommand = commandname.equalsIgnoreCase(c.getName());
                for (String string : c.getAlias()) {
                    if (commandname.equalsIgnoreCase(string)) {
                        isCommand = true;
                        break;
                    }
                }
                if (isCommand) {
                    if (c instanceof OrangePeelAdminCommand) {
                        if (orangePeel.getAdmins().has(event.getAuthor().getStringID())) {
                            if (((OrangePeelAdminCommand) c).getCommandlvl() <= orangePeel.getAdmins().getInt(event.getAuthor().getStringID())) {
                                c.onCommand(orangePeel, orangePeel.getClient(), event.getMessage(), parts);
                                orangePeel.coinController().incrementCoins(4, event.getAuthor(), event.getGuild(), false);
                                orangePeel.getStatsCounter().incrementStat("commands");
                            } else {
                                event.getMessage().reply(((OrangePeelAdminCommand) c).getNoPermText());
                            }
                        } else {
                            event.getMessage().reply(((OrangePeelAdminCommand) c).getNoPermText());

                        }
                    } else {
                        c.onCommand(orangePeel, orangePeel.getClient(), event.getMessage(), parts);
                        MetricsSystem.logCommand(event.getAuthor().getLongID(), c.getName(), event.getGuild().getLongID());
                        if (!c.getName().equals("balance")) {
                            orangePeel.coinController().incrementCoins(4, event.getAuthor(), event.getGuild(), false);
                        }
                        orangePeel.getStatsCounter().incrementStat("commands");
                    }
                }
            }
            if (toadd != null) {
                boolean match = false;
                for (Command command : commands) {
                    if (command.getName().equals(toadd.getName())) {
                        if (command instanceof PerServerCustomCmd) {
                            ((PerServerCustomCmd) command).setText(toadd.getText());
                            match = true;
                            break;
                        }
                    }
                }
                if (match == false) {
                    commands.add(toadd);
                }
                toadd = null;
            }
        }
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }

    public void setToadd(SimpleCustomCmd toadd) {
        this.toadd = toadd;
    }
}
