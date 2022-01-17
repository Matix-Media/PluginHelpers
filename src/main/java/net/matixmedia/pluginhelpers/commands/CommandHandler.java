package net.matixmedia.pluginhelpers.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.regex.Pattern;

public class CommandHandler implements CommandExecutor, TabCompleter {

    private static final Pattern SPACED_ARG_PATTERN = Pattern.compile("\"?( |$)(?=(([^\"]*\"){2})*[^\"]*$)\"?");

    private HashMap<String, SubCommand> commands = new HashMap<>();
    private SubCommand baseCommand;
    private final String prefix;

    public CommandHandler(String prefix) {
        this.prefix = prefix;
    }

    private String getStringFromArray(String[] array) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < array.length; i++) {
            sb.append(" ").append(array[i]);
        }
        return sb.toString();
    }

    private String generateHelp(String label) {
        StringBuilder help = new StringBuilder("\n" + getPrefix() + ChatColor.GRAY + "-== CompassWaypoints help ==-- \n");

        for (Map.Entry<String, SubCommand> command : commands.entrySet()) {
            help.append(getPrefix()).append(ChatColor.YELLOW).append("/").append(label).append(" ")
                    .append(command.getKey()).append(getStringFromArray(command.getValue().getArguments()))
                    .append(ChatColor.GRAY).append(" - ").append(command.getValue().getHelp()).append("\n");
        }

        help.append(" ");

        return help.toString();
    }

    public void register(String name, SubCommand cmd) {
        commands.put(name, cmd);
    }

    public void registerBaseCommand(SubCommand cmd) {
        baseCommand = cmd;
    }

    public boolean exists(String name) {
        return commands.containsKey(name);
    }

    public SubCommand getExecutor(String name) {
        return commands.get(name);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        args = SPACED_ARG_PATTERN.split(String.join(" ", args).replaceAll("^\"", ""));

        if (sender instanceof Player) {
            if (args.length == 0) {
                return baseCommand.onCommand((Player) sender, command, label, args);
            }
            if (args[0].equals("help")) {
                sender.sendMessage(generateHelp(label));
                return true;
            }
            if (exists(args[0])) {
                SubCommand subCommand = getExecutor(args[0]);
                if (subCommand.getArguments().length > args.length - 1) {
                    sender.sendMessage(getPrefix() + ChatColor.RED + "/" + label + " " + args[0] + getStringFromArray(subCommand.getArguments()));
                    return true;
                }
                return subCommand.onCommand((Player) sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            } else {
                sender.sendMessage(generateHelp(label));
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You need to be a player to use this command.");
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>(commands.keySet());
            completions.add("help");
            return completions;
        }
        return null;
    }

    public String getPrefix() {
        return this.prefix + " " + ChatColor.RESET;
    }
}
