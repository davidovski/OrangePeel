package com.mouldycheerio.discord.orangepeel.commands;

public abstract class OrangePeelAdminCommand extends OrangePeelCommand {
    private int commandlvl = 0;
    private String noPermText = "You can't do that! *(yet)*";


    public int getCommandlvl() {
        return commandlvl;
    }

    public void setCommandlvl(int commandlvl) {
        this.commandlvl = commandlvl;
    }

    public String getNoPermText() {
        return noPermText;
    }

    public void setNoPermText(String noPermText) {
        this.noPermText = noPermText;
    }

}
