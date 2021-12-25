package com.sirdanieliii.SD_SMP.commands.subcommands;

import com.sirdanieliii.SD_SMP.commands.SubCommand;
import com.sirdanieliii.SD_SMP.configuration.ConfigManager;
import org.bukkit.entity.Player;

import static com.sirdanieliii.SD_SMP.commands.CommandManager.headers;
import static com.sirdanieliii.SD_SMP.configuration.ReturnCoordsData.*;
import static com.sirdanieliii.SD_SMP.events.ErrorMessages.errorMessage;
import static com.sirdanieliii.SD_SMP.events.Utilities.*;

public class coordsList extends SubCommand {
    private static final ConfigManager PLAYER_CONFIG = new ConfigManager();

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
        return "§6/coords list <name> [Dimension]\n§6/coords list all [Dimension]";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (args[1].equalsIgnoreCase("all")) {
            if (args.length == 2) { // coords list all
                for (String output : retrieveAllCoords(player))
                    player.sendMessage(headers("COORDS") + output);
                return true;
            } else {  // coords list all [dimension]
                if (!returnDimensionString(args[2]).equals("null")) {
                    for (String output : retrieveAllCoordsDimension(player, returnDimensionString(args[2])))
                        player.sendMessage(headers("COORDS") + output);
                } else player.sendMessage(headers(errorMessage("DIMENSION")));
                return true;
            }
        }
        // /coords list name [dimension]
        String dimension;
        try {
            dimension = args[2];
        } catch (Exception e) {
            dimension = returnDimensionString(player.getWorld().getEnvironment().toString());
        }
        for (String output : retrieveCoord(player, toTitleCase(args[1]), dimension))
            player.sendMessage(headers("COORDS") + output);
        return true;
    }
}
// everything but /coords list name overworld bugs out
// Add check and message when array is blank