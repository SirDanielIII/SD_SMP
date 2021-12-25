package com.sirdanieliii.SD_SMP.commands;

import com.sirdanieliii.SD_SMP.commands.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

import static com.sirdanieliii.SD_SMP.events.ErrorMessages.incorrectArgs;
import static com.sirdanieliii.SD_SMP.events.Utilities.toTitleCase;

public class CommandManager implements CommandExecutor {

    static Map<String, List<SubCommand>> subcommands = new HashMap<String, List<SubCommand>>();
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
        death.add(new deathMurders());
        death.add(new deathNonPVP());
        death.add(new deathPVP());
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
        if (!(sender instanceof Player p)) return false;
        boolean present = false;
        if (args.length > 0) {
            try {
                for (int i = 0; i < getSubcommands(cmd.getName()).size(); i++) {
                    if (args[0].equalsIgnoreCase(getSubcommands(cmd.getName()).get(i).getName())) {
                        getSubcommands(cmd.getName()).get(i).perform(p, args);
                        present = true;
                    }
                }
            } catch (Exception ignored) {
            }
            if (!present) incorrectFirstArg(p, label, args);
        } else {
            displayCommands(p, label);
        }
        return true;
    }

    public static String headers(String type) {
        return switch (type.toUpperCase()) {
            case ("COORDS") -> "§6[§FCoords§6] ";
            case ("DEATH") -> "§4[§FDeath§4] ";
            case ("IVAN") -> "§B[§FIvan§B] ";
            default -> null;
        };
    }

    public static String headerColour(String type, boolean bold) {
        if (bold) {
            return switch (type.toUpperCase()) {
                case ("COORDS") -> "§6§L";
                case ("DEATH") -> "§4§L";
                case ("IVAN") -> "§B§L";
                default -> null;
            };
        } else {
            return switch (type.toUpperCase()) {
                case ("COORDS") -> "§6";
                case ("DEATH") -> "§4";
                case ("IVAN") -> "§B";
                default -> null;
            };
        }
    }

    public void incorrectFirstArg(Player player, String cmd, String[] args) {
        switch (cmd.toLowerCase()) {
            case ("ivan") -> player.sendMessage(incorrectArgs("IVAN"));
            case ("death") -> player.sendMessage(incorrectArgs("DEATH"));
            case ("coords") -> {
                switch (args[0].toLowerCase()) {
                    case ("set") -> player.sendMessage(incorrectArgs("SET"));
                    case ("list") -> player.sendMessage(incorrectArgs("LIST"));
                    case ("clear") -> player.sendMessage(incorrectArgs("CLEAR"));
                    case ("send") -> player.sendMessage("§C[!] Send Types: §Ahome §F/ §Aoverworld §F/ §Dportal §F/ §Dnether §F/ §Ball");
                }
            }
        }
    }

    public static void displayCommands(Player player, String label) {
        String name = headerColour(label, true) + label.toUpperCase();
        player.sendMessage("------------ | " + name + "§R§F | ------------>");
        for (SubCommand i : subcommands.get(label)) {
            player.sendMessage(i.getSyntax() + " §7→ " + i.getDescription());
        }
        player.sendMessage("<-------------------------------------------------->");
    }

    public static void displayAllCommands(Player player) {
        // Make header a variable from config
        player.sendMessage("------------ | §6§L" + "31 SMP Commands" + "§R§F | ------------>");
        for (List<SubCommand> i : subcommands.values()) {
            for (SubCommand subCommand : i) {
                player.sendMessage(subCommand.getSyntax() + " §7→ " + subCommand.getDescription());
            }
        }
        player.sendMessage(Wand.getSyntax() + " §7→ " + Wand.getDescription());
        player.sendMessage("<-------------------------------------------------->");
    }
}
// Maybe one day add pages