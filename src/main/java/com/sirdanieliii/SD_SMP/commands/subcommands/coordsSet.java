package com.sirdanieliii.SD_SMP.commands.subcommands;

import com.sirdanieliii.SD_SMP.commands.SubCommand;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.sirdanieliii.SD_SMP.SD_SMP.PLAYER_CONFIG;
import static com.sirdanieliii.SD_SMP.commands.CommandManager.headers;
import static com.sirdanieliii.SD_SMP.configuration.ReturnCoordsData.returnDimension;
import static com.sirdanieliii.SD_SMP.configuration.ReturnCoordsData.returnDimensionString;
import static com.sirdanieliii.SD_SMP.events.ErrorMessages.*;
import static com.sirdanieliii.SD_SMP.events.Utilities.*;

public class coordsSet extends SubCommand {
    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getDescription() {
        return "§7Saves coordinate under given name";
    }

    @Override
    public String getSyntax() {
        return "§6/coords set <name> here §7or\n§6/coords set <name> <X Y Z> <dimension>";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (args.length == 1) player.sendMessage(incorrectArgs("SET_ARG0")); // args[0]
        else if (args.length == 2) player.sendMessage(incorrectArgs("SET_ARG1"));
        else if (args.length == 3) {
            if (args[2].equalsIgnoreCase("here")) {
                savePlayerCoords(player,
                        (int) player.getLocation().getX(),
                        (int) player.getLocation().getY(),
                        (int) player.getLocation().getZ(),
                        toTitleCase(args[1]), player.getWorld().getEnvironment());
                return true;
            } else player.sendMessage(incorrectArgs("SET_COORDS_X"));
        } else if (args.length == 4) player.sendMessage(incorrectArgs("SET_COORDS_Y"));
        else if (args.length == 5) player.sendMessage(incorrectArgs("SET_ARG4"));
        else {
            try {
                int x, y, z;
                if (args[2].equalsIgnoreCase("~")) x = (int) player.getLocation().getX();
                else
                    try {
                        x = Integer.parseInt(args[2]);
                    } catch (NumberFormatException n) {
                        player.sendMessage(errorMessage("INTEGER"));
                        return false;
                    }
                if (args[3].equalsIgnoreCase("~")) y = (int) player.getLocation().getY();
                else
                    try {
                        y = Integer.parseInt(args[3]);
                    } catch (NumberFormatException n) {
                        player.sendMessage(errorMessage("INTEGER"));
                        return false;
                    }
                if (args[4].equalsIgnoreCase("~")) z = (int) player.getLocation().getZ();
                else
                    try {
                        z = Integer.parseInt(args[4]);
                    } catch (NumberFormatException n) {
                        player.sendMessage(errorMessage("INTEGER"));
                        return false;
                    }
                World.Environment dimension;
                if (returnDimension(args[5]) != null) {
                    dimension = returnDimension(args[5]);
                    savePlayerCoords(player, x, y, z, toTitleCase(args[1]), dimension);
                } else {
                    player.sendMessage(errorMessage("DIMENSION", toTitleCase(args[5])));
                    return false;
                }
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

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        //  §6/coords set <name> here §7or\n§6/coords set <name> <X Y Z> [Dimension]"
        if (args.length == 2) {
            List<String> types = new ArrayList<>();
            types.add("name");
            return types;
        } else if (args.length == 3) {
            List<String> types = new ArrayList<>();
            types.add("~");
            types.add("here");
            Collections.sort(types);
            return types;
        }
        if (!args[2].equalsIgnoreCase("here")) {
            if (args.length == 4) {
                List<String> types = new ArrayList<>();
                types.add("~");
                return types;
            } else if (args.length == 5) {
                List<String> types = new ArrayList<>();
                types.add("~");
                return types;
            } else if (args.length == 6) {
                List<String> types = new ArrayList<>();
                types.add("Overworld");
                types.add("Nether");
                types.add("The_End");
                return types;
            }
        }
        return null;
    }
}