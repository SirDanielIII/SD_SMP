//package com.sirdanieliii.SD_SMP.commands.coords;
//
//import com.sirdanieliii.SD_SMP.commands.SubCommand;
//import com.sirdanieliii.SD_SMP.configuration.Utilities;
//import dev.dejvokep.boostedyaml.YamlDocument;
//import net.md_5.bungee.api.chat.*;
//import net.md_5.bungee.api.chat.hover.content.Text;
//import org.bukkit.entity.Player;
//
//import java.lang.reflect.Array;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Stream;
//
//import static com.sirdanieliii.SD_SMP.commands.CommandManager.cmdHeader;
//import static com.sirdanieliii.SD_SMP.configuration.Configs.*;
//import static com.sirdanieliii.SD_SMP.configuration.Utilities.replaceVariableInStr;
//import static com.sirdanieliii.SD_SMP.configuration.Utilities.translateColourCodes;
//
//public class coordsClearBACKUP extends SubCommand {
//    @Override
//    public String getName() {
//        return "clear";
//    }
//
//    @Override
//    public String getDescription() {
//        return "&7Clears saved coordinate(s)";
//    }
//
//    @Override
//    public String getSyntax() {
//        return "&6" + errorMessages.get("coords_clear");
//    }
//
//    @Override
//    public boolean perform(Player player, String[] args) {
//        if (!(player.hasPermission("sd_smp.coords.clear"))) {
//            player.sendMessage(errorMessage("permission"));
//            return false;
//        }
//        YamlDocument config = getPlayerConfig(player);
//        // No Dimension Given
//        if (args.length == 1) {
//            player.sendMessage(errorMessage("coords_clear"));
//            return false;
//        } else if (args.length == 2) {
//            if (args[1].equalsIgnoreCase("all")) { // /coords clear all
//                config.set("coordinates.overworld", new Array[]{});
//                config.set("coordinates.nether", new Array[]{});
//                config.set("coordinates.the_end", new Array[]{});
//                savePlayerConfig(config);
//                player.sendMessage(translateColourCodes(cmdHeader("coords") + "&CCleared &BALL &Fcoordinates"));
//            } else { // /coords clear name
//                ArrayList<String> dimensions = nameMatchReturnDimension(config, args[1]); // Return dimensions that have coords with the same name as args[1]
//                if (dimensions.isEmpty()) { // [ERROR] If no coordinate was found
//                    player.sendMessage(errorMessage("invalid_coords_1"));
//                    return false;
//                } else if (dimensions.size() == 1) { // Single coordinate
//                    String coords = getFullCoords(config, args[1], dimensions.get(0));
//                    if (deleteConfigKey(config, "coordinates." + dimensions.get(0) + "." + args[1]))
//                        player.sendMessage(translateColourCodes(cmdHeader("coords") +
//                                "&CCleared &B" + args[1] + " &6[&F" + coords + "&6] &Ffrom " + returnDimensionClr(dimensions.get(0), false)));
//                    return true;
//                } else { // In the case of multiple coordinates / duplicate names
//                    player.sendMessage(translateColourCodes("------------ | &6&LCOORDS CLEAR &R&F| ------------>"));
//                    player.sendMessage(translateColourCodes(errorHeader + errorClr + " " + Utilities.replaceVariableInStr(errorMessages.get("coords_duplicate_1"),
//                            "{coord_name}", args[1])));
//                    ComponentBuilder choices = new ComponentBuilder();
//                    for (String i : dimensions) {
//                        String coords = getFullCoords(config, args[1], i.toLowerCase());
//                        TextComponent choice = new TextComponent(translateColourCodes(">>> " + returnClr(i) + "[&F" + coords + returnClr(i) + "]"));
//                        choice.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/coords clear " + args[1] + " " + i));
//                        choice.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(translateColourCodes("&7Delete from " + returnDimensionClr(i, false)))));
//                        choices.append(choice); // Add clickable messages per dimension
//                    }
//                    for (BaseComponent i : choices.getParts()) player.spigot().sendMessage(i); // Output all text components
//                    player.sendMessage("<-------------------------------------------------->");
//                    return true;
//                }
//            }
//        } else if (args.length >= 3) { // If dimension is specified
//            if (args[1].equalsIgnoreCase("all")) { // /coords clear all [dimensions]
//                ArrayList<String> dimensions = new ArrayList<>();
//                for (int i = 2; i < args.length; i++) if (Stream.of("overworld", "nether", "the_end").anyMatch(args[i]::equalsIgnoreCase)) dimensions.add(args[i]);
//                Set<String> matches = new HashSet<>(dimensions); // Get rid of duplicates
//                if (matches.size() == 0) { // If no valid dimensions were found
//                    player.sendMessage(errorMessage("invalid_dimension_2"));
//                    return false;
//                }
//                // Valid dimensions were found
//                for (String i : matches) config.set("coordinates." + i, new Array[]{});
//                savePlayerConfig(config);
//                ArrayList<String> dimensionsClr = new ArrayList<>();
//                for (String i : matches) dimensionsClr.add(returnDimensionClr(i, false)); // Get coloured dimension text
//                player.sendMessage(translateColourCodes(cmdHeader("coords") + "&CCleared &BALL &Fcoordinates from " + String.join(" & ", dimensionsClr)));
//                return true;
//            } else {  // /coords clear name overworld
//                if (Stream.of("overworld", "nether", "the_end").noneMatch(args[2]::equalsIgnoreCase)) {
//                    player.sendMessage(errorMessage("invalid_dimension_1"));
//                    return false;
//                }
//                if (getCoordValue(config, args[2].toLowerCase(), args[1], "x") == 0) {
//                    player.sendMessage(replaceVariableInStr(errorMessage("invalid_coords_2"),
//                            "{coord_name}", args[1],
//                            "{dimension}", returnDimensionClr(args[2], false)));
//                    return false;
//                }
//                String coords = getFullCoords(config, args[1], args[2].toLowerCase());
//                if (deleteConfigKey(config, "coordinates." + args[2].toLowerCase() + "." + args[1]))
//                    player.sendMessage(translateColourCodes(
//                            cmdHeader("coords") + "&CCleared &B" + args[1] + " &6[&F" + coords + "&6] &Ffrom " + returnDimensionClr(args[2], false)));
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public List<String> getSubcommandArgs(Player player, String[] args) {
//        // /coords clear <name | all> [dimension(s)]
//        YamlDocument config = getPlayerConfig(player);
//        List<String> list = new ArrayList<>();
//        ArrayList<String> secondArgs = new ArrayList<>(List.of("all"));
//        for (String i : allDimensionsStr) { // Add all coord names to second argument as tab competition
//            try {
//                secondArgs.addAll(config.getSection("coordinates." + i).getRoutesAsStrings(false));
//            } catch (NullPointerException ignored) {
//            }
//        }
//        switch (args.length) {
//            case (2) -> {
//                for (String str : secondArgs) if (str.toLowerCase().startsWith(args[1].toLowerCase())) list.add(str);
//            }
//            case (3) -> {
//                if (args[1].equalsIgnoreCase("all")) {
//                    for (String str : returnNonEmptyDimension(config)) if (str.toLowerCase().startsWith(args[2].toLowerCase())) list.add(str);
//                } else { // Add dimensions only if the name is found in them
//                    for (String str : nameMatchReturnDimension(config, args[1])) if (str.toLowerCase().startsWith(args[2].toLowerCase())) list.add(str);
//                }
//            }
//        }
//        return list;
//    }
//}