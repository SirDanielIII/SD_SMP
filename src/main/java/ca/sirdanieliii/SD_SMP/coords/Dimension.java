package ca.sirdanieliii.SD_SMP.coords;


import ca.sirdanieliii.SD_SMP.utilities.Utilities;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public enum Dimension {
    OVERWORLD,
    NETHER,
    THE_END,
    CUSTOM;

    public static String getClr(World.Environment environment) {
        if (environment == World.Environment.NORMAL) {
            return "&A";
        } else if (environment == World.Environment.NETHER) {
            return "&C";
        } else if (environment == World.Environment.THE_END) {
            return "&D";
        }
        return "&7";
    }

    public static Dimension getDimensionEnum(World.Environment environment) {
        if (environment.equals(World.Environment.NORMAL)) {
            return Dimension.OVERWORLD;
        } else if (environment.equals(World.Environment.NETHER)) {
            return Dimension.NETHER;
        } else if (environment.equals(World.Environment.THE_END)) {
            return Dimension.THE_END;
        }
        return Dimension.CUSTOM;
    }

    @Nullable
    public static Dimension getDimensionEnum(String name) {
        return switch (name.toLowerCase()) {
            case "overworld" -> Dimension.OVERWORLD;
            case "nether" -> Dimension.NETHER;
            case "the_end" -> Dimension.THE_END;
            case "custom" -> Dimension.CUSTOM;
            default -> null;
        };
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    public String formattedStr(boolean colour, boolean bold, boolean titleCase) {
        String name = this.toString();
        if (titleCase) name = Utilities.toTitleCase(name.replace("_", " "));
        if (bold) name = "&L" + name;
        if (colour) name = this.getClr() + name;
        return Utilities.translateMsgClr(name);
    }

    public String getClr() {
        if (this.equals(World.Environment.NORMAL)) {
            return "&A";
        } else if (this.equals(World.Environment.NETHER)) {
            return "&C";
        } else if (this.equals(World.Environment.THE_END)) {
            return "&D";
        }
        return "&7";
    }

    public boolean equals(String environment) {
        if (environment == null) {
            return false;
        }
        if (Stream.of("The End", "The_End").anyMatch(environment::equalsIgnoreCase)) {
            return this.equals(Dimension.THE_END);
        }
        return environment.equalsIgnoreCase(super.name());
    }

    public boolean equals(World.Environment environment) {
        if (environment == null) {
            return false;
        }
        if (super.equals(Dimension.OVERWORLD) && environment.equals(World.Environment.NORMAL)) {
            return true;
        }
        return environment.name().equalsIgnoreCase(super.name());
    }

    public boolean equals(World world) {
        if (world == null) {
            return false;
        }
        return equals(world.getEnvironment());
    }
}