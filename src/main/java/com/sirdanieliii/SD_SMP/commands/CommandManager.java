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

public class CommandManager implements TabExecutor {
    public static Map<String, List<SubCommand>> cmdCategories = new HashMap<>();
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
        cmdCategories.put("ivan", ivan);
        cmdCategories.put("death", death);
        cmdCategories.put("coords", coords);
    }

    public static ArrayList<SubCommand> getSubcommands(String key) {
        return (ArrayList<SubCommand>) cmdCategories.get(key);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;
        if (args.length > 0) { // Provided arguments
            for (SubCommand subcommand : getSubcommands(cmd.getName())) // Loop through all subcommands of category
                if (args[0].equalsIgnoreCase(subcommand.getName())) subcommand.perform(player, args);  // Check for any command family matches and perform it
        } else displaySubCommands(player, cmd.getName());  // Print all subcommands of command family if no arguments are given in command
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

    public static void displaySubCommands(Player player, String cmd) {
        String name = cmdHeaderClr(cmd, true) + cmd.toUpperCase();
        player.sendMessage("------------ | " + name + "§R§F | ------------>");
        for (SubCommand subcommand : cmdCategories.get(cmd)) player.sendMessage(subcommand.getSyntax() + " §7→ " + subcommand.getDescription());
        player.sendMessage("<-------------------------------------------------->");
    }
}