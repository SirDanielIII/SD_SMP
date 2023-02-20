package com.sirdanieliii.SD_SMP.configuration;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;

public class CoordsUtility {
    public static String[] allDimensionsStr = {"overworld", "nether", "the_end"};

    public static int getCoordValue(YamlDocument config, String dimension, String name, String axis) {
        return config.getInt("coordinates" + "." + dimension + "." + name + "." + axis.toLowerCase());
    }

    public static String getFullCoords(YamlDocument config, String name, String dimension) {
        if (getCoordValue(config, dimension, name, "x") != 0) // Check if it exists
            return getCoordValue(config, dimension, name, "x") + " " +
                    getCoordValue(config, dimension, name, "y") + " " +
                    getCoordValue(config, dimension, name, "z");
        return null;
    }

    public static ArrayList<Integer>
    getCurrentCoords(Player player) {
        ArrayList<Integer> coords = new ArrayList<>();
        coords.add((int) player.getLocation().getX());
        coords.add((int) player.getLocation().getY());
        coords.add((int) player.getLocation().getZ());
        return coords;
    }

    public static String returnDimensionClr(String dimension, boolean bold) {
        if (dimension.equalsIgnoreCase("overworld")) {
            if (bold) return "&A&LOverworld";
            else return "&AOverworld";
        } else if (dimension.equalsIgnoreCase("nether")) {
            if (bold) return "&C&LNether";
            else return "&CNether";
        } else if (dimension.equalsIgnoreCase("the_end")) {
            if (bold) return "&D&LThe End";
            else return "&DThe End";
        }
        return dimension;
    }

    public static String returnClr(String str) {
        switch (str) {
            case ("overworld") -> {
                return "&A";
            }
            case ("nether") -> {
                return "&C";
            }
            case ("the_end") -> {
                return "&D";
            }
        }
        return str;
    }

    public static String translateDimensionToStr(World.Environment dimension) {
        if (dimension == World.Environment.NORMAL) return "overworld";
        else if (dimension == World.Environment.NETHER) return "nether";
        else if (dimension == World.Environment.THE_END) return "the_end";
        return dimension.toString();
    }

    public static ArrayList<String> returnNonEmptyDimension(YamlDocument config) {
        ArrayList<String> dimensions = new ArrayList<>();
        try {
            for (String dimension : config.getSection("coordinates").getRoutesAsStrings(false)) { // Loop through Overworld, Nether & The End
                if (config.getSection("coordinates." + dimension).getRoutesAsStrings(false).size() > 0) dimensions.add(dimension);
            }
        } catch (NullPointerException ignored) {
        }
        return dimensions;
    }

    public static ArrayList<String> nameMatchReturnDimension(YamlDocument config, String name) {
        ArrayList<String> dimensions = new ArrayList<>();
        try {
            for (String dimension : config.getSection("coordinates").getRoutesAsStrings(false)) { // Loop through Overworld, Nether & The End
                for (String coord_name : config.getSection("coordinates." + dimension).getRoutesAsStrings(false)) // Loop through saved coordinates in dimension
                    if (Objects.equals(name, coord_name)) dimensions.add(dimension); // Add dimension if name matches
            }
        } catch (NullPointerException ignored) {
        }
        return dimensions;
    }
}
