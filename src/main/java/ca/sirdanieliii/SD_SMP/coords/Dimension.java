package ca.sirdanieliii.SD_SMP.coords;

import ca.sirdanieliii.SD_SMP.utilities.Utilities;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.stream.Stream;

public enum Dimension {
    OVERWORLD(World.Environment.NORMAL, "&A"),
    NETHER(World.Environment.NETHER, "&C"),
    THE_END(World.Environment.THE_END, "&D"),
    CUSTOM(null, "&7");

    private static final Map<World.Environment, Dimension> ENVIRONMENT_TO_DIMENSION = Map.of(
            World.Environment.NORMAL, OVERWORLD,
            World.Environment.NETHER, NETHER,
            World.Environment.THE_END, THE_END
    );

    public final World.Environment environment;
    private final String color;

    Dimension(World.Environment environment, String color) {
        this.environment = environment;
        this.color = color;
    }

    public static String getClr(World.Environment environment) {
        return ENVIRONMENT_TO_DIMENSION.getOrDefault(environment, CUSTOM).color;
    }

    public static Dimension getDimensionEnum(World.Environment environment) {
        return ENVIRONMENT_TO_DIMENSION.getOrDefault(environment, CUSTOM);
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
        String name = this.toString().replace("_", " ");
        if (titleCase) name = Utilities.toTitleCase(name);
        if (bold) name = "&L" + name;
        if (colour) name = this.color + name;
        return Utilities.translateMsgClr(name);
    }

    public String getClr() {
        return this.color;
    }

    public boolean equals(String environment) {
        if (environment == null) {
            return false;
        }
        if (Stream.of("The End", "The_End").anyMatch(environment::equalsIgnoreCase)) {
            return this.equals(Dimension.THE_END);
        }
        return environment.equalsIgnoreCase(this.name());
    }

    public boolean equals(World.Environment environment) {
        return environment != null && environment.equals(this.environment);
    }

    public boolean equals(World world) {
        return world != null && this.equals(world.getEnvironment());
    }
}
