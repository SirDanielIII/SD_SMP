package com.sirdanieliii.SD_SMP.commands.subcommands;

import com.sirdanieliii.SD_SMP.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.sirdanieliii.SD_SMP.SD_SMP.PLAYER_CONFIG;
import static com.sirdanieliii.SD_SMP.commands.CommandManager.headers;
import static com.sirdanieliii.SD_SMP.configuration.ReturnCoordsData.*;
import static com.sirdanieliii.SD_SMP.events.ErrorMessages.*;
import static com.sirdanieliii.SD_SMP.events.ErrorMessages.dimensionStrings;
import static com.sirdanieliii.SD_SMP.events.Utilities.toTitleCase;

public class coordsSend extends SubCommand {
    @Override
    public String getName() {
        return "send";
    }

    @Override
    public String getDescription() {
        return "§7Sends saved coordinate to other player(s)";
    }

    @Override
    public String getSyntax() {
        return "§6/coords send here <player> [Players] §7or\n§6/coords send <name> <dimension> <player> [Players]";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (args.length == 1) player.sendMessage(incorrectArgs("SEND"));
        else if (args.length == 2) {
            if (args[1].equalsIgnoreCase("here")) player.sendMessage(incorrectArgs("SEND_PLAYERS"));
            else player.sendMessage(incorrectArgs("SEND_DIMENSION"));
        } else if (args.length == 3) {
            if (args[1].equalsIgnoreCase("here")) {
                ArrayList<String> message = new ArrayList<>();
                message.add(formatCoords(returnDimensionString(player.getWorld().getEnvironment().toString()),
                        (int) player.getLocation().getX() + " " + (int) player.getLocation().getY() + " " + (int) player.getLocation().getZ()));
                sendCoords(player, args, message, 2);
            } else {
                if (returnDimensionString(args[2]).equals("null")) player.sendMessage(errorMessage("DIMENSION", "\"" + toTitleCase(args[2]) + "\""));
                else player.sendMessage(incorrectArgs("SEND_PLAYERS"));
            }
        } else sendCoords(player, args, retrieveCoord(player, toTitleCase(args[1]), returnDimensionString(args[2]), false), 3);
        return true;
    }

    private void sendCoords(Player player, String[] args, ArrayList<String> message, int startArg) {
        if (message.size() == 0)
            player.sendMessage(incorrectArgs("COORDINATE", toTitleCase(args[1]), toTitleCase(args[2])));
        else {
            for (int i = startArg; i < args.length; i++) {
                Player target = Bukkit.getPlayer(args[i]);
                if (target == null) player.sendMessage(errorMessage("PLAYER", args[i]));
                else if (target == player) player.sendMessage(incorrectArgs("SEND_NULL"));
                else {
                    target.sendMessage("§7§O" + player.getDisplayName() + " whispers to you: ");
                    for (String coords : message) {
                        target.sendMessage("§7§O→ " + coords);
                    }
                    player.sendMessage(headers("COORDS") + "§FCoordinates §Asuccessfully §Fsent to §B" + target.getDisplayName());
                }
            }
        }
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        // /coords send here <player> [Players] or /coords send <name> <dimension> <player> [Players]
        if (args.length == 2) {
            List<String> types = new ArrayList<>();
            PLAYER_CONFIG.setup("playerdata", player.getUniqueId().toString());
            PLAYER_CONFIG.reload();
            for (String i : dimensionStrings) {
                try {
                    types.addAll(Objects.requireNonNull(PLAYER_CONFIG.getConfig().getConfigurationSection("coordinates." + i)).getKeys(false));
                } catch (Exception ignored) {
                }
            }
            types.add("here");
            return types;
        } else if (args.length == 3) {
            List<String> types = new ArrayList<>();
            if (args[1].equalsIgnoreCase("here")) {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) types.add(toTitleCase(p.getDisplayName()));
            } else {
                PLAYER_CONFIG.setup("playerdata", player.getUniqueId().toString());
                PLAYER_CONFIG.reload();
                for (String i : dimensionStrings) {
                    if (PLAYER_CONFIG.getConfig().getString("coordinates." + i + "." + toTitleCase(args[1])) != null) {
                        types.add(toTitleCase(returnDimensionString(i)));
                    }
                }
            }
            return types;
        }
        return null;
    }
}
