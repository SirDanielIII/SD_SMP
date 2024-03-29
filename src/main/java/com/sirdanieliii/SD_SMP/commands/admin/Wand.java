package com.sirdanieliii.SD_SMP.commands.admin;

import com.sirdanieliii.SD_SMP.items.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.sirdanieliii.SD_SMP.configuration.ConfigManager.errorMessage;
import static com.sirdanieliii.SD_SMP.configuration.ConfigManager.errorMessages;
import static com.sirdanieliii.SD_SMP.configuration.Utilities.translateColourCodes;

public class Wand implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (!(player.hasPermission("sd_smp.god.wand"))) {
            player.sendMessage(errorMessage("permission"));
            return false;
        }
        if (cmd.getName().equalsIgnoreCase("wand")) {
            Bukkit.broadcastMessage(translateColourCodes("&4&OA Wand of the Gods has been summoned..."));
            for (Player p : Bukkit.getOnlinePlayers()) p.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 15, 25);
            player.getInventory().addItem(ItemManager.wand);
        }
        return true;
    }

    public static String getDescription() {
        return translateColourCodes("&7Summons a powerful wand of the Gods");
    }

    public static String getSyntax() {
        return translateColourCodes("&5" + errorMessages.get("wand"));
    }
}
