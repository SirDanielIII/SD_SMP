package ca.sirdanieliii.SD_SMP.coords;

import ca.sirdanieliii.SD_SMP.configuration.ConfigYML;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import static ca.sirdanieliii.SD_SMP.utilities.Utilities.translateMsgClr;
import static ca.sirdanieliii.SD_SMP.utilities.Utilities.translateMsgClrComponent;


public final class CoordsUtility {

    public static boolean worldValid(String name) {
        return Bukkit.getWorld(name) != null;
    }

    public static boolean worldValid(UUID id) {
        return Bukkit.getWorld(id) != null;
    }

    /**
     * Gets the UUID of a Minecraft world, given world name
     *
     * @param name world name
     * @return uuid of minecraft world
     */
    @Nullable
    public static UUID getWorldUid(String name) {
        UUID id = null;
        try {
            id = Objects.requireNonNull(Bukkit.getWorld(name)).getUID();
        } catch (NullPointerException ignored) {
        }
        return id;
    }

    /**
     * Gets the name of a Minecraft world, given its UUID
     *
     * @param uuid world uuid
     * @return world name
     */
    @Nullable
    public static String getWorldName(UUID uuid) {
        String name = null;
        try {
            name = Objects.requireNonNull(Bukkit.getWorld(uuid)).getName();
        } catch (NullPointerException ignored) {
        }
        return name;
    }

    /**
     * Gets the dimension enum of a Minecraft world, given it UUID
     *
     * @param uuid world uuid
     * @return corresponding enum
     */
    @Nullable
    public static World.Environment getWorldDimension(UUID uuid) {
        World.Environment env = null;
        try {
            env = Objects.requireNonNull(Bukkit.getWorld(uuid)).getEnvironment();
        } catch (NullPointerException ignored) {
        }
        return env;
    }

    /**
     * Gets the UUID of the world the player is located in
     *
     * @param player minecraft player
     * @return uuid of minecraft world
     */
    public static UUID getPlayerWorldUiD(Player player) {
        return player.getWorld().getUID();
    }

    /**
     * Gets the dimension that the player is located in
     *
     * @param player minecraft player
     * @return dimension of minecraft world
     */
    public static World.Environment getPlayerDimension(Player player) {
        return player.getWorld().getEnvironment();
    }

    /**
     * Retrieve's the player's coordinates.
     *
     * @param player the player object
     * @return the player's coordinates (in integers) as an ArrayList
     */
    public static ArrayList<Integer> getPlayerCoords(Player player) {
        return new ArrayList<>(Arrays.asList((int) player.getLocation().getX(), (int) player.getLocation().getY(), (int) player.getLocation().getZ()));
    }

    /**
     * Return coordinate of a specified axis (x or y or z).
     *
     * @param config player configuration file
     * @param uid    specified unique ID for world
     * @param name   name of coordinate
     * @param axis   axis (x or y or z)
     * @return coordinate value. Returns 0 if the path is null.
     */
    public static int getCoordAxis(FileConfiguration config, UUID uid, String name, String axis) {
        return config.getInt(String.format("coordinates.%s.coords.%s.%s", uid.toString(), name, axis.toLowerCase()));
    }

    /**
     * Return coordinate in a single string value.
     *
     * @param config player configuration file
     * @param uid    specified unique ID for world
     * @param name   name of coordinate
     * @return coordinate values as string: X Y Z
     */
    public static String getCoordsStr(FileConfiguration config, UUID uid, String name) {
        return String.format("%d %d %d",
                getCoordAxis(config, uid, name, "x"),
                getCoordAxis(config, uid, name, "y"),
                getCoordAxis(config, uid, name, "z"));
    }

    /**
     * @param config player configuration file
     * @param uid    unique id of world
     * @param name   name of coordinate
     * @return true if there already exists a coordinate saved with given name, false otherwise
     */
    public static boolean duplicateCoordName(ConfigYML config, UUID uid, String name) {
        try {
            return Objects.requireNonNull(config.getConfig().getConfigurationSection(
                    String.format("coordinates.%s.coords", uid.toString()))).getKeys(false).contains(name);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static TextComponent getWorldComponent(UUID id) {
        TextComponent w = translateMsgClrComponent(Dimension.getDimensionEnum(getWorldDimension(id)).getClr() + getWorldName(id));
        w.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(translateMsgClr(String.format("&7Dimension: %s\n&8%s",
                Dimension.getDimensionEnum(getWorldDimension(id)).formattedStr(true, false, true), id.toString())))));
        return w;
    }

    /**
     * Finds the world that contain matching saved coordinate names.
     *
     * @param config player configuration file
     * @param name   name of coordinate
     * @return list of world UUIDs that contain matching coordinate names
     */
    public static ArrayList<String> nameMatchReturnWorld(FileConfiguration config, String name) {
        ArrayList<String> worlds = new ArrayList<>();
        try {
            for (String worldUiD : Objects.requireNonNull(config.getConfigurationSection("coordinates")).getKeys(false))
                for (String coord_name : Objects.requireNonNull(config.getConfigurationSection(String.format("coordinates.%s.coords", worldUiD))).getKeys(false))
                    if (Objects.equals(name, coord_name)) worlds.add(worldUiD); // Add world uuid if name matches
        } catch (NullPointerException ignored) {
        }
        return worlds;
    }

    /**
     * Adds the configuration for a World into the player's config if it doesn't exist
     *
     * @param id     minecraft world id
     * @param config player configuration file
     */
    public static void addNewWorldToPlayerConfig(ConfigYML config, UUID id) {
        World world = Bukkit.getWorld(id);
        assert world != null;
        config.getConfig().set(String.format("coordinates.%s.world_name", id), world.getName());
        config.getConfig().set(String.format("coordinates.%s.dimension", id), Dimension.getDimensionEnum(world.getEnvironment()).toString());
        config.getConfig().set(String.format("coordinates.%s.coords", id), new ArrayList<>());
        config.save();
    }

    /**
     * Returns the folder‐name of your main (level-name) world.
     */
    public static String getMainWorldName() {
        return Bukkit.getWorlds().get(0).getName();
    }

    /**
     * Returns the UUID of the world for the given Dimension.
     * Overworld = no suffix, Nether = "_nether", The End = "_the_end".
     *
     * @param dim which dimension
     * @return the world’s UUID, or null if that world isn’t loaded
     */
    public static UUID getWorldUuid(Dimension dim) {
        String base = getMainWorldName();
        String suffix = switch (dim) {
            case NETHER -> "_nether";
            case THE_END -> "_the_end";
            default -> "";
        };
        World w = Bukkit.getWorld(base + suffix);
        return (w != null) ? w.getUID() : null;
    }

    /**
     * Get an array of the three default world UUIDs in the order
     * [OVERWORLD, NETHER, THE_END].  Unloaded dims will be null.
     */
    public static UUID[] getDefaultWorldIDs() {
        return new UUID[]{
                getWorldUuid(Dimension.OVERWORLD),
                getWorldUuid(Dimension.NETHER),
                getWorldUuid(Dimension.THE_END)
        };
    }

//    /*
//    REVIEW THIS LATER - MAY NOT NEED THIS METHOD ANYMORE / MAY REQUIRE ENTIRE RE-WRITE
//     */
//
//    /**
//     * Loop through all dimensions in a world and check if they have any saved coordinates.
//     *
//     * @param config player configuration file
//     * @param uid    unique id of world
//     * @return list of worlds & dimensions
//     */
//    public static ArrayList<String> returnNonEmptyDimensions(FileConfiguration config, UUID uid) {
//        ArrayList<String> dimensions = new ArrayList<>();
//        try {
//            for (Dimensions dimension : Dimensions.values())
//                if (config.getSection(String.format("coordinates.%s.%s", uid, dimension.toString())).getRoutesAsStrings(false).size() > 0)
//                    dimensions.add(dimension.toString());
//        } catch (NullPointerException ignored) {
//        }
//        return dimensions;
//    }
}
