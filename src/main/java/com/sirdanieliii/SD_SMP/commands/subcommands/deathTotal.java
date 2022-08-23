package com.sirdanieliii.SD_SMP.commands.subcommands;

import com.sirdanieliii.SD_SMP.commands.SubCommand;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.entity.Player;

import java.util.List;

import static com.sirdanieliii.SD_SMP.commands.CommandManager.cmdHeader;
import static com.sirdanieliii.SD_SMP.configuration.ConfigManager.*;

public class deathTotal extends SubCommand {
    @Override
    public String getName() {
        return "total";
    }

    @Override
    public String getDescription() {
        return "§7Returns your lifetime death count";
    }

    @Override
    public String getSyntax() {
        return "§4" + errorMessages.get("death_total");
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (!(player.hasPermission("sd_smp.death.total"))) {
            player.sendMessage(errorMessage("permission"));
            return false;
        }
        YamlDocument config = getPlayerConfig(player);
        double total = config.getDouble("death_by_player") + config.getDouble("death_by_nonplayer");
        if (total == 0.0) player.sendMessage(cmdHeader("death") + "§7You have never died before :O");
        else if (total == 1.0) player.sendMessage(cmdHeader("death") + "§FYou have died " + "§C" + "only once §Fin total!");
        else player.sendMessage(cmdHeader("death") + "§FYou have died " + "§C" + (int) total + " times §Fin total! Imagine.");
        return true;
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        return null;
    }
}
