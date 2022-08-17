package com.sirdanieliii.SD_SMP.commands.subcommands;

import com.sirdanieliii.SD_SMP.commands.SubCommand;
import dev.dejvokep.boostedyaml.YamlDocument;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.sirdanieliii.SD_SMP.commands.CommandManager.cmdHeader;
import static com.sirdanieliii.SD_SMP.configuration.ConfigManager.*;
import static com.sirdanieliii.SD_SMP.configuration.CoordsUtility.*;
import static com.sirdanieliii.SD_SMP.configuration.Utilities.replaceErrorVariable;

public class coordsClear extends SubCommand {
    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "§7Clears saved coordinate(s)";
    }

    @Override
    public String getSyntax() {
        return "§6" + errorMessages.get("coords_clear");
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (!(player.hasPermission("sd_smp.coords.clear"))) {
            player.sendMessage(errorMessage("permission"));
            return false;
        }
        YamlDocument config = getPlayerConfig(player);
        // No Dimension Given
        if (args.length == 1) {
            player.sendMessage(errorMessage("coords_clear"));
            return false;
        } else if (args.length == 2) {
            if (args[1].equalsIgnoreCase("all")) { // /coords clear all
                config.set("coordinates.overworld", new Array[]{});
                config.set("coordinates.nether", new Array[]{});
                config.set("coordinates.the_end", new Array[]{});
                player.sendMessage(cmdHeader("coords") + "§ASuccessfully §Fcleared §BALL §Fcoordinates");
            } else { // /coords clear name
                ArrayList<String> dimensions = new ArrayList<>();
                for (String dimension : config.getSection("coordinates").getRoutesAsStrings(false)) { // Loop through Overworld, Nether & The End
                    for (String name : config.getSection("coordinates." + dimension).getRoutesAsStrings(false)) // Loop through saved coordinates in dimension
                        if (args[1].equalsIgnoreCase(name)) dimensions.add(dimension); // Add dimension if name matches
                }
                if (dimensions.isEmpty()) { // [ERROR] If no coordinate was found
                    player.sendMessage(errorMessage("invalid_coords_1"));
                    return false;
                } else if (dimensions.size() == 1) { // Single coordinate
                    String coords = getFullCoords(config, args[1], dimensions.get(0));
                    config.set("coordinates." + dimensions.get(0) + "." + args[1], new Array[]{});
                    savePlayerConfig(config);
                    player.sendMessage(cmdHeader("coords") + "§ASuccessfully §Fcleared §B" + args[1] + " §6[§F" + coords + "§6] §Fin " + returnDimensionClr(dimensions.get(0)));
                    return true;
                } else { // In the case of multiple coordinates / duplicate names
                    player.sendMessage(cmdHeader("coords") + "§C" + errorMessages.get("coords_duplicate"));
                    ComponentBuilder choices = new ComponentBuilder();
                    StringBuilder dimensionLst = new StringBuilder();
                    for (String i : dimensions) {
                        TextComponent choice = new TextComponent("§C[" + returnDimensionClr(i) + "§C] ");
                        choice.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/coords clear " + args[1] + " " + i));
                        choice.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§8Delete from " + returnDimensionClr(i).substring(2))));
                        choices.append(choice); // Add clickable messages per dimension
                        dimensionLst.append(returnDimensionClr(i).substring(2));
                    }
                    TextComponent lastChoice = new TextComponent("§C[ §BALL CHOICES §C]");
                    lastChoice.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/coords clear all " + String.join(" ", dimensions)));
                    lastChoice.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§8Delete from " + String.join(", ", dimensionLst))));
                    choices.append(lastChoice);
                    player.spigot().sendMessage(choices.getCurrentComponent()); // Chain multiple text components together in one message
                    return true;
                }
            }
        } else if (args.length >= 3) { // If dimension is specified
            if (args[1].equalsIgnoreCase("all")) { // /coords clear all [dimensions]
                ArrayList<String> dimensions = new ArrayList<>();
                for (int i = 2; i < args.length - 1; i++) if (Stream.of("overworld", "nether", "the_end").anyMatch(args[i]::equalsIgnoreCase)) dimensions.add(args[i]);
                Set<String> matches = new HashSet<>(dimensions); // Get rid of duplicates
                if (matches.size() == 0) { // If no valid dimensions were found
                    player.sendMessage(errorMessage("invalid_dimension_2"));
                    return false;
                }
                // Valid dimensions were found
                for (String i : matches) config.set("coordinates." + i, new Array[]{});
                savePlayerConfig(config);
                ArrayList<String> dimensionsClr = new ArrayList<>();
                for (String i : matches) dimensionsClr.add(returnDimensionClr(i)); // Get coloured dimension text
                player.sendMessage(cmdHeader("coords") + "§ASuccessfully §Fcleared all coordinates in " + String.join(" & ", dimensionsClr));
                return true;
            } else {  // /coords clear name overworld
                if (Stream.of("overworld", "nether", "the_end").noneMatch(args[2]::equalsIgnoreCase)) {
                    player.sendMessage(errorMessage("invalid_dimension_1"));
                    return false;
                }
                if (getCoordValue(config, args[2].toLowerCase(), args[1], "x") == 0) {
                    player.sendMessage(replaceErrorVariable(errorMessage("invalid_coords_2"), args[1], returnDimensionClr(args[2])));
                    return false;
                }
                config.set("coordinates." + args[2].toLowerCase() + "." + args[1], null);
                savePlayerConfig(config);
                player.sendMessage(cmdHeader("coords") + "§ASuccessfully §Fcleared " + args[1] + "from " + returnDimensionClr(args[2]));
            }
        }
        return true;
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        // /coords clear <name> [Dimension] §7or §6/coords clear all [Dimension]
        return null;
    }
}