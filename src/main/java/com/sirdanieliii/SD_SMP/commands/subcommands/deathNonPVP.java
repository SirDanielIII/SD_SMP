package com.sirdanieliii.SD_SMP.commands.subcommands;

import com.sirdanieliii.SD_SMP.commands.SubCommand;
import org.bukkit.entity.Player;

import java.util.List;

import static com.sirdanieliii.SD_SMP.configuration.ReturnDeathData.nonPVPDeaths;


public class deathNonPVP extends SubCommand {
    @Override
    public String getName() {
        return "nonplayer";
    }

    @Override
    public String getDescription() {
        return "§7Shows death count (No PVP)";
    }

    @Override
    public String getSyntax() {
        return "§4/death nonplayer";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        nonPVPDeaths(player);
        return true;
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        return null;
    }
}
