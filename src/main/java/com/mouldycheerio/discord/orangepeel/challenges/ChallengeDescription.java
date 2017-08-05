package com.mouldycheerio.discord.orangepeel.challenges;

public class ChallengeDescription {


    private String name;
    private String description;
    private String longdescription;

    public ChallengeDescription(String name, String description) {
        this.name = name;
        this.description = description;
        longdescription = description;
        // TODO Auto-generated constructor stub
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongdescription() {
        return longdescription;
    }

    public void setLongdescription(String longdescription) {
        this.longdescription = longdescription;
    }

}
