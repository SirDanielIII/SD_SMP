package ca.sirdanieliii.SD_SMP.coords;

import ca.sirdanieliii.SD_SMP.commands.CommandManager;
import ca.sirdanieliii.SD_SMP.configuration.ConfigManager;
import ca.sirdanieliii.SD_SMP.configuration.ConfigYML;
import ca.sirdanieliii.SD_SMP.configuration.configs.ConfigPlayer;
import ca.sirdanieliii.SD_SMP.utilities.ChatPaginator;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

import java.util.*;

import static ca.sirdanieliii.SD_SMP.utilities.Utilities.*;

public class Coord {
    private enum Axis {
        X, Y, Z;
    }

    private final Player player;
    private final UUID worldUiD;
    private final String name;
    private int x;
    private int y;
    private int z;

    public Coord(Player player, UUID worldUiD, String name) {
        this.player = player;
        this.worldUiD = worldUiD;
        this.name = name;
        this.x = player.getLocation().getBlockX();
        this.y = player.getLocation().getBlockY();
        this.z = player.getLocation().getBlockZ();
    }

    public Coord(Player player, UUID worldUiD, String name, int x, int y, int z) {
        this.player = player;
        this.worldUiD = worldUiD;
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Coord(Player player, UUID worldUiD, String name, String x, String y, String z) throws NullPointerException, NumberFormatException {
        this.player = player;
        this.worldUiD = worldUiD;
        this.name = cleanStrForYMLKey(name);
        if (name == null) {
            player.sendMessage(ConfigManager.errorMessage("special_characters"));
            throw new NullPointerException();
        }
        try {
            this.x = parseNumberArg(x, player, Axis.X);
            this.y = parseNumberArg(y, player, Axis.Y);
            this.z = parseNumberArg(z, player, Axis.Z);
        } catch (NumberFormatException exception) {
            player.sendMessage(ConfigManager.errorMessage("coord_str_not_int"));
            throw new NumberFormatException();
        }
    }

    public Coord(Player player, String name, String x, String y, String z, String dimension) throws NullPointerException, NumberFormatException {
        this.player = player;
        this.name = cleanStrForYMLKey(name);
        if (name == null) {
            player.sendMessage(ConfigManager.errorMessage("special_characters"));
            throw new NullPointerException();
        }
        try {
            this.x = parseNumberArg(x, player, Axis.X);
            this.y = parseNumberArg(y, player, Axis.Y);
            this.z = parseNumberArg(z, player, Axis.Z);
        } catch (NumberFormatException exception) {
            player.sendMessage(ConfigManager.errorMessage("coord_str_not_int"));
            throw new NumberFormatException();
        }
        Dimension specifiedDimension = Dimension.getDimensionEnum(dimension);
        if (specifiedDimension == null) {
            player.sendMessage(ConfigManager.errorMessage("invalid_dimension"));
            throw new NullPointerException();
        }

        UUID[] mainWorlds = CoordsUtility.getDefaultWorldIDs();

        switch (specifiedDimension) {
            case OVERWORLD -> {
                worldUiD = mainWorlds[0];
            }
            case NETHER -> {
                worldUiD = mainWorlds[1];
            }
            case THE_END -> {
                worldUiD = mainWorlds[2];
            }
            default -> {
                player.sendMessage(ConfigManager.errorMessage("custom_dimension_not_linked"));
                throw new NullPointerException();
            }
        }
        if (worldUiD == null) {
            player.sendMessage(replaceStr(ConfigManager.errorMessage("missing_dimension_in_server"), "{dimension}", specifiedDimension.formattedStr(false, false, false)));
            throw new NullPointerException();
        } // If it passes this check, then we know the world is valid
    }

    public Coord(Player player, String name, String x, String y, String z, String dimension, String world) throws NullPointerException, NumberFormatException {
        this.player = player;
        this.name = cleanStrForYMLKey(name);
        if (name == null) {
            player.sendMessage(ConfigManager.errorMessage("special_characters"));
            throw new NullPointerException();
        }
        try {
            this.x = parseNumberArg(x, player, Axis.X);
            this.y = parseNumberArg(y, player, Axis.Y);
            this.z = parseNumberArg(z, player, Axis.Z);
        } catch (NumberFormatException exception) {
            player.sendMessage(ConfigManager.errorMessage("coord_str_not_int"));
            throw new NumberFormatException();
        }
        Dimension specifiedDimension = Dimension.getDimensionEnum(dimension);
        if (specifiedDimension == null) {
            player.sendMessage(ConfigManager.errorMessage("invalid_dimension"));
            throw new NullPointerException();
        }
        worldUiD = CoordsUtility.getWorldUid(world);
        if (worldUiD == null) {
            player.sendMessage(replaceStr(ConfigManager.errorMessage("invalid_world_1"), "{world}", world));
            throw new NullPointerException();
        } // If it passes this check, then we know the world is valid
    }

    public boolean save(ConfigYML config, boolean force) {
        if (hasDuplicateCoordName(config) && !force) {
            player.sendMessage(translateMsgClr("------------ | " + CommandManager.cmdClr("coords", true).toUpperCase() + "COORDS &R&F| ------------>"));
            TextComponent errorStr = replaceStr(ConfigManager.errorMessage("coords_duplicate_2"), Map.of("{coord_name}",
                    getCoordComponent(), "{world}", CoordsUtility.getWorldComponent(worldUiD)));
            player.spigot().sendMessage(errorStr);
            TextComponent choice = new TextComponent(">>> ");
            TextComponent confirm = translateMsgClrComponent(String.format("&EClick to confirm overwrite of \"%s\"", name));
            confirm.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(translateMsgClr("&CThis action is not reversible!"))));
            confirm.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                    "/coords set " + name + " " + x + " " + y + " " + z + " " + CoordsUtility.getWorldName(worldUiD) + " --force"));
            choice.addExtra(confirm);
            player.spigot().sendMessage(choice);
            player.sendMessage(ConfigManager.blockFooter);
            return false;
        }
        CoordsUtility.addNewWorldToPlayerConfig(config, worldUiD);
        config.getConfig().set(String.format("coordinates.%s.coords.%s.x", worldUiD, name), x);
        config.getConfig().set(String.format("coordinates.%s.coords.%s.y", worldUiD, name), y);
        config.getConfig().set(String.format("coordinates.%s.coords.%s.z", worldUiD, name), z);
        config.save();
        TextComponent returnMsg = translateMsgClrComponent(String.format("%s&FSaved &B%s ", CommandManager.cmdHeader("coords"), name));
        returnMsg.addExtra(getCoordComponent());
        returnMsg.addExtra(translateMsgClrComponent("&Fin "));
        returnMsg.addExtra(CoordsUtility.getWorldComponent(worldUiD));
        player.spigot().sendMessage(returnMsg);
        return true;
    }

    public TextComponent getCoordComponent() {
        String colour = Dimension.getClr(CoordsUtility.getWorldDimension(worldUiD));
        TextComponent component = translateMsgClrComponent(String.format("%s[&F%d %d %d%s]&R", colour, x, y, z, colour));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(translateMsgClr("Click for teleport command"))));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, translateMsgClr(
                String.format("/tp %s %d %d %d", player.getDisplayName(), x, y, z))));
        return component;
    }

    public static void showAllCoords(ConfigPlayer config, Player p, int page) {
        List<TextComponent> data = new ArrayList<>();
        for (String id : Objects.requireNonNull(config.getConfig().getConfigurationSection("coordinates")).getKeys(false)) {
            String name = config.getConfig().getString(String.format("coordinates.%s.name", id));
            Coord coord = new Coord(p, CoordsUtility.getWorldUid(id), name,
                    config.getConfig().getInt(String.format("coordinates.%s.%s.x", id, name)),
                    config.getConfig().getInt(String.format("coordinates.%s.%s.y", id, name)),
                    config.getConfig().getInt(String.format("coordinates.%s.%s.z", id, name)));
            data.add(coord.getCoordComponent());
        }
        ChatPaginator paginatedData = new ChatPaginator(translateMsgClrComponent(CommandManager.cmdClr("coords", true) + "COORDS LIST"), data, page, ChatColor.WHITE);
        paginatedData.configureFooter("<<<", ">>>", null, "/coords list all " + (page + 1), ChatColor.GOLD, ChatColor.GRAY, ChatColor.WHITE);
        paginatedData.sendPaginatedMessage(p);
    }

    /**
     * Parses an integer out of an argument from a command, with '~' representing the player's current coordinate
     *
     * @param arg    given string argument
     * @param player a Minecraft player
     * @param axis   either 'x', 'y', or 'z'
     * @return an integer
     * @throws NumberFormatException if there are string characters in the argument
     */
    private int parseNumberArg(String arg, Player player, Axis axis) throws NumberFormatException {
        if (arg.charAt(0) == '~') {
            double axisNum = 0;
            switch (axis) {
                case X -> axisNum = player.getLocation().getX();
                case Y -> axisNum = player.getLocation().getY();
                case Z -> axisNum = player.getLocation().getZ();
            }
            if (arg.length() == 1) return (int) axisNum;
            else return (int) (axisNum + Integer.parseInt(arg.substring(1)));
        }
        return Integer.parseInt(arg);
    }

    /**
     * @param config player configuration file
     * @return true if there already exists a coordinate saved with given name, false otherwise
     */
    private boolean hasDuplicateCoordName(ConfigYML config) {
        try {
            return Objects.requireNonNull(config.getConfig().getConfigurationSection(
                    String.format("coordinates.%s.coords", worldUiD.toString()))).getKeys(false).contains(name);
        } catch (NullPointerException e) {
            return false;
        }
    }
}
