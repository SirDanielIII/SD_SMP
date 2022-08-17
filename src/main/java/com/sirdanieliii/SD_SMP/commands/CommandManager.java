package com.sirdanieliii.SD_SMP.commands;

import com.sirdanieliii.SD_SMP.commands.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sirdanieliii.SD_SMP.configuration.ConfigManager.smpName;

public class CommandManager implements TabExecutor {
    static Map<String, List<SubCommand>> subcommands = new HashMap<>();
    List<SubCommand> ivan = new ArrayList<>();
    List<SubCommand> coords = new ArrayList<>();
    List<SubCommand> death = new ArrayList<>();

    public CommandManager() {
        ivan.add(new ivanDog());
        ivan.add(new ivanDonkey());
        coords.add(new coordsClear());
        coords.add(new coordsList());
        coords.add(new coordsSend());
        coords.add(new coordsSet());
        death.add(new deathKDR());
        death.add(new deathKills());
        death.add(new deathNonPlayer());
        death.add(new deathPlayer());
        death.add(new deathTotal());
        subcommands.put("ivan", ivan);
        subcommands.put("death", death);
        subcommands.put("coords", coords);
    }

    public static ArrayList<SubCommand> getSubcommands(String key) {
        return (ArrayList<SubCommand>) subcommands.get(key);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;
        if (args.length > 0) {
            try {
                for (int i = 0; i < getSubcommands(cmd.getName()).size(); i++)  // Loop through ArrayList of subcommands
                    if (args[0].equalsIgnoreCase(getSubcommands(cmd.getName()).get(i).getName()))  // Check for any command family matches
                        getSubcommands(cmd.getName()).get(i).perform(player, args);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else displaySubCommands(player, label);  // Print all subcommands of command family (no arguments given in command; just the label)
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1) { // E.G. /command [TAB-COMPLETE]
            ArrayList<String> subcommandArgs = new ArrayList<>();
            for (int i = 0; i < getSubcommands(cmd.getName()).size(); i++)
                subcommandArgs.add(getSubcommands(cmd.getName()).get(i).getName());  // Get subcommand names as tab-complete
            return subcommandArgs;
        } else if (args.length >= 2) { // /command subcommand [TAB-COMPLETE]
            for (int i = 0; i < getSubcommands(cmd.getName()).size(); i++) {
                if (args[0].equalsIgnoreCase(getSubcommands(cmd.getName()).get(i).getName())) {
                    return getSubcommands(cmd.getName()).get(i).getSubcommandArgs((Player) sender, args);  // Get matching subcommand's tab-complete
                }
            }
        }
        return null;
    }

    public static String cmdHeader(String type) {
        return switch (type.toLowerCase()) {
            case ("coords") -> "§6[§FCoords§6] ";
            case ("death") -> "§4[§FDeath§4] ";
            case ("ivan") -> "§B[§FIvan§B] ";
            default -> null;
        };
    }

    public static String cmdHeaderClr(String type, boolean bold) {
        if (bold) {
            return switch (type.toLowerCase()) {
                case ("coords") -> "§6§L";
                case ("death") -> "§4§L";
                case ("ivan") -> "§B§L";
                default -> null;
            };
        } else {
            return switch (type.toLowerCase()) {
                case ("coords") -> "§6";
                case ("death") -> "§4";
                case ("ivan") -> "§B";
                default -> null;
            };
        }
    }

    public static void displaySubCommands(Player player, String label) {
        String name = cmdHeaderClr(label, true) + label.toUpperCase();
        player.sendMessage("------------ | " + name + "§R§F | ------------>");
        for (SubCommand i : subcommands.get(label)) {
            player.sendMessage(i.getSyntax() + " §7→ " + i.getDescription());
        }
        player.sendMessage("<-------------------------------------------------->");
    }

    public static void displayAllCommands(Player player) { // Maybe add pages to this one day
        player.sendMessage("------------ | " + smpName + " Commands" + "§R§F | ------------>");
        // From subcommands list
        for (List<SubCommand> i : subcommands.values()) {
            for (SubCommand subCommand : i) {
                player.sendMessage(subCommand.getSyntax() + " §7→ " + subCommand.getDescription());
            }
        }
        // Single Commands
        player.sendMessage(Wand.getSyntax() + " §7→ " + Wand.getDescription());
        player.sendMessage("<-------------------------------------------------->");
    }
}