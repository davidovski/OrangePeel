package com.mouldycheerio.discord.orangepeel.commands;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class MirrorMirrorCommand extends OrangePeelCommand {
    public MirrorMirrorCommand() {
        setName("mirror");
        setDescription(new CommandDescription("Mirror Mirror on the wall...", "who is the fairest of them all?", "mirror"));
        setCatagory(CommandCatagory.FUN);
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {

        IMessage s = commandMessage.getChannel()
                .sendMessage("```markup\n" + commandMessage.getAuthor().getName() + ": Magic Mirror, on the wall, who is the fairest one of all? ```");
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            orangepeel.logError(e,commandMessage);
        }

        if (Math.random() > 0.9) {
            s.edit("```markup\n" + commandMessage.getAuthor().getName() + ": Magic Mirror, on the wall, who is the fairest one of all? \n"
                    + "Magic Mirror: davidovski, of course!```");
        } else {
            if (Math.random() > 0.7) {
                s.edit("```markup\n" + commandMessage.getAuthor().getName() + ": Magic Mirror, on the wall, who is the fairest one of all? \n"
                        + "Magic Mirror: Me, of course, you don't stand a chance!```");
            } else {
                s.edit("```markup\n" + commandMessage.getAuthor().getName() + ": Magic Mirror, on the wall, who is the fairest one of all? \n"
                        + "Magic Mirror: You, my friend, are the fairest of them all!```");
            }
        }
    }
}
