package ca.sirdanieliii.SD_SMP.commands.subcommands.coordsCmds;

import ca.sirdanieliii.SD_SMP.commands.CommandManager;
import ca.sirdanieliii.SD_SMP.commands.SubCommand;
import ca.sirdanieliii.SD_SMP.configuration.ConfigManager;
import ca.sirdanieliii.SD_SMP.configuration.configs.ConfigPlayer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static ca.sirdanieliii.SD_SMP.utilities.Utilities.translateMsgClr;
import static ca.sirdanieliii.SD_SMP.utilities.Utilities.translateMsgClrComponent;

public class coordsClear extends SubCommand {
    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getCmdGroup() {
        return "coords";
    }

    @Override
    public String getDescription() {
        return "Clears saved coordinate(s)";
    }

    @Override
    public String getSyntax() {
        return ConfigManager.generalMsgs.get("coords_clear");
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (!(player.hasPermission("sd_smp.coords.clear"))) {
            player.sendMessage(ConfigManager.errorMessage("permission"));
            return false;
        }
        ConfigPlayer config = new ConfigPlayer(player);
        // No Dimension Given
        if (args.length == 1) {
            player.sendMessage(ConfigManager.errorMessage("coords_clear"));
            return false;
        } else if (args.length == 2) {
            if (args[1].equalsIgnoreCase("all")) { // /coords clear all
                return clearAllCoords(config, player, false);
            } else { // /coords clear name
            }
        } else if (args.length >= 3) { // If dimension is specified
        }
        return true;
    }

    /**
     * @param player Minecraft player
     * @param force  Boolean determining whether all the coords gets deleted or not
     */
    public static boolean clearAllCoords(ConfigPlayer conf, Player player, boolean force) {
        if (!force) {
            TextComponent confirmMsg = translateMsgClrComponent("&E>>> Click to &Cclear all &E your coords!");
            confirmMsg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(translateMsgClr("&CThis action is irreversible!"))));
            confirmMsg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/coords clear all --force"));
            player.sendMessage(translateMsgClr(CommandManager.cmdHeader("coords") + "&FAre you sure you want clear ALL your coords?"));
            player.spigot().sendMessage(confirmMsg);
        } else {
            conf.getConfig().set("coordinates", new Array[]{});
            player.sendMessage(translateMsgClr(CommandManager.cmdHeader("coords") + "&FAll coords &Cremoved &Asuccessfully!"));
            return true;
        }
        return false;
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        // /coords clear <name | all> [dimension(s)]
        ConfigPlayer config = new ConfigPlayer(player);
        List<String> list = new ArrayList<>();
        return list;
    }
}