package com.sirdanieliii.SD_SMP.configuration;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Utilities {
    public static String getCardinalDirection(Player event) {
        double rotation = (event.getLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (0 <= rotation && rotation < 22.5) {
            return "W";
        } else if (22.5 <= rotation && rotation < 67.5) {
            return "NW";
        } else if (67.5 <= rotation && rotation < 112.5) {
            return "N";
        } else if (112.5 <= rotation && rotation < 157.5) {
            return "NE";
        } else if (157.5 <= rotation && rotation < 202.5) {
            return "E";
        } else if (202.5 <= rotation && rotation < 247.5) {
            return "SE";
        } else if (247.5 <= rotation && rotation < 292.5) {
            return "S";
        } else if (292.5 <= rotation && rotation < 337.5) {
            return "SW";
        } else if (337.5 <= rotation && rotation < 360.0) {
            return "N";
        } else {
            return null;
        }
    }

    // Get Offset Based off Direction
    public static Vector offsetFromDirection(Player event, Double offset) {
        Vector loc = null;
        switch (Objects.requireNonNull(getCardinalDirection(event))) {
            case ("N") -> loc = new Vector(0, 0, -offset);
            case ("E") -> loc = new Vector(offset, 0, 0);
            case ("S") -> loc = new Vector(0, 0, offset);
            case ("W") -> loc = new Vector(-offset, 0, 0);
            case ("NE") -> loc = new Vector(offset, 0, -offset);
            case ("SE") -> loc = new Vector(offset, 0, offset);
            case ("NW") -> loc = new Vector(-offset, 0, -offset);
            case ("SW") -> loc = new Vector(-offset, 0, offset);
        }
        return loc;
    }

    public static String randomMessageStrLst(List<String> lst) {
        return lst.get(new Random().nextInt(lst.size()));
    }

    public static String replaceVariableInStr(String msg, String toReplace, String replace) {
        // Change method so that it specifies what thing to replace
        return translateColourCodes(msg.replace(toReplace, replace));
    }

    public static String replaceVariableInStr(String msg, String toReplace1, String replace1, String toReplace2, String replace2) {
        return translateColourCodes(msg.replace(toReplace1, replace1).replace(toReplace2, replace2));
    }


    public static String translateColourCodes(String msg) {
        // Original code by Kody Simpson -> https://youtu.be/KUv6NyRo31s
        String[] texts = msg.split(String.format("((?<=%1$s)|(?=%1$s))", "&"));
        StringBuilder finalMsg = new StringBuilder();
        for (int i = 0; i < texts.length; i++) {
            if (texts[i].equalsIgnoreCase("&")) {
                i++; // Get the next string
                if (texts[i].charAt(0) == '#') finalMsg.append(net.md_5.bungee.api.ChatColor.of(texts[i].substring(0, 7))).append(texts[i].substring(7));
                else finalMsg.append(ChatColor.translateAlternateColorCodes('&', "&" + texts[i]));
            } else finalMsg.append(texts[i]);
        }
        return finalMsg.toString();
    }
}
