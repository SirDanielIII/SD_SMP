package com.sirdanieliii.SD_SMP.commands.subcommands;

import com.sirdanieliii.SD_SMP.commands.SubCommand;
import com.sirdanieliii.SD_SMP.configuration.ConfigManager;
import org.bukkit.World;
import org.bukkit.entity.Player;

import static com.sirdanieliii.SD_SMP.commands.CommandManager.headers;
import static com.sirdanieliii.SD_SMP.configuration.ReturnCoordsData.returnDimension;
import static com.sirdanieliii.SD_SMP.configuration.ReturnCoordsData.returnDimensionString;
import static com.sirdanieliii.SD_SMP.events.ErrorMessages.*;
import static com.sirdanieliii.SD_SMP.events.Utilities.*;

public class coordsSet extends SubCommand {
    private static final ConfigManager PLAYER_CONFIG = new ConfigManager();

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getDescription() {
        return "§7Saves a coordinate under a specified name";
    }

    @Override
    public String getSyntax() {
        return "§6/coords set here <name> §7or \n§6/coords set <X Y Z> <name> [dimension]";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (args.length == 1) { // args[0]
            player.sendMessage(incorrectArgs("SET_ARG0"));
        } else if (args.length == 2) { // args[1]
            if ("here".equalsIgnoreCase(args[1])) {
                player.sendMessage(incorrectArgs("SET_ARG1-NAME-1"));
            } else player.sendMessage(incorrectArgs("SET_COORDS"));
            return false;
        } else if (args.length == 3) { // args[2]
            if ("here".equalsIgnoreCase(args[1])) {
                savePlayerCoords(player,
                        (int) player.getLocation().getX(),
                        (int) player.getLocation().getY(),
                        (int) player.getLocation().getZ(),
                        toTitleCase(args[2]), player.getWorld().getEnvironment());
                return true;
            } else player.sendMessage(incorrectArgs("SET_COORDS"));
        } else if (args.length == 4) player.sendMessage(incorrectArgs("SET_ARG1-NAME-2")); // args[3]
        else { // args[4]
            try {
                int x = Integer.parseInt(args[1]);
                int y = Integer.parseInt(args[2]);
                int z = Integer.parseInt(args[3]);
                World.Environment dimension;
                try {
                    dimension = returnDimension(args[5]);
                } catch (Exception e) {
                    dimension = player.getWorld().getEnvironment();
                }
                savePlayerCoords(player, x, y, z, toTitleCase(args[4]), dimension);
            } catch (NumberFormatException n) {
                player.sendMessage(errorMessage("INTEGER"));
                return false;
            }
        }
        return true;
    }

    private void savePlayerCoords(Player player, int x, int y, int z, String name, World.Environment dimension) {
        PLAYER_CONFIG.setup("playerdata", player.getUniqueId().toString());
        PLAYER_CONFIG.reload();
        if (dimension == null) dimension = player.getWorld().getEnvironment();
        if (dimension == World.Environment.NORMAL) {
            player.sendMessage(headers("COORDS") + "§FSaved §B" + name + " §6[§F" + x + " " + y + " " + z + "§6] §Fto §AOverworld");
        } else if (dimension == World.Environment.NETHER) {
            player.sendMessage(headers("COORDS") + "§FSaved §B" + name + " §6[§F" + x + " " + y + " " + z + "§6] §Fto §CNether");
        } else if (dimension == World.Environment.THE_END) {
            player.sendMessage(headers("COORDS") + "§FSaved §B" + name + " §6[§F" + x + " " + y + " " + z + "§6] §Fto §DThe End");
        }
        PLAYER_CONFIG.getConfig().set("coordinates." + returnDimensionString(String.valueOf(dimension)) + "." + name + ".X", x);
        PLAYER_CONFIG.getConfig().set("coordinates." + returnDimensionString(String.valueOf(dimension)) + "." + name + ".Y", y);
        PLAYER_CONFIG.getConfig().set("coordinates." + returnDimensionString(String.valueOf(dimension)) + "." + name + ".Z", z);
        PLAYER_CONFIG.save();
    }
}
// Add description feature in the future?