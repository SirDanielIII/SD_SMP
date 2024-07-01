package ca.sirdanieliii.SD_SMP.commands.subcommands.ivan;

import ca.sirdanieliii.SD_SMP.commands.CommandManager;
import ca.sirdanieliii.SD_SMP.commands.SubCommand;
import ca.sirdanieliii.SD_SMP.configuration.ConfigManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static ca.sirdanieliii.SD_SMP.utilities.Utilities.getSafeLocationInFront;
import static ca.sirdanieliii.SD_SMP.utilities.Utilities.translateMsgClr;

public class ivanDonkey extends SubCommand {
    @Override
    public String getName() {
        return "donkey";
    }

    @Override
    public String getCmdGroup() {
        return "ivan";
    }

    @Override
    public String getDescription() {
        return "Summons a donkey named \"Ivan\"";
    }

    @Override
    public String getSyntax() {
        return ConfigManager.generalMsgs.get("ivan_donkey");
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (!(player.hasPermission("sd_smp.ivan.donkey"))) {
            player.sendMessage(ConfigManager.errorMessage("permission"));
            return false;
        }
        Donkey donkey = (Donkey) player.getWorld().spawnEntity(getSafeLocationInFront(player), EntityType.DONKEY);
        donkey.setTamed(true);
        donkey.setOwner(player);
        donkey.setCustomName(translateMsgClr("&#0085F5Ivan"));
        donkey.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        donkey.setCarryingChest(true);
        donkey.setJumpStrength(0.75F);
        player.playSound(player.getLocation(), Sound.ENTITY_DONKEY_AMBIENT, 1, 1);
        player.sendMessage(translateMsgClr(CommandManager.cmdHeader("ivan") + "&FYou have spawned a &Cstoopid&F Ivan!"));
        return true;
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        return null;
    }
}
