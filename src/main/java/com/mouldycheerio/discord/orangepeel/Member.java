package com.mouldycheerio.discord.orangepeel;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class Member {
    private IGuild g;
    private IUser u;

    public Member(IGuild g, IUser u) {
        this.g = g;
        // TODO Auto-generated constructor stub
        this.u = u;
    }

    public IGuild getServer() {
        return g;
    }

    public void setServer(IGuild g) {
        this.g = g;
    }

    public IUser getUser() {
        return u;
    }

    public void setUser(IUser u) {
        this.u = u;
    }
}
