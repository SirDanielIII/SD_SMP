package com.sirdanieliii.SD_SMP.commands.subcommands;

import com.sirdanieliii.SD_SMP.commands.SubCommand;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.sirdanieliii.SD_SMP.commands.CommandManager.cmdHeader;
import static com.sirdanieliii.SD_SMP.commands.CommandManager.cmdHeaderClr;
import static com.sirdanieliii.SD_SMP.configuration.ConfigManager.*;
import static com.sirdanieliii.SD_SMP.configuration.CoordsUtility.getCoordValue;
import static com.sirdanieliii.SD_SMP.configuration.CoordsUtility.returnDimensionClr;
import static com.sirdanieliii.SD_SMP.configuration.Utilities.replaceErrorVariable;

public class coordsList extends SubCommand {
    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "§7List saved coordinate(s)";
    }

    @Override
    public String getSyntax() {
        return "§6" + errorMessages.get("coords_list");
    }

    @Override // coords list all
    public boolean perform(Player player, String[] args) {
        if (!(player.hasPermission("sd_smp.coords.list"))) {
            player.sendMessage(errorMessage("permission"));
            return false;
        }
        YamlDocument config = getPlayerConfig(player);
        if (args.length == 1) {
            player.sendMessage(errorMessage("coords_list"));
            return false;
        }
        if (args[1].equalsIgnoreCase("all")) {
            if (args.length == 2) { // coords list all
                player.sendMessage("------------ | " + "§B§LALL " + cmdHeaderClr("coords", true) + "COORDS §R§F | ------------>");
                String[] dimensions = {"overworld", "nether", "the_end"};
                ArrayList<ArrayList<String>> toSend = getAllCoords(config, dimensions);
                if (toSend.isEmpty()) player.sendMessage(errorMessage("no_saved_coords_1"));
                else for (ArrayList<String> i : toSend) for (String j : i) player.sendMessage(j);
                player.sendMessage("<-------------------------------------------------->");
                return true;
            } else {  // coords list all [dimension]
                if (Stream.of("overworld", "nether", "the_end").noneMatch(args[2]::equalsIgnoreCase)) {
                    player.sendMessage(errorMessage("invalid_dimension_1"));
                    return false;
                }
                player.sendMessage("------------ | " + returnDimensionClr(args[2]).toUpperCase() + " " + cmdHeaderClr("coords", true) + "COORDS §R§F | ------------>");
                String[] dimensions = {args[2].toLowerCase()};
                ArrayList<ArrayList<String>> toSend = getAllCoords(config, dimensions);
                if (toSend.isEmpty()) player.sendMessage(replaceErrorVariable(errorMessage("no_saved_coords_2"), returnDimensionClr(args[2].toLowerCase())));
                else for (ArrayList<String> i : toSend) for (String j : i) player.sendMessage(j);
                player.sendMessage("<-------------------------------------------------->");
                return true;
            }
        }
        if (args.length == 2) {  // /coords list name
            ArrayList<String> dimensions = new ArrayList<>();
            for (String dimension : config.getSection("coordinates").getRoutesAsStrings(false)) { // Loop through Overworld, Nether & The End
                for (String name : config.getSection("coordinates." + dimension).getRoutesAsStrings(false)) // Loop through saved coordinates in dimension
                    if (args[1].equalsIgnoreCase(name)) dimensions.add(dimension); // Add dimension if name matches
            }
            if (dimensions.isEmpty()) { // [ERROR] If no coordinate was found
                player.sendMessage(errorMessage("invalid_coords_1"));
                return false;
            }
            for (String i : dimensions) {
                player.sendMessage(cmdHeader("coords") + isAtMSG(config, i, args[1]));
            }
            return true;
        } else {  // /coords list name [dimension]
            if (Stream.of("overworld", "nether", "the_end").noneMatch(args[2]::equalsIgnoreCase)) {
                player.sendMessage(errorMessage("invalid_dimension_1"));
                return false;
            }
            if (getCoordValue(config, args[2].toLowerCase(), args[1], "x") == 0) {
                player.sendMessage(replaceErrorVariable(errorMessage("invalid_coords_2"), args[1], returnDimensionClr(args[2])));
                return false;
            }
            player.sendMessage(cmdHeader("coords") + isAtMSG(config, args[2].toLowerCase(), args[1]));
        }
        return true;
    }

    private ArrayList<ArrayList<String>> getAllCoords(YamlDocument config, String[] dimensions) {
        ArrayList<ArrayList<String>> coordsAll = new ArrayList<>();
        for (String i : dimensions) {
            ArrayList<String> coords = new ArrayList<>();
            for (String key : config.getSection("coordinates." + i).getRoutesAsStrings(false))
                coords.add(isAtMSG(config, i, key));
            Collections.sort(coords); // Sort all coordinates in dimension
            coordsAll.add(coords);
        }
        return coordsAll;
    }

    private String isAtMSG(YamlDocument config, String dimension, String name) {
        return "§B" + name + " is at §6[§F" +
                getCoordValue(config, dimension, name, "x") + " " +
                getCoordValue(config, dimension, name, "y") + " " +
                getCoordValue(config, dimension, name, "z") + " " +
                "§6]§F in " + returnDimensionClr(dimension);
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        // /coords list <name> [Dimension] or /coords list all [Dimension]
        return null;
    }
}
