package com.sirdanieliii.SD_SMP.configuration;

import org.bukkit.World;
import org.bukkit.entity.Player;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Stream;

import static com.sirdanieliii.SD_SMP.SD_SMP.PLAYER_CONFIG;

public class ReturnCoordsData {
    public static int getCoordValue(String value, Player player, String dimension, String name) {
        PLAYER_CONFIG.setup("playerdata", player.getUniqueId().toString());
        PLAYER_CONFIG.reload();
        return PLAYER_CONFIG.getConfig().getInt("coordinates" + "." + dimension + "." + name + "." + value.toUpperCase());
    }

    public static ArrayList<String> retrieveAllCoords(Player player) {
        PLAYER_CONFIG.setup("playerdata", player.getUniqueId().toString());
        PLAYER_CONFIG.reload();
        ArrayList<String> coordinates = new ArrayList<>();
        try {
            ArrayList<String> overworldCoords = new ArrayList<>();
            for (String key : Objects.requireNonNull(PLAYER_CONFIG.getConfig().getConfigurationSection("coordinates.overworld")).getKeys(false)) {
                overworldCoords.add(formatCoords(key, "overworld",
                        getCoordValue("X", player, "overworld", key)
                                + " " + getCoordValue("Y", player, "overworld", key)
                                + " " + getCoordValue("Z", player, "overworld", key), true));
            }
            Collections.sort(overworldCoords);
            coordinates.addAll(overworldCoords);
        } catch (Exception ignored) {
        }
        try {
            ArrayList<String> netherCoords = new ArrayList<>();
            for (String key : Objects.requireNonNull(PLAYER_CONFIG.getConfig().getConfigurationSection("coordinates.nether")).getKeys(false)) {
                netherCoords.add(formatCoords(key, "nether",
                        getCoordValue("X", player, "nether", key)
                                + " " + getCoordValue("Y", player, "nether", key)
                                + " " + getCoordValue("Z", player, "nether", key), true));
            }
            Collections.sort(netherCoords);
            coordinates.addAll(netherCoords);
        } catch (Exception ignored) {
        }
        try {
            ArrayList<String> the_endCoords = new ArrayList<>();
            for (String key : Objects.requireNonNull(PLAYER_CONFIG.getConfig().getConfigurationSection("coordinates.the_end")).getKeys(false)) {
                the_endCoords.add(formatCoords(key, "the_end",
                        getCoordValue("X", player, "the_end", key)
                                + " " + getCoordValue("Y", player, "the_end", key)
                                + " " + getCoordValue("Z", player, "the_end", key), true));
            }
            Collections.sort(the_endCoords);
            coordinates.addAll(the_endCoords);
        } catch (Exception ignored) {
        }
        return coordinates;
    }

    public static ArrayList<String> retrieveAllCoordsDimension(Player player, String dimension) {
        PLAYER_CONFIG.setup("playerdata", player.getUniqueId().toString());
        PLAYER_CONFIG.reload();
        ArrayList<String> coordinates = new ArrayList<>();
        try {
            for (String key : Objects.requireNonNull(PLAYER_CONFIG.getConfig().getConfigurationSection("coordinates." + dimension)).getKeys(false)) {
                coordinates.add(formatCoords(key, dimension,
                        getCoordValue("X", player, dimension, key)
                                + " " + getCoordValue("Y", player, dimension, key)
                                + " " + getCoordValue("Z", player, dimension, key), true));
            }
            Collections.sort(coordinates);
        } catch (Exception ignored) {
        }
        return coordinates;
    }

    public static ArrayList<String> retrieveCoord(Player player, String name, String dimension, boolean colour) {
        PLAYER_CONFIG.setup("playerdata", player.getUniqueId().toString());
        PLAYER_CONFIG.reload();
        ArrayList<String> coordinates = new ArrayList<>();
        // Single coordinate in dimension
        if (getCoordValue("X", player, dimension, name) != 0) { // Check if exists
            String coords = getCoordValue("X", player, dimension, name)
                    + " " + getCoordValue("Y", player, dimension, name)
                    + " " + getCoordValue("Z", player, dimension, name);
            if (colour) coordinates.add(formatCoords(name, dimension, coords, true));
            else coordinates.add(formatCoords(name, dimension, coords, false));
        }
        return coordinates;
    }

    public static ArrayList<String> retrieveCoord(Player player, String name, String dimension) {
        PLAYER_CONFIG.setup("playerdata", player.getUniqueId().toString());
        PLAYER_CONFIG.reload();
        ArrayList<String> coordinates = new ArrayList<>();
        // Single coordinate in dimension
        if (getCoordValue("X", player, dimension, name) != 0) { // Check if exists
            String coords = getCoordValue("X", player, dimension, name)
                    + " " + getCoordValue("Y", player, dimension, name)
                    + " " + getCoordValue("Z", player, dimension, name);
            coordinates.add(coords);
        }
        return coordinates;
    }

    public static String formatCoords(String name, String dimension, String coordinates, boolean colour) {
        if (colour) {
            switch (dimension) {
                case ("overworld") -> {
                    return "§B" + name + " §Fis at §6[§F" + coordinates + "§6] §Fin §AOverworld";
                }
                case ("nether") -> {
                    return "§B" + name + " §Fis at §6[§F" + coordinates + "§6] §Fin §CNether";
                }
                case ("the_end") -> {
                    return "§B" + name + " §Fis at §6[§F" + coordinates + "§6] §Fin §DThe End";
                }
            }
        } else {
            switch (dimension) {
                case ("overworld") -> {
                    return name + " is at [" + coordinates + "] in Overworld";
                }
                case ("nether") -> {
                    return name + " is at [" + coordinates + "] in Nether";
                }
                case ("the_end") -> {
                    return name + " is at [" + coordinates + "] in The End";
                }
            }
        }
        return null;
    }

    public static String formatCoords(String dimension, String coordinates) {
        switch (dimension) {
            case ("overworld") -> {
                return "I am at [" + coordinates + "] in Overworld";
            }
            case ("nether") -> {
                return "I am at [" + coordinates + "] in Nether";
            }
            case ("the_end") -> {
                return "I am at [" + coordinates + "] in The End";
            }
        }
        return null;
    }

    public static World.Environment returnDimension(String name) {
        if (Stream.of("overworld", "normal").anyMatch(name::equalsIgnoreCase))
            return World.Environment.NORMAL;
        else if (Stream.of("nether", "hell", "thenether", "the_nether").anyMatch(name::equalsIgnoreCase))
            return World.Environment.NETHER;
        else if (Stream.of("end", "the_end", "theend").anyMatch(name::equalsIgnoreCase))
            return World.Environment.THE_END;
        return null;
    }

    public static String returnDimensionString(String name) {
        if (returnDimension(name) == World.Environment.NORMAL)
            return "overworld";
        else if (returnDimension(name) == World.Environment.NETHER)
            return "nether";
        else if (returnDimension(name) == World.Environment.THE_END)
            return "the_end";
        return "null";
    }

    public static String returnDimensionTitleString(String name) {
        if (returnDimension(name) == World.Environment.NORMAL)
            return "Overworld";
        else if (returnDimension(name) == World.Environment.NETHER)
            return "Nether";
        else if (returnDimension(name) == World.Environment.THE_END)
            return "The End";
        return "null";
    }

    public static String returnDimensionString(World.Environment dimension) {
        if (dimension == World.Environment.NORMAL)
            return "overworld";
        else if (dimension == World.Environment.NETHER)
            return "nether";
        else if (dimension == World.Environment.THE_END)
            return "the_end";
        return "null";
    }
}
