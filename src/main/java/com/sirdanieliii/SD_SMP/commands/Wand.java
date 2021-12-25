package com.sirdanieliii.SD_SMP.commands;

import com.sirdanieliii.SD_SMP.items.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.sirdanieliii.SD_SMP.events.ErrorMessages.errorMessage;

public class Wand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(errorMessage("PLAYER_ONLY"));
            return true;
        }
        Player player = (Player) sender;
        if (!player.isOp()) {
            sender.sendMessage(errorMessage("OP_ONLY"));
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("wand")) {
            Bukkit.broadcastMessage("§6§OA wand of the gods has been summoned...");
            player.getInventory().addItem(ItemManager.wand);
        }
        return true;
    }

    public static String getDescription() {
        return "§C[§FOP§C] §7Spawns powerful a wand of the gods";
    }

    public static String getSyntax() {
        return "§D/wand";
    }
}
