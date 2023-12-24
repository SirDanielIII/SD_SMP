//package com.sirdanieliii.SD_SMP.commands.subcommands.coordsCmds;
//
//import com.sirdanieliii.SD_SMP.commands.SubCommand;
//import com.sirdanieliii.SD_SMP.configuration.ConfigYML;
//import com.sirdanieliii.SD_SMP.configuration.configs.ConfigPlayer;
//import net.md_5.bungee.api.chat.ClickEvent;
//import net.md_5.bungee.api.chat.HoverEvent;
//import net.md_5.bungee.api.chat.TextComponent;
//import net.md_5.bungee.api.chat.hover.content.Text;
//import org.bukkit.World;
//import org.bukkit.entity.Player;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//import java.util.stream.Stream;
//
//import static com.sirdanieliii.SD_SMP.SD_SMP.getThisPluginServer;
//import static com.sirdanieliii.SD_SMP.commands.CommandManager.cmdClr;
//import static com.sirdanieliii.SD_SMP.commands.CommandManager.cmdHeader;
//import static com.sirdanieliii.SD_SMP.configuration.ConfigManager.*;
//import static com.sirdanieliii.SD_SMP.coords.CoordsUtility.*;
//import static com.sirdanieliii.SD_SMP.utilities.Utilities.*;
//
//public class coordsSetBACKUP2 extends SubCommand {
//    @Override
//    public String getName() {
//        return "set";
//    }
//
//    @Override
//    public String getCmdGroup() {
//        return "coords";
//    }
//
//    @Override
//    public String getDescription() {
//        return "Saves a coordinate under given name";
//    }
//
//    @Override
//    public String getSyntax() {
//        return generalMsgs.get("coords_set");
//    }
//
//    @Override
//    public boolean perform(Player player, String[] args) {
//        if (!(player.hasPermission("sd_smp.coords.set"))) {
//            player.sendMessage(errorMessage("permission"));
//            return false;
//        }
//        ConfigPlayer config = new ConfigPlayer(player);
//        switch (args.length) {
//            case (1) -> { // coords set
//                player.sendMessage(errorMessage("coords_set"));
//                return false;
//            }
//            case (2) -> { // /coords set name
//                player.sendMessage(errorMessage("no_args_to_set_coord"));
//                return false;
//            }
//            case (3) -> { // /coords set name here or /coords set name <x>
//                if (args[2].equalsIgnoreCase("here")) {
//                    String name = cleanStrForYMLKey(args[1]);
//                    if (name == null) {
//                        player.sendMessage(errorMessage("special_characters"));
//                        return false;
//                    }
//                    UUID world = getPlayerWorldUiD(player);
//                    if (!duplicateCoordName(config, world, name)) {
//                        savePlayerCoords(config, player, name, getPlayerCoords(player), world);
//                    } else duplicateFound(player, args, name, getPlayerCoords(player), world);
//                    return true;
//                }
//                player.sendMessage(errorMessage("invalid_xyz"));
//                return false;
//            }
//            case (4) -> { // /coords set name <x> <y>
//                String name = cleanStrForYMLKey(args[1]);
//                if (name == null) {
//                    player.sendMessage(errorMessage("special_characters"));
//                    return false;
//                }
//                if (!args[2].equalsIgnoreCase("here") && !args[3].equalsIgnoreCase("--force")) {
//                    player.sendMessage(errorMessage("invalid_xyz"));
//                    return false;
//                }
//                // /coords set name here --force
//                savePlayerCoords(config, player, name, getPlayerCoords(player), getPlayerWorldUiD(player));
//                return true;
//            }
//            case (5) -> { // /coords set name <x> <y> <z>
//                String name = cleanStrForYMLKey(args[1]);
//                if (name == null) {
//                    player.sendMessage(errorMessage("special_characters"));
//                    return false;
//                }
//                ArrayList<Integer> coords = new ArrayList<>();
//                try {
//                    coords.add(parseNumberArg(args[2], player, 'x'));
//                    coords.add(parseNumberArg(args[3], player, 'y'));
//                    coords.add(parseNumberArg(args[4], player, 'z'));
//                } catch (NumberFormatException e) {
//                    player.sendMessage(errorMessage("coord_str_not_int"));
//                    return false;
//                }
//                boolean duplicateKey = duplicateCoordName(config, getPlayerWorldUiD(player), name);
//                if (!duplicateKey) {
//                    savePlayerCoords(config, player, name, coords, getPlayerWorldUiD(player));
//                    return true;
//                } else {
//                    duplicateFound(player, args, name, coords, getPlayerWorldUiD(player));
//                    return false;
//                }
//            }
//            default -> {
//                String name = cleanStrForYMLKey(args[1]);
//                if (name == null) {
//                    player.sendMessage(errorMessage("special_characters"));
//                    return false;
//                }
//                ArrayList<Integer> coords = new ArrayList<>();
//                try {
//                    coords.add(parseNumberArg(args[2], player, 'x'));
//                    coords.add(parseNumberArg(args[3], player, 'y'));
//                    coords.add(parseNumberArg(args[4], player, 'z'));
//                } catch (NumberFormatException e) {
//                    player.sendMessage(errorMessage("coord_str_not_int"));
//                    return false;
//                }
//                UUID[] defaultWorlds = getDefaultWorldIDs();
//                UUID id = null;
//                if (args.length == 6) {
//                    if (args[5].equalsIgnoreCase("overworld")) {
//                        id = defaultWorlds[0];
//                    } else if (args[5].equalsIgnoreCase("nether")) {
//                        id = defaultWorlds[1];
//                    } else if (args[5].equalsIgnoreCase("the_end")) {
//                        id = defaultWorlds[2];
//                    } else if (args[5].equalsIgnoreCase("custom")) {
//                        player.sendMessage(errorMessage("custom_is_specifier"));
//                        return false;
//                    } else if (worldValid(args[5])) {
//                        id = getWorldUid(args[5]);
//                    }
//                    if (id == null) {
//                        player.sendMessage(replaceStr(errorMessage("invalid_world_1"), "{world}", args[5]));
//                        return false;
//                    }
//                    boolean duplicateKey = duplicateCoordName(config, id, name);
//                    if (!duplicateKey) {
//                        savePlayerCoords(config, player, name, coords, id);
//                        return true;
//                    } else {
//                        duplicateFound(player, args, name, coords, id);
//                        return false;
//                    }
//                } else if (args.length == 7) { // arg[5] must be dimension, arg[6] must be specific world name, unless arg[6] is "--force"
//                    if (worldValid(args[5]) && args[6].equalsIgnoreCase("--force")) {
//                        savePlayerCoords(config, player, name, coords, getWorldUid(args[5]));
//                        return true;
//                    }
//                    if (Stream.of("overworld", "nether", "the_end", "custom").noneMatch(args[5]::equalsIgnoreCase)) {
//                        player.sendMessage(errorMessage("invalid_dimension"));
//                        return false;
//                    }
//                    if (!worldValid(args[6])) {
//                        player.sendMessage(errorMessage("invalid_world_2"));
//                        return false;
//                    }
//                    Dimensions target = Dimensions.getDimensionEnum(args[5]);
//                    if (!target.equals(getWorldDimension(getWorldUid(args[6])))) {
//                        player.sendMessage(errorMessage("invalid_match_dimension"));
//                        return false;
//                    }
//                    // By this point the dimension and world match
//                    boolean duplicateKey = duplicateCoordName(config, getWorldUid(args[6]), name);
//                    if (!duplicateKey) {
//                        savePlayerCoords(config, player, name, coords, getWorldUid(args[6]));
//                        return true;
//                    } else {
//                        duplicateFound(player, args, name, coords, getWorldUid(args[6]));
//                        return false;
//                    }
//                }
//                player.sendMessage(replaceStr(errorMessage("too_many_arguments"), "{cmd_syntax}", generalMsgs.get("coords_set")));
//            }
//        }
//        return false;
//    }
//
//
//    private void duplicateFound(Player player, String[] args, String name, ArrayList<Integer> coords, UUID worldUiD) {
//        player.sendMessage(translateMsgClr("------------ | " + cmdClr("coords", true).toUpperCase() + "COORDS &R&F| ------------>"));
//        TextComponent errorStr = replaceStr(errorMessage("coords_duplicate_2"), Map.of("{coord_name}",
//                getCoordComponent(coords, player), "{world}", getWorldComponent(worldUiD)));
//        player.spigot().sendMessage(errorStr);
//        TextComponent choice = new TextComponent(">>> ");
//        TextComponent confirm = translateMsgClrComponent(String.format("&EClick to confirm overwrite of \"%s\"", name));
//        confirm.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(translateMsgClr("&CThis action is not reversible!"))));
//        if (args[2].equalsIgnoreCase("here")) {
//            confirm.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
//                    "/coords set " + name + " here --force"));
//        } else {
//            confirm.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
//                    "/coords set " + name + " " + args[2] + " " + args[3] + " " + args[4] + " " + getWorldName(worldUiD) + " --force"));
//        }
//        choice.addExtra(confirm);
//        player.spigot().sendMessage(choice);
//        player.sendMessage(blockFooter);
//    }
//
//
//    private void savePlayerCoords(ConfigYML config, Player player, String name, ArrayList<Integer> coords, UUID worldUiD) {
//        addNewWorldToPlayerConfig(config, worldUiD);
//        config.getConfig().set(String.format("coordinates.%s.coords.%s.x", worldUiD, name), coords.get(0));
//        config.getConfig().set(String.format("coordinates.%s.coords.%s.y", worldUiD, name), coords.get(1));
//        config.getConfig().set(String.format("coordinates.%s.coords.%s.z", worldUiD, name), coords.get(2));
//        config.save();
//        TextComponent returnMsg = translateMsgClrComponent(String.format("%s&FSaved &B%s ", cmdHeader("coords"), name));
//        returnMsg.addExtra(getCoordComponent(coords, player));
//        returnMsg.addExtra(translateMsgClrComponent("&Fin "));
//        returnMsg.addExtra(getWorldComponent(worldUiD));
//        player.spigot().sendMessage(returnMsg);
//    }
//
//    @Override
//    public List<String> getSubcommandArgs(Player player, String[] args) {
//        // /coords set <name> [X Y Z] <world>
//        ArrayList<String> secondArgs = new ArrayList<>(List.of("name"));
//        ArrayList<String> thirdArgs = new ArrayList<>(List.of("here", "~"));
//        ArrayList<String> fourthArgs = new ArrayList<>(List.of("~"));
//        ArrayList<String> fifthArgs = new ArrayList<>(List.of("~"));
//        ArrayList<String> sixthArgs = new ArrayList<>(List.of("overworld", "nether", "the_end"));
//        ArrayList<World> allWorlds = new ArrayList<>();
//        for (World w : getThisPluginServer().getWorlds()) {
//            allWorlds.add(w);
//            if (Dimensions.CUSTOM.equals(w.getEnvironment())) {
//                sixthArgs.add("custom");
//            }
//        }
//        List<String> list = new ArrayList<>();
//        switch (args.length) {
//            case (2) -> {
//                for (String str : secondArgs) if (str.toLowerCase().startsWith(args[1].toLowerCase())) list.add(str);
//            }
//            case (3) -> {
//                for (String str : thirdArgs) if (str.toLowerCase().startsWith(args[2].toLowerCase())) list.add(str);
//            }
//            case (4) -> {
//                if (!args[2].equalsIgnoreCase("here")) {
//                    for (String str : fourthArgs) if (str.toLowerCase().startsWith(args[3].toLowerCase())) list.add(str);
//                }
//            }
//            case (5) -> {
//                if (!args[2].equalsIgnoreCase("here"))
//                    for (String str : fifthArgs) if (str.toLowerCase().startsWith(args[4].toLowerCase())) list.add(str);
//            }
//            case (6) -> {
//                if (!args[2].equalsIgnoreCase("here")) {
//                    for (String str : sixthArgs) if (str.toLowerCase().startsWith(args[5].toLowerCase())) list.add(str);
//                    for (World w : allWorlds) if (w.getName().startsWith(args[5])) list.add(w.getName()); // World names are case-sensitive
//                }
//            }
//            case (7) -> {
//                if (!args[2].equalsIgnoreCase("here")) {
//                    if (sixthArgs.contains(args[5].toLowerCase())) {
//                        Dimensions target = Dimensions.getDimensionEnum(args[5]);
//                        for (World w : allWorlds) {
//                            if (target.equals(w.getEnvironment())) if (w.getName().startsWith(args[6])) list.add(w.getName());
//                        }
//                    }
//                }
//            }
//        }
//        return list;
//    }
//}