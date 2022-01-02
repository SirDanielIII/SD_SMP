package com.sirdanieliii.SD_SMP.commands.subcommands;

import com.sirdanieliii.SD_SMP.commands.SubCommand;
import org.bukkit.entity.Player;

import java.util.List;

import static com.sirdanieliii.SD_SMP.configuration.ReturnDeathData.murders;

public class deathMurders extends SubCommand {
    @Override
    public String getName() {
        return "murders";
    }

    @Override
    public String getDescription() {
        return "§7Shows how many people you've murdered";
    }

    @Override
    public String getSyntax() {
        return "§4/death murders";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        murders(player);
        return true;
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        return null;
    }
}
