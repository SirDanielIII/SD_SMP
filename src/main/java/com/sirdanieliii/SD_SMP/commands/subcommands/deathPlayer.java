package com.sirdanieliii.SD_SMP.commands.subcommands;

import com.sirdanieliii.SD_SMP.commands.SubCommand;
import org.bukkit.entity.Player;

import java.util.List;

import static com.sirdanieliii.SD_SMP.configuration.ReturnDeathData.pvpDeaths;
import static com.sirdanieliii.SD_SMP.events.ErrorMessages.errorMessage;

public class deathPlayer extends SubCommand {
    @Override
    public String getName() {
        return "player";
    }

    @Override
    public String getDescription() {
        return "§7Returns death count (Only PVP)";
    }

    @Override
    public String getSyntax() {
        return "§4/death player";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (!(player.hasPermission("sd_smp.death.player"))) {
            player.sendMessage(errorMessage("PERMISSION"));
            return true;
        }
        pvpDeaths(player);
        return true;
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        return null;
    }
}
