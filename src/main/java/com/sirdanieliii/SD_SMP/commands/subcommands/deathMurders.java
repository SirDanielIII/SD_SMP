package com.sirdanieliii.SD_SMP.commands.subcommands;

import com.sirdanieliii.SD_SMP.commands.SubCommand;
import org.bukkit.entity.Player;

import java.util.List;

import static com.sirdanieliii.SD_SMP.configuration.ReturnDeathData.murders;
import static com.sirdanieliii.SD_SMP.events.ErrorMessages.errorMessage;

public class deathMurders extends SubCommand {
    @Override
    public String getName() {
        return "murders";
    }

    @Override
    public String getDescription() {
        return "§7Returns how many people you've murdered";
    }

    @Override
    public String getSyntax() {
        return "§4/death murders";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (!(player.hasPermission("death.murders"))) {
            player.sendMessage(errorMessage("PERMISSION"));
            return true;
        }
        murders(player);
        return true;
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        return null;
    }
}
