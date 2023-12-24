package ca.sirdanieliii.SD_SMP.commands.subcommands.ivan;

import ca.sirdanieliii.SD_SMP.commands.CommandManager;
import ca.sirdanieliii.SD_SMP.commands.SubCommand;
import ca.sirdanieliii.SD_SMP.configuration.ConfigManager;
import ca.sirdanieliii.SD_SMP.utilities.Utilities;
import org.bukkit.DyeColor;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.util.Vector;

import java.util.List;

public class ivanDog extends SubCommand {
    @Override
    public String getName() {
        return "dog";
    }

    @Override
    public String getCmdGroup() {
        return "ivan";
    }

    @Override
    public String getDescription() {
        return "Summons a dog named \"Ivan\"";
    }

    @Override
    public String getSyntax() {
        return ConfigManager.generalMsgs.get("ivan_dog");
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (!(player.hasPermission("sd_smp.ivan.dog"))) {
            player.sendMessage(ConfigManager.errorMessage("permission"));
            return false;
        }
        Vector offset = Utilities.offsetFromDirection(player, 2.0D); // Calculates 2 block forward offset
        Wolf wolf = (Wolf) player.getWorld().spawnEntity(player.getLocation().add(offset), EntityType.WOLF);
        wolf.setTamed(true);
        wolf.setOwner(player);
        wolf.setCustomName("Ivan");
        wolf.setCollarColor(DyeColor.LIGHT_BLUE);
        wolf.setSitting(true);
        player.playSound(player.getLocation(), Sound.ENTITY_WOLF_PANT, 1, 1);
        player.sendMessage(Utilities.translateMsgClr(CommandManager.cmdHeader("ivan") + "&FYou have spawned a little Ivan!"));
        return true;
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        return null;
    }
}
