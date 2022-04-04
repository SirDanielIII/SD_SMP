package com.sirdanieliii.SD_SMP.commands.subcommands;

import com.sirdanieliii.SD_SMP.commands.SubCommand;
import org.bukkit.entity.Player;

import java.util.List;

import static com.sirdanieliii.SD_SMP.configuration.ReturnDeathData.kdr;
import static com.sirdanieliii.SD_SMP.events.ErrorMessages.errorMessage;

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
        if (!(player.hasPermission("death.kdr"))) {
            player.sendMessage(errorMessage("PERMISSION"));
            return true;
        }
        kdr(player);
        return true;
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        return null;
    }
}
