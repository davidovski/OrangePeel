package com.mouldycheerio.discord.orangepeel.commands;

public abstract class OrangePeelAdminCommand extends OrangePeelCommand {
    private int commandlvl = 0;



    public int getCommandlvl() {
        return commandlvl;
    }

    public void setCommandlvl(int commandlvl) {
        this.commandlvl = commandlvl;
    }

}
