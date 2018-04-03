package com.mouldycheerio.discord.orangepeel.commands;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.mouldycheerio.discord.orangepeel.Logger;
import com.mouldycheerio.discord.orangepeel.Member;
import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;

public class ServerAgeScoresCommand extends OrangePeelCommand {
    public java.util.List<Member> games = new ArrayList<Member>();

    public ServerAgeScoresCommand() {
        setName("sss");
        setDescription(new CommandDescription("sss", "sss!", "sss"));
        setCatagory(CommandCatagory.DEBUG);

    }

    @Override
    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        client.getDispatcher().registerListener(this);
        commandMessage.getChannel().sendMessage("heyo!");
        List<IGuild> guilds = orangepeel.getClient().getGuilds();

        Collections.sort(guilds, new Comparator<IGuild>() {

            @Override
            public int compare(IGuild o1, IGuild o2) {
                return (int) (o1.getJoinTimeForUser(client.getOurUser()).toEpochSecond(ZoneOffset.UTC) - o2.getJoinTimeForUser(client.getOurUser()).toEpochSecond(ZoneOffset.UTC));
            }
        });
        String m = "```";
        int size = 10;
        if (args.length > 1) {
            try {
                size = Integer.parseInt(args[1]);
            } catch (Exception e) {
            }
        }
        if (size > client.getGuilds().size()) {
            size = client.getGuilds().size();
        }
        for (int i = 0; i < size; i++) {
            IGuild g = guilds.get(i);
            if (g == null) {
                break;
            } else {
                m = m + (i+1) + "|" + g.getJoinTimeForUser(client.getOurUser()).format(DateTimeFormatter.ISO_DATE_TIME) + " -> " + g.getName() + " (" + g.getUsers().size()+ ")\n";
            }
        }
        m = m + "```";

       commandMessage.getChannel().sendMessage(m);

        Logger.info("found");
    }


}
