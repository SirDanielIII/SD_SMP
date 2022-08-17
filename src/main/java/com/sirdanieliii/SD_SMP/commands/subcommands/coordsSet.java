package com.sirdanieliii.SD_SMP.commands.subcommands;

import com.sirdanieliii.SD_SMP.commands.SubCommand;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.sirdanieliii.SD_SMP.commands.CommandManager.cmdHeader;
import static com.sirdanieliii.SD_SMP.configuration.ConfigManager.*;
import static com.sirdanieliii.SD_SMP.configuration.CoordsUtility.*;
import static com.sirdanieliii.SD_SMP.configuration.Utilities.strIntParseCheck;

public class coordsSet extends SubCommand {
    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getDescription() {
        return "§7Saves coordinate under given name";
    }

    @Override
    public String getSyntax() {
        return "§6" + errorMessages.get("coords_set");
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (!(player.hasPermission("sd_smp.coords.set"))) {
            player.sendMessage(errorMessage("permission"));
            return false;
        }
        YamlDocument config = getPlayerConfig(player);
        switch (args.length) {
            case (1) -> {
                player.sendMessage(errorMessage("coords_set"));
                return false;
            }
            case (2) -> {
                player.sendMessage(errorMessage("no_args_to_set_coord")); // /coords set name
                return false;
            }
            case (3) -> { // /coords set name here
                if (args[2].equalsIgnoreCase("here")) {
                    ArrayList<Integer> coords = getCurrentCoords(player);
                    String dimension = translateDimensionToStr(player.getWorld().getEnvironment());
                    savePlayerCoords(config, player, dimension, args[1], coords);
                    return true;
                }
                player.sendMessage(errorMessage("invalid_xyz"));
                return false;
            }
            case (4) -> { // /coords set name x y
                player.sendMessage(errorMessage("invalid_xyz"));
                return false;
            }
            case (5) -> { // /coords set name x y z
                if (strIntParseCheck(Arrays.copyOfRange(args, 2, 4))) player.sendMessage(errorMessage("missing_dimension"));
                else player.sendMessage(errorMessage("coord_str_not_int"));
                return false;
            }
            case (6) -> { // /coords set name x y z <dimension>
                if (!strIntParseCheck(Arrays.copyOfRange(args, 2, 4))) {
                    player.sendMessage(errorMessage("coord_str_not_int"));
                    return false;
                }
                if (Stream.of("overworld", "nether", "the_end").noneMatch(args[5]::equalsIgnoreCase)) {
                    player.sendMessage(errorMessage("invalid_dimension_1"));
                    return false;
                }
                ArrayList<Integer> coords = new ArrayList<>();
                coords.add(Integer.parseInt(args[2]));
                coords.add(Integer.parseInt(args[3]));
                coords.add(Integer.parseInt(args[4]));
                savePlayerCoords(config, player, args[5].toLowerCase(), args[1], coords);
            }
        }
        return true;
    }

    private void savePlayerCoords(YamlDocument config, Player player, String dimension, String name, ArrayList<Integer> coords) {
        config.set("coordinates." + dimension + "." + name + ".x", coords.get(0));
        config.set("coordinates." + dimension + "." + name + ".y", coords.get(1));
        config.set("coordinates." + dimension + "." + name + ".z", coords.get(2));
        savePlayerConfig(config);
        player.sendMessage(cmdHeader("coords") + "§FSaved §B" + name + " §6[§F" +
                coords.get(0) + " " + coords.get(1) + " " + coords.get(2) + "§6]§F in " + returnDimensionClr(dimension));
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        //  §6/coords set <name> here §7or\n§6/coords set <name> <X Y Z> [Dimension]"
        return null;
    }
}