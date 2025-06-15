package ca.sirdanieliii.SD_SMP.commands;

import ca.sirdanieliii.SD_SMP.commands.subcommands.coordsCmds.coordsClear;
import ca.sirdanieliii.SD_SMP.commands.subcommands.coordsCmds.coordsList;
import ca.sirdanieliii.SD_SMP.commands.subcommands.coordsCmds.coordsSend;
import ca.sirdanieliii.SD_SMP.commands.subcommands.coordsCmds.coordsSet;
import ca.sirdanieliii.SD_SMP.commands.subcommands.death.*;
import ca.sirdanieliii.SD_SMP.commands.subcommands.ivan.ivanDog;
import ca.sirdanieliii.SD_SMP.commands.subcommands.ivan.ivanDonkey;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static ca.sirdanieliii.SD_SMP.configuration.ConfigManager.BLOCK_FOOTER;
import static ca.sirdanieliii.SD_SMP.configuration.ConfigManager.errorMessage;
import static ca.sirdanieliii.SD_SMP.utilities.Utilities.translateMsgClr;
import static ca.sirdanieliii.SD_SMP.utilities.Utilities.translateMsgClrComponent;

public class CommandManager implements TabExecutor {

    public static Map<String, List<SubCommand>> CMD_CATEGORIES;

    public CommandManager() {
        List<SubCommand> ivan = Arrays.asList(new ivanDog(), new ivanDonkey());
        List<SubCommand> coords = Arrays.asList(new coordsClear(), new coordsList(), new coordsSend(), new coordsSet());
        List<SubCommand> death = Arrays.asList(new deathKDR(), new deathKills(), new deathNonPlayer(), new deathPlayer(), new deathTotal());

        CMD_CATEGORIES = Map.of(
                "ivan", ivan,
                "coords", coords,
                "death", death
        );
    }

    public static List<SubCommand> getSubcommands(String key) {
        return CMD_CATEGORIES.getOrDefault(key, Collections.emptyList());
    }

    public static String cmdHeader(String type) {
        return switch (type.toLowerCase()) {
            case "coords" -> "&6[&FCoords&6] ";
            case "death" -> "&4[&FDeath&4] ";
            case "ivan" -> "&#0085F5[&FIvan&#0085F5] ";
            default -> "";
        };
    }

    public static String cmdClr(String type, boolean bold) {
        String colorCode = switch (type.toLowerCase()) {
            case "coords" -> "&6";
            case "death" -> "&4";
            case "ivan" -> "&B";
            case "smp" -> "&#f50057";
            case "wand" -> "&D";
            default -> "&F";
        };
        return bold ? colorCode + "&L" : colorCode;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(errorMessage("player_only"));
            return false;
        }

        if (args.length > 0) {
            for (SubCommand subcommand : getSubcommands(cmd.getName())) {
                if (args[0].equalsIgnoreCase(subcommand.getName())) {
                    subcommand.perform(player, args);
                    return true;
                }
            }
        }
        displaySubCommands(player, cmd.getName());
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            List<String> subcommandArgs = new ArrayList<>();
            getSubcommands(cmd.getName()).forEach(subcommand -> subcommandArgs.add(subcommand.getName()));
            return subcommandArgs;
        } else if (args.length >= 2) {
            for (SubCommand subcommand : getSubcommands(cmd.getName())) {
                if (args[0].equalsIgnoreCase(subcommand.getName())) {
                    return subcommand.getSubcommandArgs((Player) sender, args);
                }
            }
        }
        return null;
    }

    private void displaySubCommands(Player player, String cmd) {
        String header = cmdClr(cmd, true) + cmd.toUpperCase();
        player.sendMessage(translateMsgClr("------------ | " + header + " &R&F| ------------>"));
        for (SubCommand subcommand : getSubcommands(cmd)) {
            TextComponent commandComponent = translateMsgClrComponent("→ " + cmdClr(subcommand.getCmdGroup(), false) + subcommand.getSyntax());
            commandComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, subcommand.getSyntax()));
            commandComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(translateMsgClr("&7" + subcommand.getDescription()))));
            player.spigot().sendMessage(commandComponent);
        }
        player.sendMessage(BLOCK_FOOTER);
    }
}
