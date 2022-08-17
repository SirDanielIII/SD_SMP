package com.sirdanieliii.SD_SMP.configuration;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CoordsUtility {
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

    public static ArrayList<Integer> getCurrentCoords(Player player) {
        ArrayList<Integer> coords = new ArrayList<>();
        coords.add((int) player.getLocation().getX());
        coords.add((int) player.getLocation().getY());
        coords.add((int) player.getLocation().getZ());
        return coords;
    }


    public static String returnDimensionClr(String dimension) {
        if (dimension.equalsIgnoreCase("overworld")) return "§AOverworld";
        else if (dimension.equalsIgnoreCase("nether")) return "§CNether";
        else if (dimension.equalsIgnoreCase("the_end")) return "§DThe End";
        return dimension;
    }

    public static String translateDimensionToStr(World.Environment dimension) {
        if (dimension == World.Environment.NORMAL) return "overworld";
        else if (dimension == World.Environment.NETHER) return "nether";
        else if (dimension == World.Environment.THE_END) return "the_end";
        return dimension.toString();
    }
}
