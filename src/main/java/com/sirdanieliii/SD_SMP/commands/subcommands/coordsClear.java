package com.sirdanieliii.SD_SMP.commands.subcommands;

import com.sirdanieliii.SD_SMP.commands.SubCommand;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.sirdanieliii.SD_SMP.SD_SMP.PLAYER_CONFIG;
import static com.sirdanieliii.SD_SMP.commands.CommandManager.headers;
import static com.sirdanieliii.SD_SMP.configuration.ReturnCoordsData.*;
import static com.sirdanieliii.SD_SMP.events.ErrorMessages.*;
import static com.sirdanieliii.SD_SMP.events.Utilities.toTitleCase;

public class coordsClear extends SubCommand {
    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "§7Clears saved coordinate(s)";
    }

    @Override
    public String getSyntax() {
        return "§6/coords clear <name> [Dimension] §7or\n§6/coords clear all [Dimension]";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        PLAYER_CONFIG.setup("playerdata", player.getUniqueId().toString());
        PLAYER_CONFIG.reload();
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("all")) { // Add clickable permission here one day
                PLAYER_CONFIG.getConfig().set("coordinates." + "overworld", new Array[]{});
                PLAYER_CONFIG.getConfig().set("coordinates." + "nether", new Array[]{});
                PLAYER_CONFIG.getConfig().set("coordinates." + "the_end", new Array[]{});
                PLAYER_CONFIG.save();
                player.sendMessage(headers("COORDS") + "§ASuccessfully §FCleared §BAll §FCoordinates");
            } else { // /coords clear name
                String dimension = returnDimensionString(player.getWorld().getEnvironment());
                if (PLAYER_CONFIG.getConfig().getString("coordinates." + dimension + "." + toTitleCase(args[1])) != null) {
                    player.sendMessage(headers("COORDS") + returnMessage(player, toTitleCase(args[1]), dimension));
                    PLAYER_CONFIG.getConfig().set("coordinates." + dimension + "." + toTitleCase(args[1]), null);
                } else player.sendMessage(incorrectArgs("COORDINATE", toTitleCase(args[1]), dimension));
            }
            PLAYER_CONFIG.save();
        } else if (args.length == 3) { // If dimension is specified
            if (args[1].equalsIgnoreCase("all")) { // Add clickable permission here one day
                // /coords clear all overworld
                if (!(returnDimensionString(args[2]).equalsIgnoreCase("null"))) {
                    player.sendMessage(headers("COORDS") + returnMessage(returnDimensionString(args[2])));
                    PLAYER_CONFIG.getConfig().set("coordinates." + returnDimensionString(args[2]), new Array[]{});
                } else player.sendMessage(errorMessage("DIMENSION", toTitleCase(args[2])));
            } else {
                // /coords clear name overworld
                if (retrieveCoord(player, toTitleCase(args[1]), returnDimensionString(args[2])).size() != 0) {
                    player.sendMessage(headers("COORDS") + returnMessage(player, toTitleCase(args[1]), returnDimensionString(args[2])));
                    PLAYER_CONFIG.getConfig().set("coordinates." + returnDimensionString(args[2]) + "." + toTitleCase(args[1]), null);
                } else player.sendMessage(incorrectArgs("COORDINATE", toTitleCase(args[1]), toTitleCase(args[2])));
            }
        }
        PLAYER_CONFIG.save();
        return true;
    }

    private String returnMessage(Player player, String name, String dimension) {
        ArrayList<String> coordinates = retrieveCoord(player, name, returnDimensionString(dimension));
        switch (dimension) {
            case ("overworld") -> {
                return "§ASuccessfully §FCleared §B" + name + " §6[§F" + coordinates.get(0) + "§6] §Fin §AOverworld";
            }
            case ("nether") -> {
                return "§ASuccessfully §FCleared §B" + name + " §6[§F" + coordinates.get(0) + "§6] §Fin §CNether";
            }
            case ("the_end") -> {
                return "§ASuccessfully §FCleared §B" + name + " §6[§F" + coordinates.get(0) + "§6] §Fin §DThe End";
            }
            default -> {
                return "null";
            }
        }
    }

    private String returnMessage(String dimension) {
        switch (dimension) {
            case ("overworld") -> {
                return "§ASuccessfully §FCleared All §FCoordinates in §AOverworld";
            }
            case ("nether") -> {
                return "§ASuccessfully §FCleared All Coordinates in §CNether";
            }
            case ("the_end") -> {
                return "§ASuccessfully §FCleared All Coordinates in §DThe End";
            }
            default -> {
                return "null";
            }
        }
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        // /coords clear <name> [Dimension] §7or §6/coords clear all [Dimension]
        if (args.length == 2) {
            List<String> types = new ArrayList<>();
            PLAYER_CONFIG.setup("playerdata", player.getUniqueId().toString());
            PLAYER_CONFIG.reload();
            for (String i : dimensionStrings) {
                try {
                    types.addAll(Objects.requireNonNull(PLAYER_CONFIG.getConfig().getConfigurationSection("coordinates." + i)).getKeys(false));
                } catch (Exception ignored) {
                }
            }
            types.add("all");
            return types;
        } else if (args.length == 3) {
            List<String> types = new ArrayList<>();
            if (args[1].equalsIgnoreCase("all")) {
                for (String i : dimensionStrings) types.add(toTitleCase(i));
            } else {
                PLAYER_CONFIG.setup("playerdata", player.getUniqueId().toString());
                PLAYER_CONFIG.reload();
                for (String i : dimensionStrings) {
                    if (PLAYER_CONFIG.getConfig().getString("coordinates." + i + "." + toTitleCase(args[1])) != null) {
                        types.add(toTitleCase(returnDimensionString(i)));
                    }
                }
            }
            return types;
        }
        return null;
    }
}