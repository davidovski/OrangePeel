package com.mouldycheerio.discord.orangepeel;

import org.json.JSONObject;

public class StatsCounter {
    private JSONObject stats;
    private OrangePeel op;

    public StatsCounter(JSONObject stats, OrangePeel op) {
        this.op = op;
        this.setStats(stats);
    }

    public int getServers() {
        return op.getClient().getGuilds().size();
    }

    public int getVotes() {
        if (stats.has("votes")) {
            return stats.getInt("votes");
        } else {
            stats.put("votes", 0);
            return stats.getInt("votes");
        }
    }

    public int getUpVotes() {
        if (stats.has("upvotes")) {
            return stats.getInt("upvotes");
        } else {
            stats.put("upvotes", 0);
            return stats.getInt("upvotes");
        }
    }

    public int getAdmins() {
        return op.getAdmins().length();
    }

    public int getDownVotes() {
        if (stats.has("downvotes")) {
            return stats.getInt("downvotes");
        } else {
            stats.put("downvotes", 0);
            return stats.getInt("downvotes");
        }
    }

    public int getRPSplays() {
        if (stats.has("rps-plays")) {
            return stats.getInt("rps-plays");
        } else {
            stats.put("rps-plays", 0);
            return stats.getInt("rps-plays");
        }
    }

    public int getRPSwins() {
        if (stats.has("rps-wins")) {
            return stats.getInt("rps-wins");
        } else {
            stats.put("rps-wins", 0);
            return stats.getInt("rps-wins");
        }
    }
    public int getRPSdraws() {
        if (stats.has("rps-draws")) {
            return stats.getInt("rps-draws");
        } else {
            stats.put("rps-draws", 0);
            return stats.getInt("rps-draws");
        }
    }

    public int getRPSlosses() {
        if (stats.has("rps-losses")) {
            return stats.getInt("rps-losses");
        } else {
            stats.put("rps-losses", 0);
            return stats.getInt("rps-losses");
        }
    }

    public int getXoxGamesPlayed() {
        if (stats.has("xox-plays")) {
            return stats.getInt("xox-plays");
        } else {
            stats.put("xox-plays", 0);
            return stats.getInt("xox-plays");
        }
    }

    public int getXOXlosses() {
        if (stats.has("xox-losses")) {
            return stats.getInt("xox-losses");
        } else {
            stats.put("xox-losses", 0);
            return stats.getInt("xox-losses");
        }
    }


    public int getXOXwins() {
        if (stats.has("xox-wins")) {
            return stats.getInt("xox-wins");
        } else {
            stats.put("xox-wins", 0);
            return stats.getInt("xox-wins");
        }
    }

    public int getPings() {
        if (stats.has("pings")) {
            return stats.getInt("pings");
        } else {
            stats.put("pings", 0);
            return stats.getInt("pings");
        }
    }

    public int getHelps() {
        if (stats.has("helps")) {
            return stats.getInt("helps");
        } else {
            stats.put("helps", 0);
            return stats.getInt("helps");
        }
    }

    public void incrementStat(String stat) {
        if (stats.has(stat)) {
            stats.put(stat, stats.getInt(stat) + 1);
        } else {
            stats.put(stat, 1);
        }
        op.saveAll();
    }

    public int getChannels() {
        return op.getClient().getChannels().size();
    }

    public int getUsers() {
        return op.getClient().getUsers().size();
    }

    public JSONObject getStats() {
        return stats;
    }

    public void setStats(JSONObject stats) {
        this.stats = stats;
    }

}
