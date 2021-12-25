package com.sirdanieliii.SD_SMP.commands.subcommands;

import com.sirdanieliii.SD_SMP.commands.SubCommand;
import com.sirdanieliii.SD_SMP.configuration.ConfigManager;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;

import static com.sirdanieliii.SD_SMP.commands.CommandManager.headers;
import static com.sirdanieliii.SD_SMP.configuration.ReturnCoordsData.returnDimensionString;
import static com.sirdanieliii.SD_SMP.events.ErrorMessages.incorrectArgs;
import static com.sirdanieliii.SD_SMP.events.Utilities.toTitleCase;

public class coordsClear extends SubCommand {
    private static final ConfigManager PLAYER_CONFIG = new ConfigManager();

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "§7Clear saved coordinate(s)";
    }

    @Override
    public String getSyntax() {
        return "§6/coords clear <name> [Dimension]\n§6/coords clear all [Dimension]";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        PLAYER_CONFIG.setup("playerdata", player.getUniqueId().toString());
        PLAYER_CONFIG.reload();
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("all")) { // Add clickable permission here
                player.sendMessage(headers("COORDS") + "§BAll §Fcoordinates have been cleared successfully");
                PLAYER_CONFIG.getConfig().set("coordinates." + "overworld", new Array[]{});
                PLAYER_CONFIG.getConfig().set("coordinates." + "nether", new Array[]{});
                PLAYER_CONFIG.getConfig().set("coordinates." + "the_end", new Array[]{});
                PLAYER_CONFIG.save();
            } else { // /coords clear name
                String[] dimensions = {"overworld", "nether", "the_end"};
                for (String i : dimensions) {
                    if (PLAYER_CONFIG.getConfig().getString("coordinates." + i + "." + toTitleCase(args[1])) != null) {
                        player.sendMessage(headers("COORDS") + "§B" + PLAYER_CONFIG.getConfig().getString("coordinates." + i + "." + toTitleCase(args[1]))
                                + " §Ffrom " + i + " §Fhas been cleared successfully");
                        PLAYER_CONFIG.getConfig().set("coordinates." + i + "." + toTitleCase(args[1]), null);
                    } else player.sendMessage(incorrectArgs("COORDINATE", toTitleCase(args[1]), i));
                }
            }
            PLAYER_CONFIG.save();
            return true;
        } else if (args.length == 3 && !(returnDimensionString(args[2]).equalsIgnoreCase("null"))) { // If dimension is specified
            if (args[1].equalsIgnoreCase("all")) { // Add clickable permission here
                if (!(returnDimensionString(args[2]).equalsIgnoreCase("null"))) { // /coords clear all overworld
                    player.sendMessage(headers("COORDS") + "§B" + returnDimensionString(args[2]) + "§Fcoordinates have been cleared successfully");
                    PLAYER_CONFIG.getConfig().set("coordinates." + returnDimensionString(args[2]), new Array[]{});
                } else {
                    player.sendMessage(incorrectArgs("DIMENSION"));
                }
            } else { // /coords clear name overworld
                if (PLAYER_CONFIG.getConfig().getString("coordinates." + returnDimensionString(args[2]) + "." + toTitleCase(args[1])) != null) {
                    player.sendMessage(headers("COORDS") + "§B" + PLAYER_CONFIG.getConfig().getString(returnDimensionString(args[2]) + "."
                            + toTitleCase(args[1])) + " §Ffrom " + returnDimensionString(args[2]) + " §Fhas been cleared successfully");
                    PLAYER_CONFIG.getConfig().set(returnDimensionString(args[2]) + "." + toTitleCase(args[1]), null);
                } else player.sendMessage(incorrectArgs("COORDINATE", toTitleCase(args[1]), returnDimensionString(args[2])));
            }
        } else {
            player.sendMessage(incorrectArgs("CLEAR"));
        }
        PLAYER_CONFIG.save();
        return true;
    }
}
// Redo formatting later