package com.mouldycheerio.discord.web;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IVoiceChannel;

public class MagicChannel {
    private IVoiceChannel voiceChannel;
    private IRole role;

    public MagicChannel(IVoiceChannel voiceChannel, IRole role ) {
        this.voiceChannel = voiceChannel;
        this.role = role;
    }

    public MagicChannel(IDiscordClient client, String vc, String r ) {
        voiceChannel = client.getVoiceChannelByID(Long.parseLong(vc));
        role = client.getRoleByID(Long.parseLong(r));

    }

    public IRole getRole() {
        return role;
    }
    public void setRole(IRole role) {
        this.role = role;
    }
    public IVoiceChannel getVoiceChannel() {
        return voiceChannel;
    }
    public void setVoiceChannel(IVoiceChannel voiceChannel) {
        this.voiceChannel = voiceChannel;
    }

}
