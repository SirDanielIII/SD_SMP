package com.sirdanieliii.SD_SMP.commands.subcommands;

import com.sirdanieliii.SD_SMP.commands.SubCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.sirdanieliii.SD_SMP.SD_SMP.PLAYER_CONFIG;
import static com.sirdanieliii.SD_SMP.commands.CommandManager.headers;
import static com.sirdanieliii.SD_SMP.configuration.ReturnCoordsData.*;
import static com.sirdanieliii.SD_SMP.configuration.ReturnCoordsData.returnDimensionString;
import static com.sirdanieliii.SD_SMP.events.ErrorMessages.*;
import static com.sirdanieliii.SD_SMP.events.Utilities.*;

public class coordsList extends SubCommand {
    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "§7List saved coordinate(s)";
    }

    @Override
    public String getSyntax() {
        return "§6/coords list <name> [Dimension] §7or\n§6/coords list all [Dimension]";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (!(player.hasPermission("sd_smp.coords.list"))) {
            player.sendMessage(errorMessage("PERMISSION"));
            return true;
        }
        if (args[1].equalsIgnoreCase("all")) {
            if (args.length == 2) { // coords list all
                if (retrieveAllCoords(player).size() != 0) {
                    for (String output : retrieveAllCoords(player))
                        player.sendMessage(headers("COORDS") + output);
                } else player.sendMessage(incorrectArgs("LIST_ALL-NULL"));
            } else {  // coords list all [dimension]
                if (!returnDimensionString(args[2]).equals("null")) {
                    if (retrieveAllCoordsDimension(player, returnDimensionString(args[2])).size() != 0) {
                        for (String output : retrieveAllCoordsDimension(player, returnDimensionString(args[2])))
                            player.sendMessage(headers("COORDS") + output);
                    } else player.sendMessage(incorrectArgs("LIST_ALL_D-NULL", toTitleCase(args[2])));
                } else player.sendMessage(errorMessage("DIMENSION"));
            }
            return true;
        }
        // /coords list name [dimension]
        String dimension;
        try {
            dimension = args[2];
            if (returnDimension(dimension) == null) {
                player.sendMessage(errorMessage("DIMENSION", toTitleCase(dimension)));
                return false;
            }
        } catch (Exception e) {
            dimension = returnDimensionString(player.getWorld().getEnvironment());
        }
        if (returnDimensionString(dimension).equalsIgnoreCase("null")) dimension = returnDimensionString(player.getWorld().getEnvironment());
        if (getCoordValue("X", player, returnDimensionString(dimension), toTitleCase(args[1])) != 0) {
            for (String output : retrieveCoord(player, toTitleCase(args[1]), returnDimensionString(dimension), true))
                player.sendMessage(headers("COORDS") + output);
        } else {
            try {
                player.sendMessage(incorrectArgs("COORDINATE", toTitleCase(args[1]), toTitleCase(args[2])));
            } catch (Exception e) {
                player.sendMessage(incorrectArgs("COORDINATE", toTitleCase(args[1]), returnDimensionTitleString(dimension)));
            }
        }
        return true;
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        // /coords list <name> [Dimension] or /coords list all [Dimension]
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
            types.add("all");
            return types;
        } else if (args.length == 3) {
            List<String> types = new ArrayList<>();
            if (args[1].equalsIgnoreCase("all")) {
                for (String i : dimensionStrings) types.add(toTitleCase(i));
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
