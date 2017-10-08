package com.mouldycheerio.discord.orangepeel.commands;

import java.io.File;

import com.mouldycheerio.discord.orangepeel.Logger;
import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.audio.AudioPlayer;

public class SummonCommand extends OrangePeelCommand {
    public SummonCommand() {
        setName("summon");
        setDescription(new CommandDescription("summon", "Add the bot to your current voice channel.", "summon"));
        addAlias("setVoice");
        setCatagory(CommandCatagory.MUSIC);
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        if (!(commandMessage.getAuthor().getPermissionsForGuild(commandMessage.getGuild()).contains(Permissions.MANAGE_WEBHOOKS)
                || commandMessage.getAuthor().getPermissionsForGuild(commandMessage.getGuild()).contains(Permissions.ADMINISTRATOR))) {
            commandMessage.getChannel().sendMessage("Nah, m8, you cant do this! You need to be able to manage webhooks.");
        } else {
            IVoiceChannel voiceChannel = commandMessage.getAuthor().getVoiceStateForGuild(commandMessage.getGuild()).getChannel();

            if (voiceChannel == null) {
                Logger.warn("no voice channeL!");
                return;
            }

            joinandplay(voiceChannel, orangepeel);
            commandMessage.getChannel().sendMessage("i will now stream music in " + voiceChannel.getName());


        }
    }

    public static void joinandplay(IVoiceChannel voiceChannel, OrangePeel orangepeel) {
        voiceChannel.join();



        AudioPlayer audioP = AudioPlayer.getAudioPlayerForGuild(voiceChannel.getGuild());

        File song = new File("music.mp3");

        if (song.exists()) {

            audioP.clear();

            // Play the found song
            try {
                audioP.queue(song);
                audioP.setLoop(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            orangepeel.saveAll();
        } else {
            Logger.warn("404");

        }
    }
}
