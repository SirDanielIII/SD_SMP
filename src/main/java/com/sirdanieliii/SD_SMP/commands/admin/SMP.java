package com.sirdanieliii.SD_SMP.commands.admin;

import com.sirdanieliii.SD_SMP.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.sirdanieliii.SD_SMP.commands.CommandManager.cmdCategories;
import static com.sirdanieliii.SD_SMP.configuration.ConfigManager.*;

public class SMP implements TabExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender instanceof Player player) {
                    if (!(player.hasPermission("sd_smp.smp.reload"))) {
                        player.sendMessage(errorMessage("permission"));
                        return false;
                    }
                }
                reload();
                sender.sendMessage(cmdHeader + " §FConfiguration files & settings reloaded");
                return true;
            } else {
                sender.sendMessage(errorMessage("smp"));
                return false;
            }
        }
        sender.sendMessage("------------ | " + smpName + " Commands" + "§R§F | ------------>");
        // From subcommands list
        for (List<SubCommand> i : cmdCategories.values()) for (SubCommand subCommand : i) sender.sendMessage(subCommand.getSyntax() + " §7→ " + subCommand.getDescription());
        // Single Commands
        sender.sendMessage(Wand.getSyntax() + " §7→ " + Wand.getDescription());
        sender.sendMessage("<-------------------------------------------------->");
        return true;
    }

    public static String getDescription() {
        return "§7SMP tools & commands list";
    }

    public static String getSyntax() {
        return "§5" + errorMessages.get("wand");
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) list.add("reload");
        return list;
    }
}
