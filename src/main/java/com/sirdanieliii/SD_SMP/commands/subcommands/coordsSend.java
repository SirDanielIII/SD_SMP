package com.sirdanieliii.SD_SMP.commands.subcommands;

import com.sirdanieliii.SD_SMP.commands.SubCommand;
import org.bukkit.entity.Player;

public class coordsSend extends SubCommand {
    @Override
    public String getName() {
        return "send";
    }

    @Override
    public String getDescription() {
        return "§7Send saved coordinate to other player(s)";
    }

    @Override
    public String getSyntax() {
        return "§6/coords send <name> <dimension> <player> [More Players]\n";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        player.sendMessage("This is currently broken");
//        if (args.length == 1) {
//            player.sendMessage("§C[!] Missing type, dimension and player name(s)");
//        } else {
//            if ((Stream.of("home", "overworld", "portal", "nether", "all").anyMatch(args[1]::equalsIgnoreCase))) {
//                if (args.length == 2) {
//                    player.sendMessage("§C[!] Missing player name(s)");
//                } else {
//                    String type = args[1].toLowerCase();
//                    ArrayList<String> messages = new ArrayList<>();
//                    if (Stream.of("home", "overworld").anyMatch(type::equalsIgnoreCase)) {
//                        messages.add("§7§O--> " + toTitleCase(type) + " " + retrievePlayerCoords(player, type, "§O", "§O", "§O"));
//                    } else if (Stream.of("nether").anyMatch(type::equalsIgnoreCase)) {
//                        messages.add("§7§O--> " + toTitleCase(type) + " " + retrievePlayerCoords(player, type, "§O", "§O", "§O"));
//                    } else if (Stream.of("portal").anyMatch(type::equalsIgnoreCase)) {
//                        messages.add("§7§O--> " + "Nether Portal " + retrievePlayerCoords(player, type, "§O", "§O", "§O"));
//                    } else if (Stream.of("all").anyMatch(type::equalsIgnoreCase)) {
//                        messages.add("§7§O--> " + "Home " + retrievePlayerCoords(player, "home", "§O", "§O", "§O"));
//                        messages.add("§7§O--> " + "Overworld " + retrievePlayerCoords(player, "overworld", "§O", "§O", "§O"));
//                        messages.add("§7§O--> " + "Nether Portal " + retrievePlayerCoords(player, "portal", "§O", "§O", "§O"));
//                        messages.add("§7§O--> " + "Nether " + retrievePlayerCoords(player, "nether", "§O", "§O", "§O"));
//                    }
//                    for (int i = 0; i < args.length - 2; i++) {
//                        Player target = Bukkit.getPlayer(args[i + 2]);
//                        if (target == null) {
//                            player.sendMessage("§C[!] " + toTitleCase(args[i + 2]) + " isn't online or does not exist!");
//                        } else if (target == player) {
//                            player.sendMessage("§6[§FCoords§6] §FWhy would you send the coords to yourself lol");
//                        } else {
//                            target.sendMessage("§7§O" + player.getDisplayName() + " whispers to you: ");
//                            player.sendMessage("§6[§FCoords§6] §AMessage successfully sent to " + target.getDisplayName());
//                            for (String message : messages) {
//                                target.sendMessage(message);
//                            }
//                        }
//                    }
//                }
//            } else {
//                player.sendMessage("§C[!] Send Type: §Ahome §F/ §Aoverworld §F/ §Dportal §F/ §Dnether §F/ §Ball");
//            }
//        }
        return true;
    }
}
