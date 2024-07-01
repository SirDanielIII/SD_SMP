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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

import static ca.sirdanieliii.SD_SMP.coords.CoordsUtility.getWorldDimension;
import static ca.sirdanieliii.SD_SMP.utilities.Utilities.*;

public class Coord {
    private final Player player;
    private final UUID worldUid;
    private final String name;
    private final int x;
    private final int y;
    private final int z;
    public Coord(Player player, UUID worldUid, String name) {
        this(player, worldUid, name, player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
    }

    public Coord(Player player, UUID worldUid, String name, int x, int y, int z) {
        this.player = player;
        this.worldUid = worldUid;
        this.name = cleanStrForYMLKey(name);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Coord(Player player, UUID worldUid, String name, String x, String y, String z) {
        this.player = player;
        this.worldUid = worldUid;
        this.name = cleanStrForYMLKey(name);
        this.x = parseNumberArg(x, player, Axis.X);
        this.y = parseNumberArg(y, player, Axis.Y);
        this.z = parseNumberArg(z, player, Axis.Z);
    }

    public Coord(Player player, String name, String x, String y, String z, String dimension) {
        this(player, name, x, y, z, dimension, getDimensionWorldUid(player, dimension));
    }

    public Coord(Player player, String name, String x, String y, String z, String dimension, String world) {
        this(player, name, x, y, z, dimension, CoordsUtility.getWorldUid(world));
    }

    private Coord(Player player, String name, String x, String y, String z, String dimension, UUID worldUid) {
        this.player = player;
        this.name = cleanStrForYMLKey(name);
        this.x = parseNumberArg(x, player, Axis.X);
        this.y = parseNumberArg(y, player, Axis.Y);
        this.z = parseNumberArg(z, player, Axis.Z);
        this.worldUid = worldUid;

        if (this.name == null) {
            player.sendMessage(ConfigManager.errorMessage("special_characters"));
            return;
        }

        if (this.worldUid == null) {
            player.sendMessage(ConfigManager.errorMessage("invalid_world"));
            return;
        }

        Dimension specifiedDimension = Dimension.getDimensionEnum(dimension);
        if (specifiedDimension == null) {
            player.sendMessage(ConfigManager.errorMessage("invalid_dimension"));
        }
    }

    private static UUID getDimensionWorldUid(Player player, String dimension) {
        Dimension specifiedDimension = Dimension.getDimensionEnum(dimension);
        if (specifiedDimension == null) {
            player.sendMessage(ConfigManager.errorMessage("invalid_dimension"));
            throw new NullPointerException();
        }

        UUID[] mainWorlds = CoordsUtility.getDefaultWorldIDs();
        return switch (specifiedDimension) {
            case OVERWORLD -> mainWorlds[0];
            case NETHER -> mainWorlds[1];
            case THE_END -> mainWorlds[2];
            default -> {
                player.sendMessage(ConfigManager.errorMessage("custom_dimension_not_linked"));
                throw new NullPointerException();
            }
        };
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

    public boolean save(ConfigYML config, boolean force) {
        if (hasDuplicateCoordName(config) && !force) {
            displayDuplicateCoordMessage();
            return false;
        }

        CoordsUtility.addNewWorldToPlayerConfig(config, worldUid);
        String path = String.format("coordinates.%s.coords.%s", worldUid, name);
        config.getConfig().set(path + ".x", x);
        config.getConfig().set(path + ".y", y);
        config.getConfig().set(path + ".z", z);
        config.save();

        TextComponent returnMsg = translateMsgClrComponent(String.format("%s&FSaved &B%s ", CommandManager.cmdHeader("coords"), name));
        returnMsg.addExtra(getCoordComponent());
        returnMsg.addExtra(" ");
        returnMsg.addExtra(translateMsgClrComponent("&Fin "));
        returnMsg.addExtra(CoordsUtility.getWorldComponent(worldUid));
        player.spigot().sendMessage(returnMsg);

        return true;
    }

    private void displayDuplicateCoordMessage() {
        player.sendMessage(translateMsgClr("------------ | " + CommandManager.cmdClr("coords", true).toUpperCase() + "COORDS &R&F| ------------>"));
        TextComponent errorStr = replaceStr(ConfigManager.errorMessage("coords_duplicate_2"), Map.of(
                "{coord_name}", getCoordComponent(), "{world}", CoordsUtility.getWorldComponent(worldUid)));
        player.spigot().sendMessage(errorStr);

        TextComponent choice = new TextComponent(">>> ");
        TextComponent confirm = translateMsgClrComponent(String.format("&EClick to confirm overwrite of \"%s\"", name));
        confirm.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(translateMsgClr("&CThis action is not reversible!"))));
        confirm.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                String.format("/coords set %s %d %d %d %s %s --force", name, x, y, z, Dimension.getDimensionEnum(getWorldDimension(worldUid)), CoordsUtility.getWorldName(worldUid))));
        choice.addExtra(confirm);
        player.spigot().sendMessage(choice);
        player.sendMessage(ConfigManager.BLOCK_FOOTER);
    }

    public TextComponent getCoordComponent() {
        String colour = Dimension.getClr(getWorldDimension(worldUid));
        TextComponent component = translateMsgClrComponent(String.format("%s[&F%d %d %d%s]", colour, x, y, z, colour));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(translateMsgClr("Click for teleport command"))));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/tp %s %d %d %d", player.getDisplayName(), x, y, z)));
        return component;
    }

    private int parseNumberArg(String arg, Player player, Axis axis) {
        double axisNum = switch (axis) {
            case X -> player.getLocation().getX();
            case Y -> player.getLocation().getY();
            case Z -> player.getLocation().getZ();
        };
        return arg.charAt(0) == '~' ? (arg.length() == 1 ? (int) axisNum : (int) (axisNum + Integer.parseInt(arg.substring(1)))) : Integer.parseInt(arg);
    }

    private boolean hasDuplicateCoordName(ConfigYML config) {
        Bukkit.broadcastMessage("Duplicate: " + Optional.ofNullable(config.getConfig().getConfigurationSection(String.format("coordinates.%s.coords", worldUid)))
                .map(section -> section.getKeys(false).contains(name))
                .orElse(false));
        return Optional.ofNullable(config.getConfig().getConfigurationSection(String.format("coordinates.%s.coords", worldUid)))
                .map(section -> section.getKeys(false).contains(name))
                .orElse(false);
    }

    private enum Axis {
        X, Y, Z
    }
}
