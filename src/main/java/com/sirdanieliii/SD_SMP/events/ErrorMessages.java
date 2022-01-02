package com.sirdanieliii.SD_SMP.events;

import static com.sirdanieliii.SD_SMP.events.Utilities.toTitleCase;

public class ErrorMessages {
    public static final String[] dimensionStrings = {"overworld", "nether", "the_end"};

    public static String errorMessage(String type) {
        return switch (type) {
            case ("OP_ONLY") -> "§4[§C§L!§R§4] §CSorry, but only operators can use this command";
            case ("PLAYER_ONLY") -> "§4[§C§L!§R§4] §CSorry, but only players can use this command";
            case ("INTEGER") -> "§4[§C§L!§R§4] §C<X Y Z> Coordinates Must be Integer, Not String";
            case ("DIMENSION") -> "§4[§C§L!§R§4] §CNot a valid dimension\n   §7(Must be Overworld / Nether / The_End)";
            default -> null;
        };
    }

    public static String errorMessage(String type, String arg1) {
        return switch (type) {
            case ("PLAYER") -> "§4[§C§L!§R§4] §C" + toTitleCase(arg1) + " isn't online or does not exist!";
            case ("DIMENSION") -> "§4[§C§L!§R§4] §C\"" + (toTitleCase(arg1)) + "\" is not a valid dimension\n   §7(Must be Overworld / Nether / The_End)";
            default -> null;
        };
    }

    public static String incorrectArgs(String type) {
        return switch (type) {
            case ("IVAN") -> "§4[§C§L!§R§4] §CType must be donkey / dog";
            case ("DEATH") -> "§4[§C§L!§R§4] §CArgument must be player / nonplayer / total / murders / kdr";
            case ("SET") -> "§4[§C§L!§R§4] §C/coords set <name> <here> §7OR\n    §C/coords set <name> <X> <Y> <Z> <dimension>";
            case ("SET_ARG0") -> "§4[§C§L!§R§4] §C§C/coords set <name> here §7OR\n§C/coords set <name> <X Y Z> <dimension>";
            case ("SET_ARG1") -> "§4[§C§L!§R§4] §CMissing \"here\" §7OR <X Y Z> <dimension>";
            case ("SET_COORDS_X") -> "§4[§C§L!§R§4] §CMissing <Y Z> <dimension>";
            case ("SET_COORDS_Y") -> "§4[§C§L!§R§4] §CMissing <Z> <dimension>";
            case ("SET_ARG4") -> "§4[§C§L!§R§4] §CMissing [Dimension]";
            case ("LIST") -> "§4[§C§L!§R§4] §C/coords list <name> [Dimension] §7OR\n   §C/coords list all [Dimension]";
            case ("LIST_ALL-NULL") -> "§4[§C§L!§R§4] §CYou have no saved coordinates";
            case ("CLEAR") -> "§4[§C§L!§R§4] §C/coords clear <name> [Dimension] §7OR\n   §C/coords clear all [Dimension]";
            case ("SEND") -> "§4[§C§L!§R§4] §C/coords send here <player> [Players] §7OR\n   §C/coords send <name> <dimension> <player> [Players]";
            case ("SEND_DIMENSION") -> "§4[§C§L!§R§4] §CMissing <dimension> <player> [Players]";
            case ("SEND_PLAYERS") -> "§4[§C§L!§R§4] §CMissing <player> [Players]";
            case ("SEND_NULL") -> "§4[§C§L!§R§4] §CWhy would you send the coords to yourself lol";
            default -> null;
        };
    }

    public static String incorrectArgs(String type, String arg1) {
        return switch (type) {
            case ("COORDINATE") -> "§4[§C§L!§R§4] §CCould not find \"" + arg1 + "\"";
            case ("LIST_ALL_D-NULL") -> "§4[§C§L!§R§4] §CYou have no saved coordinates in \"" + arg1 + "\"";
            default -> null;
        };
    }

    public static String incorrectArgs(String type, String arg1, String arg2) {
        return switch (type) {
            case ("COORDINATE") -> "§4[§C§L!§R§4] §CCould not find \"" + arg1 + "\" in dimension \"" + arg2 + "\"";
            default -> null;
        };
    }
}
// Maybe move all this onto a config file one day?