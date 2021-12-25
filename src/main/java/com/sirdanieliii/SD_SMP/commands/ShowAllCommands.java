package com.sirdanieliii.SD_SMP.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.sirdanieliii.SD_SMP.commands.CommandManager.displayAllCommands;

public class ShowAllCommands implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§C[!] Sorry, but players can use this command");
            return true;
        }
        displayAllCommands(player);
        return true;
    }

    public static String getDescription() {
        return "[OP] Spawns powerful a wand of the gods";
    }

    public static String getSyntax() {
        return "/wand";
    }
}
