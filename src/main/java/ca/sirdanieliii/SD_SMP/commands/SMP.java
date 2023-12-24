package ca.sirdanieliii.SD_SMP.commands;

import ca.sirdanieliii.SD_SMP.SD_SMP;
import ca.sirdanieliii.SD_SMP.configuration.ConfigManager;
import ca.sirdanieliii.SD_SMP.events.Scoreboards;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static ca.sirdanieliii.SD_SMP.utilities.Utilities.translateMsgClr;
import static ca.sirdanieliii.SD_SMP.utilities.Utilities.translateMsgClrComponent;

public class SMP implements TabExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender instanceof Player player) {
                    if (!(player.hasPermission("sd_smp.smp.reload"))) {
                        player.sendMessage(ConfigManager.errorMessage("permission"));
                        return false;
                    }
                }
                ConfigManager.reloadConfigs(); // Reload Configs
                // Reload scoreboard if enabled, disable it otherwise
                if (ConfigManager.healthUnderName) Scoreboards.reloadHealthScoreboard();
                else Scoreboards.disableHealthScoreboard();
                // Return Messages
                sender.sendMessage(translateMsgClr(ConfigManager.cmdHeader + " &FAll configs, settings and tasks have been reloaded!"));
                if (sender instanceof Player) SD_SMP.getThisPlugin().getLogger().info(sender.getName() + "has reloaded the " + ConfigManager.pluginName + " plugin!");
                return true;
            }
            sender.sendMessage(ConfigManager.errorMessage("smp"));
            return false;
        }
        // Display all plugin commands
        sender.sendMessage(translateMsgClr("━━━━━━━━━━━━ ║ " + ConfigManager.pluginName + " Commands &R&F║ ━━━━━━━━━━━━"));
        TextComponent cmdReload = translateMsgClrComponent("→ " + CommandManager.cmdClr(cmd.getName(), false) + ConfigManager.generalMsgs.get("smp"));
        cmdReload.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, ConfigManager.generalMsgs.get("smp")));
        cmdReload.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(translateMsgClr("&7Reloads all configs, settings and tasks for this plugin"))));
        sender.spigot().sendMessage(cmdReload);
        for (List<SubCommand> subcommands : CommandManager.cmdCategories.values()) {
            for (SubCommand subcommand : subcommands) {
                TextComponent command = translateMsgClrComponent("→ " + CommandManager.cmdClr(subcommand.getCmdGroup(), false) + subcommand.getSyntax());
                command.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, subcommand.getSyntax()));
                command.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(translateMsgClr("&7" + subcommand.getDescription()))));
                sender.spigot().sendMessage(command);
            }
        }
        TextComponent cmdWand = translateMsgClrComponent("→ " + CommandManager.cmdClr("wand", false) + Wand.getSyntax());
        cmdWand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, Wand.getSyntax()));
        cmdWand.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(translateMsgClr("&7" + Wand.getDescription()))));
        sender.spigot().sendMessage(cmdWand);
        sender.sendMessage(ConfigManager.blockFooter);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) list.add("reload");
        return list;
    }
}
