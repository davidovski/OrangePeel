package com.mouldycheerio.discord.orangepeel.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IMessage.Attachment;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.audio.AudioPlayer;

public class SetMusicCommand extends OrangePeelAdminCommand {
    public SetMusicCommand() {
        setName("setMusic");
        setDescription(new CommandDescription("setMusic", "Set the music that the bot plays. DOn't forget to attach the file", "setMusic <attach file>"));
        setCommandlvl(3);
        setCatagory(CommandCatagory.BOT_ADMIN);
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {

        List<Attachment> attachments = commandMessage.getAttachments();
        File song = new File("music.mp3");

        if (!attachments.isEmpty()) {
            for (Attachment a : attachments) {
                if (a.getFilename().endsWith(".mp3")) {
                    try {
                        download(song, a.getUrl());
                        commandMessage.getChannel().sendMessage("Now playing **" + a.getFilename() + "** 24/7. Good job.", true);
                    } catch (MalformedURLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (ProtocolException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        } else {

            try {
                download(song, args[1]);
                commandMessage.getChannel().sendMessage("Now playing **" + args[1] + "** 24/7. Good job.", true);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        List<IVoiceChannel> connectedVoiceChannels = client.getConnectedVoiceChannels();
        for (IVoiceChannel voiceChannel : connectedVoiceChannels) {
            AudioPlayer audioP = AudioPlayer.getAudioPlayerForGuild(voiceChannel.getGuild());
            audioP.clear();
            try {
                audioP.queue(song);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            audioP.setLoop(true);
        }

    }

    private static void download(File file, String urlString) throws MalformedURLException, IOException, ProtocolException, FileNotFoundException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        try {
            InputStream in = connection.getInputStream();
            try {
                FileOutputStream out = new FileOutputStream(file);
                try {
                    copy(in, out);
                } finally {
                    out.close();
                }
            } finally {
                in.close();
            }
        } finally {
            connection.disconnect();
        }
    }

    private static byte[] buffer = new byte[10240];

    public static void copy(InputStream input, OutputStream output) throws IOException {
        System.out.println("Copying...");
        int n = input.read(buffer);
        while (n >= 0) {
            output.write(buffer, 0, n);
            n = input.read(buffer);
        }
        output.flush();
        System.out.print(" Copied!");

        // int len;
        // while ((len = zis.read(buffer)) > 0) {
        // fos.write(buffer, 0, len);
        // }
    }
}
