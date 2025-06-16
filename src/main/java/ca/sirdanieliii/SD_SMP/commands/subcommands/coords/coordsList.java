package ca.sirdanieliii.SD_SMP.commands.subcommands.coords;

import ca.sirdanieliii.SD_SMP.commands.CommandManager;
import ca.sirdanieliii.SD_SMP.commands.SubCommand;
import ca.sirdanieliii.SD_SMP.configuration.ConfigManager;
import ca.sirdanieliii.SD_SMP.configuration.ConfigYML;
import ca.sirdanieliii.SD_SMP.configuration.configs.ConfigPlayer;
import ca.sirdanieliii.SD_SMP.coords.Coord;
import ca.sirdanieliii.SD_SMP.coords.CoordsUtility;
import ca.sirdanieliii.SD_SMP.coords.Dimension;
import ca.sirdanieliii.SD_SMP.utilities.ChatPaginator;
import ca.sirdanieliii.SD_SMP.utilities.Utilities;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ca.sirdanieliii.SD_SMP.configuration.ConfigManager.errorMessage;
import static ca.sirdanieliii.SD_SMP.coords.CoordsUtility.getWorldComponent;
import static ca.sirdanieliii.SD_SMP.utilities.Utilities.*;

public class coordsList extends SubCommand {
    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getCmdGroup() {
        return "coords";
    }

    @Override
    public String getDescription() {
        return "Lists saved coordinate(s)";
    }

    @Override
    public String getSyntax() {
        return ConfigManager.generalMsgs.get("coords_list");
    }

    @Override // coords list all
    public boolean perform(Player player, String[] args) {
        if (!(player.hasPermission("sd_smp.coords.list"))) {
            player.sendMessage(errorMessage("permission"));
            return false;
        }
        ConfigPlayer config = new ConfigPlayer(player);
        // /coords list <name|all> <dimension> [world]
        switch (args.length) {
            // /coords list
            case (1) -> {
                player.sendMessage(errorMessage("coords_list"));
                return false;
            }
            /*
             * /coords list name
             * /coords list all
             */
            case (2) -> {
                if (args[1].equalsIgnoreCase("all")) {
                    retrieveAllCoords(config, player, 0);
                    return true;
                }
                // Do name logic here
                // Check for duplicates - If duplicates are detected, prompt the user to run /coords list name --SHOWALL
                Coord coord = retrieveCurrentWorldCoord(config, player, args[1]); // args[1] is the given coords name
                if (coord != null) { // The coordinate does not exist
                    coord.displayCoordToPlayer();
                    return true;
                }
                player.sendMessage(errorMessage("invalid_coords_1"));
                return false;
            }
            /*
             * /coords list name --SHOWALL
             * /coords list name <dimension>
             * /coords list all <page>
             * /coords list all <dimension>
             */
            case (3) -> {
                if (args[1].equalsIgnoreCase("all")) {
                    // /coords list all <page>
                    if (isStringNumber(args[2])) {
                        retrieveAllCoords(config, player, Integer.parseInt(args[2]));
                        return true;
                    }
                    // /coords list all <dimension>
                    Dimension dimension = Dimension.getDimensionEnum(args[2]);
                    if (dimension == null) {
                        player.sendMessage(errorMessage("invalid_dimension"));
                        return false;
                    }
                    List<Coord> coords = retrieveCurrentWorldCoords(config, player);
                    if (coords.isEmpty()) {
                        ChatPaginator paginatedData = new ChatPaginator(
                                translateMsgClrComponent(CommandManager.cmdClr("coords", true) + "COORDS LIST"),
                                coords.stream().map(coord -> coord.getCoordComponent(player)).toList(), 0, ChatColor.WHITE
                        );
                        paginatedData.configureFooter(
                                "<<<", ">>>",
                                null,
                                "/coords list all " + 1,
                                ChatColor.GOLD, ChatColor.GRAY, ChatColor.WHITE
                        );
                        paginatedData.sendPaginatedMessage(player);
                        return true;
                    }
                    player.spigot().sendMessage(replaceStr(errorMessage("no_saved_coords_2"), "{world}", getWorldComponent(player.getWorld().getUID())));
                    return false;
                } else if (args[2].equals("--SHOWALL")) {
                    // /coords list <name> --SHOWALL
                    List<Coord> coords = retrieveWorldCoords(config, player, args[1]);
                    if (!coords.isEmpty()) {
                        ChatPaginator paginatedData = new ChatPaginator(translateMsgClrComponent(
                                CommandManager.cmdClr("coords", true) + "COORDS LIST"),
                                coords.stream().map(coord -> coord.getCoordComponent(player)).toList(),
                                0,
                                ChatColor.WHITE);
                        paginatedData.configureFooter("<<<", ">>>", null, String.format("/coords list %s %d", args[1], 1), // /coords list name 1
                                ChatColor.GOLD, ChatColor.GRAY, ChatColor.WHITE);
                        paginatedData.sendPaginatedMessage(player);
                        return true;
                    }
                    player.sendMessage(replaceStr(errorMessage("no_saved_coords_3"), "{name}", args[1]));
                    return true;
                } else {
                    // /coords list <name> <dimension>
                    Dimension dimension = Dimension.getDimensionEnum(args[2]);
                    if (dimension == null) {
                        player.sendMessage(errorMessage("invalid_dimension"));
                        return false;
                    }
                    Coord coord = retrieveCurrentWorldCoord(config, player, args[1], dimension); // args[1] is the given coords name
                    if (coord != null) { // The coordinate does not exist
                        coord.displayCoordToPlayer();
                        return true;
                    }
                    player.sendMessage(
                            replaceStr(errorMessage("invalid_coords_3"),
                                    List.of("coord_name", "dimension"),
                                    List.of(args[1], dimension.formattedStr(true, false, true))));
                    return false;
                }
            }
            /*
             * /coords list name <dimension> <page>
             * /coords list name <dimension> --SHOWALL
             * /coords list name <dimension> [world]
             * /coords list all <dimension> <page>
             * /coords list all <dimension> [world]
             */
            case (4) -> {


            }
            /*
             * /coords list name <dimension> --SHOWALL <page>
             * /coords list all <dimension> [world] <page>
             */
            case (5) -> {
                // /coords list all <dimension> [world] <page>
                if (args[1].equalsIgnoreCase("all")) {
                    // Check dimension input
                    Dimension dimension = Dimension.getDimensionEnum(args[2]);
                    if (dimension == null) {
                        player.sendMessage(errorMessage("invalid_dimension"));
                        return false;
                    }
                    // Check world input
                    UUID worldID = CoordsUtility.getWorldUid(args[3]);
                    if (worldID == null) {
                        player.sendMessage(errorMessage("invalid_world"));
                        return false;
                    }
                    // Check page input
                    if (!isStringNumber(args[4])) {
                        player.sendMessage(errorMessage("invalid_page_number"));
                        return false;
                    }
                    int page = Integer.parseInt(args[4]);
                    List<Coord> coordinates = retrieveWorldCoords(config, player, dimension, worldID);
                    if (coordinates == null) { // This error is for if the dimension & world do not match up
                        player.sendMessage(replaceStr(errorMessage("invalid_dimension_for_world"), List.of("world", "dimension"), List.of(Objects.requireNonNull(Bukkit.getWorld(worldID)).getName(), dimension.toString())));
                        return false;
                    }
                    if (!coordinates.isEmpty()) {
                        ChatPaginator paginatedData = new ChatPaginator(
                                translateMsgClrComponent(CommandManager.cmdClr("coords", true) + "COORDS LIST"),
                                coordinates.stream().map(coord -> coord.getCoordComponent(player)).toList(),
                                page,
                                ChatColor.WHITE);
                        paginatedData.configureFooter(
                                "<<<", ">>>",
                                String.format("/coords list all %s %s %d", args[2], args[3], page - 1),
                                String.format("/coords list all %s %s %d", args[2], args[3], page + 1),
                                ChatColor.GOLD, ChatColor.GRAY, ChatColor.WHITE);
                        paginatedData.sendPaginatedMessage(player);
                        return true;
                    }
                    player.sendMessage(replaceStr(errorMessage("no_saved_coords_2"), "{world}", args[3]));
                    return false;
                }
                // /coords list name <dimension> --SHOWALL <page>
                // Check dimension input
                Dimension dimension = Dimension.getDimensionEnum(args[2]);
                if (dimension == null) {
                    player.sendMessage(errorMessage("invalid_dimension"));
                    return false;
                }
                if (!args[3].equals("--SHOWALL")) {
                    player.sendMessage(errorMessage("invalid_world"));
                    return false;
                }
                // Check page input
                if (!isStringNumber(args[4])) {
                    player.sendMessage(errorMessage("invalid_page_number"));
                    return false;
                }
                int page = Integer.parseInt(args[4]);
                List<Coord> coordinates = retrieveWorldCoords(config, player, args[1], dimension);
                if (coordinates == null) { // This error is if the coords section is missing entirely
                    player.sendMessage(errorMessage("broken_player_config"));
                    return false;
                }
                if (coordinates.isEmpty()) {
                    ChatPaginator paginatedData = new ChatPaginator(translateMsgClrComponent(
                            CommandManager.cmdClr("coords", true) + "COORDS LIST"),
                            coordinates.stream().map(coord -> coord.getCoordComponent(player)).toList(),
                            page,
                            ChatColor.WHITE);
                    paginatedData.configureFooter("<<<", ">>>", String.format("/coords list all %s %s %d", args[2], args[3], page - 1),
                            String.format("/coords list all %s %s %d", args[2], args[3], page + 1), ChatColor.GOLD, ChatColor.GRAY, ChatColor.WHITE);
                    paginatedData.sendPaginatedMessage(player);
                    return true;
                }
                player.sendMessage(replaceStr(errorMessage("no_saved_coords_3"), "{name}", args[1]));
                return false;
            }
            // Number of arguments are more than 6
            default -> {
                player.sendMessage(Utilities.replaceStr(errorMessage("too_many_arguments"), "{cmd_syntax}", ConfigManager.generalMsgs.get("coords_list")));
            }
        }
        return true;
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        // /coords list <name | all> <dimension>
        ConfigPlayer config = new ConfigPlayer(player);
        ArrayList<String> secondArgs = new ArrayList<>(List.of("all"));
        List<String> list = new ArrayList<>();
        return list;
    }

    /**
     * Lists coords to player for /coords list all [page]
     *
     * @param config The player's config file
     * @param player A Minecraft player
     * @param page   Page number for paginated data
     */
    private void retrieveAllCoords(ConfigPlayer config, Player player, int page) {
        List<Coord> overworld = new ArrayList<>();
        List<Coord> nether = new ArrayList<>();
        List<Coord> theEnd = new ArrayList<>();
        List<Coord> custom = new ArrayList<>();
        ConfigurationSection coordinatesSection = config.getConfig().getConfigurationSection("coordinates");

        if (coordinatesSection != null) {
            for (String id : coordinatesSection.getKeys(false)) {
                World world = Bukkit.getWorld(id);
                if (world == null) {
                    player.sendMessage(errorMessage("invalid_world"));
                    return;
                }
                UUID worldID = world.getUID();
                ConfigurationSection worldCoordsSection = coordinatesSection.getConfigurationSection(id + ".coords");

                if (worldCoordsSection != null) {
                    for (String name : worldCoordsSection.getKeys(false)) {
                        ConfigurationSection coordSection = worldCoordsSection.getConfigurationSection(name);
                        if (coordSection != null) {
                            Coord coord = new Coord(player, worldID, coordSection);

                            switch (world.getEnvironment()) {
                                case NORMAL -> overworld.add(coord);
                                case NETHER -> nether.add(coord);
                                case THE_END -> theEnd.add(coord);
                                default -> custom.add(coord);
                            }
                        }
                    }
                }
            }
        }

        List<TextComponent> coordComponents = Stream.of(overworld, nether, theEnd, custom)
                .flatMap(Collection::stream)
                .map(coord -> coord.getCoordComponent(player))
                .collect(Collectors.toList());

        // Check if the list is empty or not
        if (coordComponents.isEmpty()) {
            player.sendMessage(errorMessage("no_saved_coords_1"));
            return;
        }

        ChatPaginator paginatedData = new ChatPaginator(
                translateMsgClrComponent(CommandManager.cmdClr("coords", true) + "COORDS LIST"),
                coordComponents, page, ChatColor.WHITE
        );

        paginatedData.configureFooter(
                "<<<", ">>>",
                "/coords list all " + (page - 1),
                "/coords list all " + (page + 1),
                ChatColor.GOLD, ChatColor.GRAY, ChatColor.WHITE
        );
        paginatedData.sendPaginatedMessage(player);
    }

    /**
     * Retrieve current world's coord for /coords list name
     *
     * @param config The player's config file
     * @param player A Minecraft player
     * @param name   Coordinate name to match
     * @return List of coordinates
     */
    @Nullable
    private Coord retrieveCurrentWorldCoord(ConfigYML config, Player player, String name) {
        World world = player.getWorld();
        ConfigurationSection coordsSection = config.getConfig().getConfigurationSection(String.format("coordinates.%s.coords", world.getUID()));
        if (coordsSection != null) {
            for (String coordName : coordsSection.getKeys(false)) {
                if (!name.equals(coordName)) {
                    continue;
                }
                ConfigurationSection coordSection = coordsSection.getConfigurationSection(name);
                if (coordSection != null) {
                    return new Coord(player, world.getUID(), name, coordSection.getInt("x"), coordSection.getInt("y"), coordSection.getInt("z"));
                }
            }
        }
        return null;
    }

    /**
     * Retrieve given dimension's coord for /coords list <name> <dimension>
     *
     * @param config The player's config file
     * @param player A Minecraft player
     * @param name   Coordinate name to match
     * @return List of coordinates
     */
    @Nullable
    private Coord retrieveCurrentWorldCoord(ConfigYML config, Player player, String name, Dimension dimension) {
        UUID worldID = CoordsUtility.getMainWorldID(dimension);
        ConfigurationSection coordsSection = config.getConfig().getConfigurationSection(String.format("coordinates.%s.coords", worldID));
        if (coordsSection != null) {
            for (String coordName : coordsSection.getKeys(false)) {
                if (!name.equals(coordName)) {
                    continue;
                }
                ConfigurationSection coordSection = coordsSection.getConfigurationSection(name);
                if (coordSection != null) {
                    return new Coord(player, worldID, name, coordSection.getInt("x"), coordSection.getInt("y"), coordSection.getInt("z"));
                }
            }
        }
        return null;
    }

    /**
     * Retrieve coords for /coords list all <dimension>
     *
     * @param config The player's config file
     * @param player A Minecraft player
     * @return List of coordinates
     */
    private List<Coord> retrieveCurrentWorldCoords(ConfigYML config, Player player) {
        World world = player.getWorld();
        List<Coord> coords = new ArrayList<>();
        // Go through every coord and check for ones with the same name
        ConfigurationSection coordsSection = config.getConfig().getConfigurationSection(String.format("coordinates.%s.coords", world.getUID()));
        if (coordsSection != null) {
            for (String coordName : coordsSection.getKeys(false)) {
                ConfigurationSection coordSection = coordsSection.getConfigurationSection(coordName);
                if (coordSection != null) {
                    coords.add(new Coord(player, world.getUID(), coordSection));
                }
            }
        }
        return coords;
    }

    /**
     * Retrieve coords for /coords list <name> --SHOWALL
     *
     * @param config The player's config file
     * @param player A Minecraft player
     * @param name   Coordinate name to match
     * @return List of coordinates
     */
    private List<Coord> retrieveWorldCoords(ConfigYML config, Player player, String name) {
        List<Coord> coords = new ArrayList<>();
        // Go through every coord and check for ones with the same name
        ConfigurationSection coordinatesSection = config.getConfig().getConfigurationSection("coordinates");
        if (coordinatesSection != null) {
            for (String id : coordinatesSection.getKeys(false)) { // World IDs
                World world = Bukkit.getWorld(id);
                if (world == null) {
                    continue;
                }
                UUID worldID = world.getUID();
                ConfigurationSection worldCoordsSection = coordinatesSection.getConfigurationSection(id + ".coords");
                if (worldCoordsSection == null) {
                    continue;
                }
                // Loop through each saved coordinate's key (name)
                for (String coordName : worldCoordsSection.getKeys(false)) {
                    if (!coordName.equals(name)) {
                        continue; // Skip if coordName does not match
                    }
                    // Get data on coordinate
                    ConfigurationSection coordSection = worldCoordsSection.getConfigurationSection(name);
                    if (coordSection != null) {
                        coords.add(new Coord(player, worldID, coordSection));
                    }
                }
            }
        }
        return coords;
    }

    /**
     * Retrieve coords for /coords list name <dimension> --SHOWALL ...
     *
     * @param config    The player's config file
     * @param player    A Minecraft player
     * @param name      Coordinate name to match
     * @param dimension Dimension of coordinate
     * @return List of coordinates
     */
    private List<Coord> retrieveWorldCoords(ConfigYML config, Player player, String name, @NotNull Dimension dimension) {
        List<Coord> coords = new ArrayList<>();

        ConfigurationSection coordinatesSection = config.getConfig().getConfigurationSection("coordinates");
        if (coordinatesSection == null) {
            return null;
        }
        for (String worldID : coordinatesSection.getKeys(false)) {
            World world = Bukkit.getWorld(worldID);
            if (world == null) {
                continue;
            }
            ConfigurationSection worldSection = coordinatesSection.getConfigurationSection(worldID);
            assert worldSection != null;
            // Ignore if the dimension does not match up
            if (dimension.equals(worldSection.get("dimension"))) {
                continue;
            }
            ConfigurationSection coordsSection = worldSection.getConfigurationSection("coords");
            if (coordsSection != null) {
                for (String coordName : coordsSection.getKeys(false)) {
                    if (!coordName.equals(name)) {
                        continue;
                    }
                    ConfigurationSection coordSection = coordsSection.getConfigurationSection(coordName);
                    if (coordSection != null) {
                        coords.add(new Coord(player, world.getUID(), coordSection));
                    }
                }
            }
        }
        return coords;
    }

    /**
     * Retrieve coords for /coords list all <dimension> [world]
     *
     * @param config    The player's config file
     * @param player    A Minecraft player
     * @param dimension Dimension of coordinate
     * @param worldID   ID of coordinate's Minecraft world
     * @return List of coordinates
     */
    private List<Coord> retrieveWorldCoords(ConfigYML config, Player player, @NotNull Dimension dimension, @NotNull UUID worldID) {
        List<Coord> coords = new ArrayList<>();

        ConfigurationSection worldSection = config.getConfig().getConfigurationSection(String.format("coordinates.%s", worldID));
        if (worldSection != null) {
            // Check if dimension matches (just in case)
            if (!dimension.equals(worldSection.getString("dimension"))) {
                return null;
            }
            // If the dimension matches, then we'll now use the specified world as reference for the coordinates
            ConfigurationSection coordsSection = worldSection.getConfigurationSection("coords");
            if (coordsSection != null) {
                for (String coordName : coordsSection.getKeys(false)) {
                    ConfigurationSection coordSection = coordsSection.getConfigurationSection(coordName);
                    if (coordSection != null) {
                        coords.add(new Coord(player, worldID, coordSection));
                    }
                }
            }
        }
        return coords;
    }
}