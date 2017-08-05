package com.mouldycheerio.discord.orangepeel.commands;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.mouldycheerio.discord.orangepeel.OrangePeel;
import com.mouldycheerio.discord.orangepeel.challenges.Challenge;
import com.mouldycheerio.discord.orangepeel.challenges.ChallengeController;
import com.mouldycheerio.discord.orangepeel.challenges.ChallengeStatus;
import com.mouldycheerio.discord.orangepeel.challenges.ChallengeType;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class ChallengesCommand extends OrangePeelCommand {
    public ChallengesCommand() {
        setName("challenges");
        setDescription(new CommandDescription("Challenges", "Hey!", "challenges"));
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        ChallengeController challengeController = orangepeel.getChallengeController();

        IUser u = commandMessage.getAuthor();
        challengeController.giveUserAllChallenges(u);
        List<Challenge> challengesForUser = challengeController.getChallengesForUser(u);
        if (args.length == 1) {
            String list = "`[id]` __Challenges__:\n";

            Collections.sort(challengesForUser, new Comparator<Challenge>() {

                public int compare(Challenge o1, Challenge o2) {
                    return o1.getType().getId() - o2.getType().getId();
                }
            });

            for (Challenge challenge : challengesForUser) {
                list = list + "`[" + challenge.getType().getId() + "]` **" + challenge.getDescription().getName() + "** " + challenge.getDescription().getDescription();
                if (challenge.getStatus() == ChallengeStatus.ACTIVE) {
                    list = list + " `" + Math.round(((double)challenge.getProgress() / (double)challenge.getMaxProgress()) * 100.0) + "%`";
                }
                if (challenge.getStatus() == ChallengeStatus.COMPLETE) {
                    list = list + " :white_check_mark:";
                }
                list = list + "\n";
            }
            commandMessage.getChannel().sendMessage(list + "\nTo start a challenge, please type `>challenge accept [id]"
                    + "\nFor more info about one `>challenge info [id]");
        } else if (args.length == 3) {
            if ("accept".equals(args[1])) {
                challengeController.appointChallenge(u, ChallengeType.getById(Integer.parseInt(args[2])));
                commandMessage.getChannel().sendMessage("You have accpted the challenge: " + ChallengeType.getById(Integer.parseInt(args[2])).toString());
            }
            if ("info".equals(args[1])) {
                ChallengeType type = ChallengeType.getById(Integer.parseInt(args[2]));
                for (Challenge challenge : challengesForUser) {
                    if (challenge.getType() == type) {
                        String m = "`[" + challenge.getType().getId() + "]` __**" + challenge.getDescription().getName() + "**__\n"
                    + challenge.getDescription().getLongdescription() + "\n";
                        if (challenge.getStatus() == ChallengeStatus.ACTIVE) {
                            m = m + "In progress:\n~~**|";
                            int percent = (int) Math.round(((double)challenge.getProgress() / (double)challenge.getMaxProgress()) * 100.0);
                            for (int i = 0; i < 10; i++) {
                                if (i == Math.round(percent / 10)) {
                                    m = m + ":radio_button:";
                                } else {
                                    m = m + "----";
                                }
                            }
                            m = m + "|**~~ " + percent + "%";
                        }
                        commandMessage.getChannel().sendMessage(m);
                    }
                }
            }
        }
    }
}
