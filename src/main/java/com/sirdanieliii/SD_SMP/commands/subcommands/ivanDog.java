package com.sirdanieliii.SD_SMP.commands.subcommands;

import com.sirdanieliii.SD_SMP.commands.SubCommand;
import org.bukkit.DyeColor;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.util.Vector;

import java.util.List;

import static com.sirdanieliii.SD_SMP.commands.CommandManager.cmdHeader;
import static com.sirdanieliii.SD_SMP.configuration.ConfigManager.errorMessage;
import static com.sirdanieliii.SD_SMP.configuration.ConfigManager.errorMessages;
import static com.sirdanieliii.SD_SMP.configuration.Utilities.offsetFromDirection;
import static com.sirdanieliii.SD_SMP.configuration.Utilities.translateColourCodes;

public class ivanDog extends SubCommand {
    @Override
    public String getName() {
        return "dog";
    }

    @Override
    public String getDescription() {
        return "&7Summons a dog named \"Ivan\"";
    }

    @Override
    public String getSyntax() {
        return "&B" + errorMessages.get("ivan_dog");
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (!(player.hasPermission("sd_smp.ivan.dog"))) {
            player.sendMessage(errorMessage("permission"));
            return false;
        }
        Vector offset = offsetFromDirection(player, 2.0D); // Calculates 2 block forward offset
        Wolf wolf = (Wolf) player.getWorld().spawnEntity(player.getLocation().add(offset), EntityType.WOLF);
        wolf.setTamed(true);
        wolf.setOwner(player);
        wolf.setCustomName("Ivan");
        wolf.setCollarColor(DyeColor.LIGHT_BLUE);
        wolf.setSitting(true);
        player.playSound(player.getLocation(), Sound.ENTITY_WOLF_PANT, 1, 1);
        player.sendMessage(translateColourCodes(cmdHeader("ivan") + "&FYou have spawned a little Ivan!"));
        return true;
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        return null;
    }
}
