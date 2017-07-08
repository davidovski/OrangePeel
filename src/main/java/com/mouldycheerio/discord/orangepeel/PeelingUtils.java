package com.mouldycheerio.discord.orangepeel;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class PeelingUtils {
    public static String mentionToId(String mention, IGuild server) {
        String id = "";
        for (char c : mention.toCharArray()) {
            if (Character.isDigit(c)) {
                id = id + c;
            }
        }
        if (id.length() <= 4) {

            for (IUser u : server.getUsers()) {
              if (u.getDiscriminator().equals(id) && u.getName().equals((mention.split("#")[0]))) {
                  id = u.getStringID();
                  break;
              }
            }
        }
        return id;
    }
}
