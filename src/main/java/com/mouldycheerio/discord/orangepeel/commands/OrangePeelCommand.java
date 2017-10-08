package com.mouldycheerio.discord.orangepeel.commands;

import java.util.ArrayList;
import java.util.List;

public abstract class OrangePeelCommand implements Command {

    private CommandCatagory catagory = CommandCatagory.OTHER;
    private String name;
    private CommandDescription description;
    private List<String> alias;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CommandDescription getDescription() {
        return description;
    }

    public void setDescription(CommandDescription description) {
        this.description = description;
    }

    public List<String> getAlias() {
        if (alias == null) {
            alias = new ArrayList<String>();
        }
        return alias;
    }

    public void addAlias(String alias) {
        getAlias().add(alias);
    }

    public void setAlias(List<String> aliases) {
        this.alias = alias;
    }

    public CommandCatagory getCatagory() {
        return catagory;
    }

    public void setCatagory(CommandCatagory catagory) {
        this.catagory = catagory;
    }
}
