package com.sirdanieliii.SD_SMP.events;

public class ErrorMessages {
    public static final String[] dimensionStrings = {"overworld", "nether", "the_end"};

    public static String errorMessage(String type) {
        return switch (type) {
            case ("OP_ONLY") -> "§4[§C§L!§R§4] §CSorry, but only operators can use this command";
            case ("PLAYER_ONLY") -> "§4[§C§L!§R§4] §CSorry, but only players can use this command";
            case ("INTEGER") -> "§4[§C§L!§R§4] §CCoordinates Must be Integer, Not String";
            case ("DIMENSION") -> "§4[§C§L!§R§4] §CNot a valid dimension §7(Must be Overworld / Nether / The_End)";
            default -> null;
        };
    }

    public static String incorrectArgs(String type) {
        return switch (type) {
            case ("IVAN") -> "§4[§C§L!§R§4] §CType must be donkey or dog";
            case ("DEATH") -> "§4[§C§L!§R§4] §CStatistics: player or nonplayer or total or murders or kdr";
            case ("SET") -> "§4[§C§L!§R§4] §C/coords set <here> <name> \n§4[§C§L!§R§4] §C/coords set <X> <Y> <Z> <name> <dimension>";
            case ("SET_ARG0") -> "§4[§C§L!§R§4] §C/coords set here <name> §7or \n§6/coords set <X Y Z> <name> [dimension]";
            case ("SET_ARG1-NAME-1") -> "§4[§C§L!§R§4] §CMissing <name>";
            case ("SET_ARG1-NAME-2") -> "§4[§C§L!§R§4] §CMissing <name> [dimension]";
            case ("SET_COORDS") -> "§4[§C§L!§R§4] §C<X Y Z> values are incomplete";
            case ("LIST") -> "§4[§C§L!§R§4] §C/coords list <name> [Dimension]\n/coords list all [Dimension]";
            case ("CLEAR") -> "§4[§C§L!§R§4] §C/coords clear <name> [Dimension]\n/coords clear all [Dimension]";
            default -> null;
        };
    }

    public static String incorrectArgs(String type, String arg1) {
        return switch (type) {
            case ("COORDINATE") -> "§4[§C§L!§R§4] §CCould not find " + arg1;
            default -> null;
        };
    }

    public static String incorrectArgs(String type, String arg1, String arg2) {
        return switch (type) {
            case ("COORDINATE") -> "§4[§C§L!§R§4] §CCould not find " + arg1 + " in " + arg2;
            default -> null;
        };
    }
}
// Maybe one day move all this onto a config file