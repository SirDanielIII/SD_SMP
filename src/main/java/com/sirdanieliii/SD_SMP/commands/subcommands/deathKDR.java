package com.sirdanieliii.SD_SMP.commands.subcommands;

import com.sirdanieliii.SD_SMP.commands.SubCommand;
import org.bukkit.entity.Player;

import static com.sirdanieliii.SD_SMP.configuration.ReturnDeathData.kdr;

public class deathKDR extends SubCommand {
    @Override
    public String getName() {
        return "kdr";
    }

    @Override
    public String getDescription() {
        return "§7Shows your kill-death ratio";
    }

    @Override
    public String getSyntax() {
        return "§4/death kdr";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        kdr(player);
        return true;
    }
}
