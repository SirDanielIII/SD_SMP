package ca.sirdanieliii.SD_SMP.commands.subcommands.coordsCmds;

import ca.sirdanieliii.SD_SMP.commands.CommandManager;
import ca.sirdanieliii.SD_SMP.commands.SubCommand;
import ca.sirdanieliii.SD_SMP.configuration.ConfigManager;
import ca.sirdanieliii.SD_SMP.configuration.ConfigYML;
import ca.sirdanieliii.SD_SMP.configuration.configs.ConfigPlayer;
import ca.sirdanieliii.SD_SMP.coords.Coord;
import ca.sirdanieliii.SD_SMP.coords.Dimension;
import ca.sirdanieliii.SD_SMP.utilities.ChatPaginator;
import ca.sirdanieliii.SD_SMP.utilities.Utilities;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
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
            case (1) -> { // coords list
                player.sendMessage(errorMessage("coords_list"));
                return false;
            }
            case (2) -> { // /coords list <name|all>
                if (args[1].equalsIgnoreCase("all")) {
                    showAllCoords(config, player, 0);
                    return true;
                }
                // Do name logic here
                Coord coord = retrieveWorldCoord(config, player, args[1]);
                if (coord != null) {
                    coord.displayCoordToPlayer();
                    return true;
                }
                player.sendMessage(errorMessage("invalid_coords_1"));
                return false;
            }
            case (3) -> { // /coords list <name|all> dimension or /coords list all <page>
                /*
                 * All Possible Cases
                 *
                 * /coords list name --SHOWALL
                 * /coords list name <dimension>
                 * /coords list name <invalid dimension>
                 * /coords list all <page>
                 * /coords list all <dimension>
                 * /coords list all <invalid dimension>
                 */
                if (args[1].equalsIgnoreCase("all")) {
                    if (isStringNumber(args[2])) {
                        showAllCoords(config, player, Integer.parseInt(args[2]));
                        return true;
                    }
                    Dimension dimension = Dimension.getDimensionEnum(args[2]);
                    if (dimension == null) {
                        player.sendMessage(errorMessage("invalid_dimension"));
                        return false;
                    }
                    // show all coords for dimension
                    List<Coord> coords = retrieveWorldCoords(config, player);
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
                }
                if (args[2].equals("--SHOWALL")) {
                    retrieveCoordNameMultiple(config, player, args[1]);
                    return true;
                }
                if (args[1].equalsIgnoreCase("all")) {
                    showAllCoords(config, player, 0);
                    return true;
                }
                player.sendMessage(errorMessage("no_args_to_set_coord"));
                return false;

            }
            case (4) -> { // /coords list <name|all> [dimension] [world] or /coords list all <dimension> <page>

            }
            // Number of arguments are more than 7
            default -> {
                player.sendMessage(Utilities.replaceStr(errorMessage("too_many_arguments"), "{cmd_syntax}", ConfigManager.generalMsgs.get("coords_list")));
            }
        }
        return true;
    }

    private ArrayList<ArrayList<String>> getAllCoords(ConfigPlayer config, String[] dimensions) {
        ArrayList<ArrayList<String>> coordsAll = new ArrayList<>();
        return coordsAll;
    }

    private String isAtMSG(ConfigPlayer config, String dimension, String name) {
        return "";
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        // /coords list <name | all> <dimension>
        ConfigPlayer config = new ConfigPlayer(player);
        ArrayList<String> secondArgs = new ArrayList<>(List.of("all"));
        List<String> list = new ArrayList<>();
        return list;
    }


    private void showAllCoords(ConfigPlayer config, Player player, int page) {
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
                            int x = coordSection.getInt("x");
                            int y = coordSection.getInt("y");
                            int z = coordSection.getInt("z");
                            Coord coord = new Coord(player, worldID, name, x, y, z);

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


    @Nullable
    private List<Coord> retrieveCoordNameMultiple(ConfigYML config, Player player, String name) {
        List<Coord> coords = new ArrayList<>();
        // Go through every coord and check for ones with the same name
        ConfigurationSection coordinatesSection = config.getConfig().getConfigurationSection("coordinates");
        if (coordinatesSection != null) {
            for (String id : coordinatesSection.getKeys(false)) {
                World world = Bukkit.getWorld(id);
                if (world == null) {
                    return null;
                }
                UUID worldID = world.getUID();
                ConfigurationSection worldCoordsSection = coordinatesSection.getConfigurationSection(id + ".coords");

                if (worldCoordsSection != null) {
                    ConfigurationSection coordSection = worldCoordsSection.getConfigurationSection(name);
                    if (coordSection != null) {
                        int x = coordSection.getInt("x");
                        int y = coordSection.getInt("y");
                        int z = coordSection.getInt("z");
                        coords.add(new Coord(player, worldID, name, x, y, z));


                    }
                }
            }
        }
        return coords;
    }

    @Nullable
    private Coord retrieveWorldCoord(ConfigYML config, Player player, String name) {
        World world = player.getWorld();
        // Go through every coord and check for ones with the same name
        ConfigurationSection coordsSection = config.getConfig().getConfigurationSection(String.format("coordinates.%s.coords", world.getUID()));
        if (coordsSection != null) {
            for (String coordName : coordsSection.getKeys(false)) {
                if (name.equals(coordName)) {
                    ConfigurationSection coordSection = coordsSection.getConfigurationSection(name);
                    if (coordSection != null) {
                        int x = coordSection.getInt("x");
                        int y = coordSection.getInt("y");
                        int z = coordSection.getInt("z");
                        return new Coord(player, world.getUID(), name, x, y, z);
                    }
                }
            }
        }
        return null;
    }

    private List<Coord> retrieveWorldCoords(ConfigYML config, Player player) {
        World world = player.getWorld();
        List<Coord> coords = new ArrayList<>();
        // Go through every coord and check for ones with the same name
        ConfigurationSection coordsSection = config.getConfig().getConfigurationSection(String.format("coordinates.%s.coords", world.getUID()));
        if (coordsSection != null) {
            for (String coordName : coordsSection.getKeys(false)) {
                ConfigurationSection coordSection = coordsSection.getConfigurationSection(coordName);
                if (coordSection != null) {
                    int x = coordSection.getInt("x");
                    int y = coordSection.getInt("y");
                    int z = coordSection.getInt("z");
                    coords.add(new Coord(player, world.getUID(), coordName, x, y, z));
                }
            }
        }
        return coords;
    }
}
