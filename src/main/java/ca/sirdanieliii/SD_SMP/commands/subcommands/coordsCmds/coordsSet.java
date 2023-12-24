package ca.sirdanieliii.SD_SMP.commands.subcommands.coordsCmds;

import ca.sirdanieliii.SD_SMP.configuration.ConfigManager;
import ca.sirdanieliii.SD_SMP.configuration.configs.ConfigPlayer;
import ca.sirdanieliii.SD_SMP.commands.SubCommand;
import ca.sirdanieliii.SD_SMP.coords.Coord;
import ca.sirdanieliii.SD_SMP.coords.Dimension;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static ca.sirdanieliii.SD_SMP.utilities.Utilities.cleanStrForYMLKey;
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
        if (!(player.hasPermission("sd_smp.coords.set"))) {
            player.sendMessage(ConfigManager.errorMessage("permission"));
            return false;
        }
        ConfigPlayer config = new ConfigPlayer(player);
        switch (args.length) {
            case (1) -> { // coords set
                player.sendMessage(ConfigManager.errorMessage("coords_set"));
                return false;
            }
            case (2) -> { // /coords set name
                player.sendMessage(ConfigManager.errorMessage("no_args_to_set_coord"));
                return false;
            }
            case (3) -> { // /coords set name here or /coords set name <x>
                if (args[2].equalsIgnoreCase("here")) {
                    Coord coord;
                    try {
                        coord = new Coord(player, player.getWorld().getUID(), args[1]);
                    } catch (NullPointerException | NumberFormatException exception) {
                        return false;
                    }
                    return coord.save(config, false);
                }
                player.sendMessage(ConfigManager.errorMessage("invalid_xyz"));
                return false;
            }
            case (4) -> { // /coords set name <x> <y>
                String name = cleanStrForYMLKey(args[1]);
                if (name == null) {
                    player.sendMessage(ConfigManager.errorMessage("special_characters"));
                    return false;
                }
                if (!args[2].equalsIgnoreCase("here") && !args[3].equals("--force")) {
                    player.sendMessage(ConfigManager.errorMessage("invalid_xyz"));
                    return false;
                }
                // /coords set name here --force
                Coord coord;
                try {
                    coord = new Coord(player, player.getWorld().getUID(), args[1]);
                } catch (NullPointerException | NumberFormatException exception) {
                    return false;
                }
                coord.save(config, true);
                return true;
            }
            case (5) -> { // /coords set name <x> <y> <z>
                Coord coord;
                try {
                    coord = new Coord(player, player.getWorld().getUID(), args[1], args[2], args[3], args[4]);
                } catch (NullPointerException | NumberFormatException exception) {
                    return false;
                }
                coord.save(config, false);
            }
            case (6) -> {
                /*
                /coords set name <x> <y> <z> <overworld | nether | the_end>
                   -> The 6th argument here refers to the main world's Overworld, Nether & The End.
                 */
                Coord coord;
                try {
                    coord = new Coord(player, args[1], args[2], args[3], args[4], args[5]);
                } catch (NullPointerException | NumberFormatException exception) {
                    return false;
                }
                coord.save(config, false);
            }
            case (7) -> {
                /*
                /coords set name <x> <y> <z> <overworld | nether | the_end> <world>
                   -> The 6th argument here refers to the main world's Overworld, Nether & The End.
                   -> The 7th argument here is a specific world. arg[5] will filter the tab-completion to
                      show worlds that are only of that dimension.
                /coords set name <x> <y> <z> <overworld | nether | the_end> --force
                 */
                Coord coord;

                if (args[6].equals("--force")) {
                    try {
                        coord = new Coord(player, args[1], args[2], args[3], args[4], args[5]);
                    } catch (NullPointerException | NumberFormatException exception) {
                        return false;
                    }
                    coord.save(config, true);
                } else {
                    try {
                        coord = new Coord(player, args[1], args[2], args[3], args[4], args[5], args[6]);
                    } catch (NullPointerException | NumberFormatException exception) {
                        return false;
                    }
                    coord.save(config, false);
                }
            }
            // Number of arguments are more than 7
            default -> {
                player.sendMessage(replaceStr(ConfigManager.errorMessage("too_many_arguments"), "{cmd_syntax}", ConfigManager.generalMsgs.get("coords_set")));
            }
        }
        return false;
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        // /coords set <name> <X Y Z> [dimension] [world]
        ArrayList<String> secondArgs = new ArrayList<>(List.of("name"));
        ArrayList<String> thirdArgs = new ArrayList<>(List.of("here", "~"));
        ArrayList<String> fourthArgs = new ArrayList<>(List.of("~"));
        ArrayList<String> fifthArgs = new ArrayList<>(List.of("~"));
        ArrayList<String> sixthArgs = new ArrayList<>(List.of("overworld", "nether", "the_end"));
        ArrayList<World> allWorlds = new ArrayList<>();
        for (World w : Bukkit.getWorlds()) {
            allWorlds.add(w);
            if (Dimension.CUSTOM.equals(w.getEnvironment())) {
                sixthArgs.add("custom");
            }
        }
        List<String> list = new ArrayList<>();
        switch (args.length) {
            case (2) -> {
                for (String str : secondArgs) if (str.toLowerCase().startsWith(args[1].toLowerCase())) list.add(str);
            }
            case (3) -> {
                for (String str : thirdArgs) if (str.toLowerCase().startsWith(args[2].toLowerCase())) list.add(str);
            }
            case (4) -> {
                if (!args[2].equalsIgnoreCase("here")) {
                    for (String str : fourthArgs) if (str.toLowerCase().startsWith(args[3].toLowerCase())) list.add(str);
                }
            }
            case (5) -> {
                if (!args[2].equalsIgnoreCase("here"))
                    for (String str : fifthArgs) if (str.toLowerCase().startsWith(args[4].toLowerCase())) list.add(str);
            }
            case (6) -> {
                if (!args[2].equalsIgnoreCase("here")) {
                    for (String str : sixthArgs) if (str.toLowerCase().startsWith(args[5].toLowerCase())) list.add(str);
                    for (World w : allWorlds) if (w.getName().startsWith(args[5])) list.add(w.getName()); // World names are case-sensitive
                }
            }
            case (7) -> { // /coords set name <x> <y> <z> <overworld | nether | the_end> <world>
                if (!args[2].equalsIgnoreCase("here")) {
                    if (sixthArgs.contains(args[5].toLowerCase())) {
                        Dimension target = Dimension.getDimensionEnum(args[5]);
                        if (target != null) {
                            for (World w : allWorlds) {
                                if (target.equals(w.getEnvironment())) if (w.getName().startsWith(args[6])) list.add(w.getName());
                            }
                        }
                    }
                }
            }
        }
        return list;
    }
}