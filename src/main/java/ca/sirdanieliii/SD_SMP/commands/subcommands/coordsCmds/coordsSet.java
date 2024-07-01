package ca.sirdanieliii.SD_SMP.commands.subcommands.coordsCmds;

import ca.sirdanieliii.SD_SMP.commands.SubCommand;
import ca.sirdanieliii.SD_SMP.configuration.ConfigManager;
import ca.sirdanieliii.SD_SMP.configuration.configs.ConfigPlayer;
import ca.sirdanieliii.SD_SMP.coords.Coord;
import ca.sirdanieliii.SD_SMP.coords.Dimension;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static ca.sirdanieliii.SD_SMP.utilities.Utilities.replaceStr;

public class coordsSet extends SubCommand {

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getCmdGroup() {
        return "coords";
    }

    @Override
    public String getDescription() {
        return "Saves a coordinate under given name";
    }

    @Override
    public String getSyntax() {
        return ConfigManager.generalMsgs.get("coords_set");
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (!player.hasPermission("sd_smp.coords.set")) {
            player.sendMessage(ConfigManager.errorMessage("permission"));
            return false;
        }

        ConfigPlayer config = new ConfigPlayer(player);

        return switch (args.length) {
            case 1 -> {
                player.sendMessage(ConfigManager.errorMessage("coords_set"));
                yield false;
            }
            case 2 -> {
                player.sendMessage(ConfigManager.errorMessage("no_args_to_set_coord"));
                yield false;
            }
            case 3 -> {
                if (args[2].equalsIgnoreCase("here")) {
                    yield saveCoord(player, config, args[1], false);
                }
                player.sendMessage(ConfigManager.errorMessage("invalid_xyz"));
                yield false;
            }
            case 4 -> {
                if (args[2].equalsIgnoreCase("here") && args[3].equals("--force")) {
                    yield saveCoord(player, config, args[1], true);
                } else if (args[2].equalsIgnoreCase("here")) { // Argument does not equal "--force"
                    yield tooManyArguments(player);
                }
                player.sendMessage(ConfigManager.errorMessage("invalid_xyz"));
                yield false;
            }
            case 5 -> saveCoord(player, config, args[1], args[2], args[3], args[4]);
            case 6 -> saveCoord(player, config, args[1], args[2], args[3], args[4], args[5]);
            case 7 -> saveCoord(player, config, args[1], args[2], args[3], args[4], args[5], args[6], false);
            case 8 -> {
                if (args[7].equals("--force")) {
                    saveCoord(player, config, args[1], args[2], args[3], args[4], args[5], args[6], true);
                    yield true;
                }
                yield tooManyArguments(player);
            }
            default -> tooManyArguments(player);
        };
    }

    private boolean saveCoord(Player player, ConfigPlayer config, String name, boolean force) {
        try {
            Coord coord = new Coord(player, player.getWorld().getUID(), name);
            return coord.save(config, force);
        } catch (NullPointerException | NumberFormatException e) {
            return false;
        }
    }

    private boolean saveCoord(Player player, ConfigPlayer config, String name, String x, String y, String z) {
        try {
            Coord coord = new Coord(player, player.getWorld().getUID(), name, x, y, z);
            return coord.save(config, false);
        } catch (NullPointerException | NumberFormatException e) {
            return false;
        }
    }

    private boolean saveCoord(Player player, ConfigPlayer config, String name, String x, String y, String z, String dimension) {
        try {
            Coord coord = new Coord(player, name, x, y, z, dimension);
            return coord.save(config, false);
        } catch (NullPointerException | NumberFormatException e) {
            return false;
        }
    }

    private boolean saveCoord(Player player, ConfigPlayer config, String name, String x, String y, String z, String dimension, String world, boolean force) {
        try {
            Coord coord = new Coord(player, name, x, y, z, dimension, world);
            return coord.save(config, force);
        } catch (NullPointerException | NumberFormatException e) {
            return false;
        }
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        List<String> suggestions = new ArrayList<>();
        List<String> dimensions = List.of("overworld", "nether", "the_end");

        switch (args.length) {
            case 2 -> suggestions.add("name");
            case 3 -> suggestions.addAll(List.of("here", "~"));
            case 4, 5 -> suggestions.add("~");
            case 6 -> suggestions.addAll(dimensions);
            case 7 -> {
                if (dimensions.contains(args[5].toLowerCase())) {
                    Dimension dimension = Dimension.getDimensionEnum(args[5]);
                    if (dimension != null) {
                        for (World world : Bukkit.getWorlds()) {
                            if (dimension.equals(world.getEnvironment())) {
                                suggestions.add(world.getName());
                            }
                        }
                    }
                }
            }
        }

        return filterSuggestions(suggestions, args);
    }

    private List<String> filterSuggestions(List<String> suggestions, String[] args) {
        String currentArg = args[args.length - 1].toLowerCase();
        return suggestions.stream()
                .filter(suggestion -> suggestion.toLowerCase().startsWith(currentArg))
                .toList();
    }

    private boolean tooManyArguments(Player player) {
        player.sendMessage(replaceStr(ConfigManager.errorMessage("too_many_arguments"), "{cmd_syntax}", ConfigManager.generalMsgs.get("coords_set")));
        return false;
    }
}
