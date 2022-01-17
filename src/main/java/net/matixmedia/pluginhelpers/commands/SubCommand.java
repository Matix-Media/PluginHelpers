package net.matixmedia.pluginhelpers.commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public interface SubCommand {
    public boolean onCommand(Player sender, Command cmd, String commandLabel, String[] args);

    public String getHelp();

    public String[] getArguments();
}
