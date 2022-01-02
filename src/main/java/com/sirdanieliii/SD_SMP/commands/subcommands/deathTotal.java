package com.sirdanieliii.SD_SMP.commands.subcommands;

import com.sirdanieliii.SD_SMP.commands.SubCommand;
import org.bukkit.entity.Player;

import java.util.List;

import static com.sirdanieliii.SD_SMP.configuration.ReturnDeathData.totalDeaths;

public class deathTotal extends SubCommand {
    @Override
    public String getName() {
        return "total";
    }

    @Override
    public String getDescription() {
        return "§7Shows your lifetime death count";
    }

    @Override
    public String getSyntax() {
        return "§4/death total";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        totalDeaths(player);
        return true;
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        return null;
    }
}
