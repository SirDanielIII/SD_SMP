package com.sirdanieliii.SD_SMP.commands.subcommands;

import com.sirdanieliii.SD_SMP.commands.SubCommand;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Stream;

import static com.sirdanieliii.SD_SMP.commands.CommandManager.cmdHeader;
import static com.sirdanieliii.SD_SMP.configuration.ConfigManager.*;
import static com.sirdanieliii.SD_SMP.configuration.CoordsUtility.*;

public class coordsSet extends SubCommand {
    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getDescription() {
        return "§7Saves specific coordinate under given name";
    }

    @Override
    public String getSyntax() {
        return "§6" + errorMessages.get("coords_set");
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (!(player.hasPermission("sd_smp.coords.set"))) {
            player.sendMessage(errorMessage("permission"));
            return false;
        }
        YamlDocument config = getPlayerConfig(player);
        switch (args.length) {
            case (1) -> {
                player.sendMessage(errorMessage("coords_set"));
                return false;
            }
            case (2) -> {
                player.sendMessage(errorMessage("no_args_to_set_coord")); // /coords set name
                return false;
            }
            case (3) -> { // /coords set name here
                if (args[2].equalsIgnoreCase("here")) {
                    ArrayList<Integer> coords = getCurrentCoords(player);
                    String dimension = translateDimensionToStr(player.getWorld().getEnvironment());
                    savePlayerCoords(config, player, dimension, args[1], coords);
                    return true;
                }
                player.sendMessage(errorMessage("invalid_xyz"));
                return false;
            }
            case (4) -> { // /coords set name x y
                player.sendMessage(errorMessage("invalid_xyz"));
                return false;
            }
            case (5) -> { // /coords set name x y z
                ArrayList<Integer> coords = new ArrayList<>();
                try {
                    for (int i = 0; i < 3; i++) {
                        // Check if argument is "~" If not, try to get a number from it
                        if (i == 0) {
                            if (Objects.equals(args[2 + i], "~")) coords.add((int) player.getLocation().getX());
                            else coords.add(Integer.parseInt(args[2 + i]));
                        } else if (i == 1) {
                            if (Objects.equals(args[2 + i], "~")) coords.add((int) player.getLocation().getY());
                            else coords.add(Integer.parseInt(args[2 + i]));
                        } else {
                            if (Objects.equals(args[2 + i], "~")) coords.add((int) player.getLocation().getZ());
                            else coords.add(Integer.parseInt(args[2 + i]));
                        }
                    }
                } catch (NumberFormatException ignored) {
                    player.sendMessage(errorMessage("coord_str_not_int"));
                    return false;
                }
                savePlayerCoords(config, player, translateDimensionToStr(player.getWorld().getEnvironment()), args[1], coords);
                return true;
            }
            case (6) -> { // /coords set name x y z <dimension>
                ArrayList<Integer> coords = new ArrayList<>();
                try {
                    for (int i = 0; i < 3; i++) {
                        // Check if argument is "~" If not, try to get a number from it
                        if (i == 0) {
                            if (Objects.equals(args[2 + i], "~")) coords.add((int) player.getLocation().getX());
                            else coords.add(Integer.parseInt(args[2 + i]));
                        } else if (i == 1) {
                            if (Objects.equals(args[2 + i], "~")) coords.add((int) player.getLocation().getY());
                            else coords.add(Integer.parseInt(args[2 + i]));
                        } else {
                            if (Objects.equals(args[2 + i], "~")) coords.add((int) player.getLocation().getZ());
                            else coords.add(Integer.parseInt(args[2 + i]));
                        }
                    }
                } catch (NumberFormatException ignored) {
                    player.sendMessage(errorMessage("coord_str_not_int"));
                    return false;
                }
                if (Stream.of("overworld", "nether", "the_end").noneMatch(args[5]::equalsIgnoreCase)) {
                    player.sendMessage(errorMessage("invalid_dimension_1"));
                    return false;
                }
                Bukkit.broadcastMessage(String.valueOf(coords));
                savePlayerCoords(config, player, args[5].toLowerCase(), args[1], coords);
            }
        }
        return true;
    }

    private void savePlayerCoords(YamlDocument config, Player player, String dimension, String name, ArrayList<Integer> coords) {
        config.set("coordinates." + dimension + "." + name + ".x", coords.get(0));
        config.set("coordinates." + dimension + "." + name + ".y", coords.get(1));
        config.set("coordinates." + dimension + "." + name + ".z", coords.get(2));
        savePlayerConfig(config);
        player.sendMessage(cmdHeader("coords") + "§FSaved §B" + name + " §6[§F" +
                coords.get(0) + " " + coords.get(1) + " " + coords.get(2) + "§6]§F in " + returnDimensionClr(dimension, false));
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        // /coords set <name> [X Y Z] <dimension>
        ArrayList<String> secondArgs = new ArrayList<>(List.of("name"));
        ArrayList<String> thirdArgs = new ArrayList<>(List.of("here", "~"));
        ArrayList<String> fourthArgs = new ArrayList<>(List.of("~"));
        ArrayList<String> fifthArgs = new ArrayList<>(List.of("~"));
        List<String> list = new ArrayList<>();
        switch (args.length) {
            case (2) -> {
                for (String str : secondArgs) if (str.toLowerCase().startsWith(args[1].toLowerCase())) list.add(str);
            }
            case (3) -> {
                for (String str : thirdArgs) if (str.toLowerCase().startsWith(args[2].toLowerCase())) list.add(str);
            }
            case (4) -> {
                if (!args[2].equalsIgnoreCase("here")) for (String str : fourthArgs) if (str.toLowerCase().startsWith(args[3].toLowerCase())) list.add(str);
            }
            case (5) -> {
                if (!args[2].equalsIgnoreCase("here")) for (String str : fifthArgs) if (str.toLowerCase().startsWith(args[4].toLowerCase())) list.add(str);
            }
            case (6) -> {
                if (!args[2].equalsIgnoreCase("here")) for (String str : allDimensionsStr) if (str.toLowerCase().startsWith(args[5].toLowerCase())) list.add(str);
            }
        }
        return list;
    }
}