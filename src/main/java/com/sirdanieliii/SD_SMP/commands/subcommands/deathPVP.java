package com.sirdanieliii.SD_SMP.commands.subcommands;

import com.sirdanieliii.SD_SMP.commands.SubCommand;
import org.bukkit.entity.Player;

import static com.sirdanieliii.SD_SMP.configuration.ReturnDeathData.pvpDeaths;

public class deathPVP extends SubCommand {
    @Override
    public String getName() {
        return "player";
    }

    @Override
    public String getDescription() {
        return "§7Shows death count (Only PVP)";
    }

    @Override
    public String getSyntax() {
        return "§4/death player";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        pvpDeaths(player);
        return true;
    }
}
