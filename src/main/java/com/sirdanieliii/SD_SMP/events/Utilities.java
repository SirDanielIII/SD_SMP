package com.sirdanieliii.SD_SMP.events;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

import static com.sirdanieliii.SD_SMP.SD_SMP.SMP_CONFIG;

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

    public static String randomMessage(String type) {
        switch (type) {
            case ("join") -> {
                return SMP_CONFIG.joinMessages.get(new Random().nextInt(SMP_CONFIG.joinMessages.size()));
            }
            case ("quit") -> {
                return SMP_CONFIG.quitMessages.get(new Random().nextInt(SMP_CONFIG.quitMessages.size()));
            }
            case ("sleep") -> {
                return SMP_CONFIG.sleepMessages.get(new Random().nextInt(SMP_CONFIG.sleepMessages.size()));
            }
            case ("kill") -> { // Describing player kills
                return SMP_CONFIG.describeKill.get(new Random().nextInt(SMP_CONFIG.describeKill.size()));
            }
            case ("death") -> { // Describing deaths (not including PVP)
                return SMP_CONFIG.describeDeath.get(new Random().nextInt(SMP_CONFIG.describeDeath.size()));
            }
        }
        return null;
    }

    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder(input.length());
        boolean nextTitleCase = true;
        for (char c : input.toLowerCase().toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }
            titleCase.append(c);
        }
        return titleCase.toString();
    }
}
