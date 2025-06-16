//package com.sirdanieliii.SD_SMP.commands.coords;
//
//import com.sirdanieliii.SD_SMP.commands.SubCommand;
//import dev.dejvokep.boostedyaml.YamlDocument;
//import org.apache.commons.lang.StringUtils;
//import org.bukkit.Bukkit;
//import org.bukkit.entity.Player;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Stream;
//
//import static com.sirdanieliii.SD_SMP.commands.CommandManager.cmdHeader;
//import static com.sirdanieliii.SD_SMP.configuration.Configs.*;
//import static com.sirdanieliii.SD_SMP.configuration.Utilities.replaceVariableInStr;
//import static com.sirdanieliii.SD_SMP.configuration.Utilities.translateColourCodes;
//
//public class coordsSendBACKUP extends SubCommand {
//    @Override
//    public String getName() {
//        return "send";
//    }
//
//    @Override
//    public String getDescription() {
//        return "&7Sends a saved coordinate or your current location to other player(s)";
//    }
//
//    @Override
//    public String getSyntax() {
//        return "&6" + errorMessages.get("coords_send");
//    }
//
//    @Override
//    public boolean perform(Player player, String[] args) {
//        if (!(player.hasPermission("sd_smp.coords.send"))) {
//            player.sendMessage(errorMessage("permission"));
//            return false;
//        }
//        YamlDocument config = getPlayerConfig(player);
//        if (args.length == 1) {
//            player.sendMessage(errorMessage("coords_send"));
//            return false;
//        } else if (args.length == 2) { // /coords send <word>
//            if (args[1].equalsIgnoreCase("here")) player.sendMessage(errorMessage("missing_players"));
//            else player.sendMessage(errorMessage("no_args_to_send_coord"));
//            return false;
//        } else if (args.length == 3) { // /coords send here [player]   |   /coords send home [dimension] [player]
//            if (args[1].equalsIgnoreCase("here")) {
//                sendCoords(config, player, args, 2);
//                return true;
//            }
//            if (Stream.of("overworld", "nether", "the_end").anyMatch(args[2]::equalsIgnoreCase)) player.sendMessage(errorMessage("missing_players"));
//            else player.sendMessage(errorMessage("invalid_dimension_1"));  // If the correct dimension is not valid
//            return false;
//        } else {  // /coords send home [dimension] [player]
//            if (args[1].equalsIgnoreCase("here")) {
//                sendCoords(config, player, args, 2);
//                return true;
//            }
//            if (Stream.of("overworld", "nether", "the_end").noneMatch(args[2]::equalsIgnoreCase)) {
//                player.sendMessage(errorMessage("invalid_dimension_1"));
//                return false;
//            }
//            sendCoords(config, player, args, 3);
//        }
//        return true;
//    }
//
//    protected void sendCoords(YamlDocument config, Player player, String[] args, int startArg) {
//        ArrayList<Integer> coords = new ArrayList<>();
//        String dimension;
//        if (args[1].equalsIgnoreCase("here")) {
//            coords = getCurrentCoords(player);
//            dimension = returnDimensionClr(translateDimensionToStr(player.getWorld().getEnvironment()), false).substring(2);
//        } else { // Name & dimension are given
//            if (getCoordValue(config, args[2].toLowerCase(), args[1], "x") == 0) { // Coordinate does not exist
//                player.sendMessage(replaceVariableInStr(errorMessage("invalid_coords_2"),
//                        "{coord_name}", args[1],
//                        "{dimension}", returnDimensionClr(args[2], false).substring(2)));
//                return;
//            }
//            for (String i : Arrays.asList("x", "y", "z")) coords.add(getCoordValue(config, args[2].toLowerCase(), args[1], i));
//            dimension = returnDimensionClr(args[2].toLowerCase(), false).substring(2);
//        }
//        ArrayList<String> sentValid = new ArrayList<>();
//        ArrayList<String> sentInvalid = new ArrayList<>();
//        boolean sentToYourself = false;
//        for (int i = startArg; i < args.length; i++) {
//            Player target = Bukkit.getPlayerExact(args[i]);
//            if (target != null) { // If player is valid
//                if (!sentValid.contains(target.getDisplayName())) { // Player not found in sentValid (first occurrence)
//                    if (target == player) {
//                        sentToYourself = true;
//                    } else { // Send coordinate to player
//                        target.sendMessage(translateColourCodes("&7&O" + player.getDisplayName() + " whispers to you: "));
//                        if (args[1].equalsIgnoreCase("here")) target.sendMessage(translateColourCodes(
//                                "&7&O→ I am currently at [" + coords.get(0) + " " + coords.get(1) + " " + coords.get(2) + "] in " + dimension));
//                        else target.sendMessage(translateColourCodes(
//                                "&7&O→ " + args[1] + " is at [" + coords.get(0) + " " + coords.get(1) + " " + coords.get(2) + "] in " + dimension));
//                        sentValid.add(target.getDisplayName()); // Add to list of valid players to avoid duplicate messaging
//                    }
//                }
//            } else if (!sentInvalid.contains(args[i])) sentInvalid.add((args[i])); // Add null player to a set of invalid players
//        }
//        // If only specified argument was the player itself
//        if (sentToYourself && sentValid.size() == 0 && sentInvalid.size() == 0) {
//            player.sendMessage(errorMessage("sent_to_yourself"));
//            return;
//        }
//        // Otherwise:
//        player.sendMessage(translateColourCodes("------------ | &6&LCOORDS Send &R&F| ------------>"));
//        if (sentValid.size() == 1) {
//            player.sendMessage(translateColourCodes(cmdHeader("coords") +
//                    "&ACoordinate successfully sent to &B" + sentValid.get(0)));
//        } else if (sentValid.size() > 1) {
//            player.sendMessage(translateColourCodes(cmdHeader("coords") +
//                    "&ACoordinate successfully sent to &B" + StringUtils.join(sentValid.subList(0, sentValid.size() - 1), ", "))
//                    + " and " + sentValid.get(sentValid.size() - 1));
//        }
//        if (sentInvalid.size() == 1) player.sendMessage(translateColourCodes(replaceVariableInStr(errorMessage("player_not_online_1"),
//                "{player}", sentInvalid.get(0))));
//        else if (sentInvalid.size() > 1) player.sendMessage(translateColourCodes(replaceVariableInStr(errorMessage("player_not_online_2"),
//                "{player}", StringUtils.join(sentInvalid.subList(0, sentInvalid.size() - 1), ", ") + " and " + sentInvalid.get(sentInvalid.size() - 1))));
//        if (sentToYourself) player.sendMessage(errorMessage("sent_to_yourself"));
//        player.sendMessage("<-------------------------------------------------->");
//    }
//
//    @Override
//    public List<String> getSubcommandArgs(Player player, String[] args) {
//        // /coords send <here> [players] OR /coords send <name> <dimension> [players]>
//        YamlDocument config = getPlayerConfig(player);
//        ArrayList<String> secondArgs = new ArrayList<>(List.of("here"));
//        List<String> list = new ArrayList<>();
//        for (String i : allDimensionsStr) {
//            try {
//                secondArgs.addAll(config.getSection("coordinates." + i).getRoutesAsStrings(false));
//            } catch (NullPointerException ignored) {
//            }
//        }
//        if (args.length == 2) {
//            for (String str : secondArgs) if (str.startsWith(args[1])) list.add(str); // Case-sensitive
//            return list;
//        }
//        if (args.length == 3) {
//            if (!args[1].equalsIgnoreCase("here")) { // Add dimensions only if the name is found in them
//                for (String str : nameMatchReturnDimension(config, args[1])) if (str.toLowerCase().startsWith(args[2].toLowerCase())) list.add(str);
//                return list;
//            }
//        }
//        for (Player p : Bukkit.getOnlinePlayers())
//            for (int i = 2; i < args.length; i++) if (p.getDisplayName().toLowerCase().startsWith(args[i].toLowerCase())) list.add(p.getDisplayName());
//        return list;
//    }
//}
