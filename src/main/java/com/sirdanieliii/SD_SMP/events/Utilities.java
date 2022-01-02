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

    public static String randomMessage(String type, Player player) {
        SMP_CONFIG.setup("", "config");
        SMP_CONFIG.reload();
        switch (type) {
            case ("join") -> {
                List<String> joinMessages = SMP_CONFIG.getConfig().getStringList("join-messages");
                return joinMessages.get(new Random().nextInt(joinMessages.size()));
            }
            case ("quit") -> {
                List<String> quitMessages = SMP_CONFIG.getConfig().getStringList("quit-messages");
                return quitMessages.get(new Random().nextInt(quitMessages.size()));
            }
            case ("sleep") -> {
                List<String> sleepMessage = SMP_CONFIG.getConfig().getStringList("sleep-messages");
                return sleepMessage.get(new Random().nextInt(sleepMessage.size()));
            }
            case ("kill") -> { // Describing player kills
                List<String> killDescription = SMP_CONFIG.getConfig().getStringList("kill");
                return killDescription.get(new Random().nextInt(killDescription.size()));
            }
            case ("death") -> { // Describing deaths (not including PVP)
                List<String> deathDescription = SMP_CONFIG.getConfig().getStringList("death");
                return deathDescription.get(new Random().nextInt(deathDescription.size()));
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
