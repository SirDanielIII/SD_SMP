package com.sirdanieliii.SD_SMP.commands.subcommands;

import com.sirdanieliii.SD_SMP.commands.SubCommand;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.entity.Player;

import java.util.List;

import static com.sirdanieliii.SD_SMP.commands.CommandManager.cmdHeader;
import static com.sirdanieliii.SD_SMP.configuration.ConfigManager.*;
import static com.sirdanieliii.SD_SMP.configuration.Utilities.randomMessageStrLst;

public class deathKills extends SubCommand {
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
        return "§4" + errorMessages.get("death_kills");
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (!(player.hasPermission("sd_smp.death.murders"))) {
            player.sendMessage(errorMessage("permission"));
            return false;
        }
        YamlDocument config = getPlayerConfig(player);
        double kills = config.getDouble("murders");
        if (kills == 0.0) player.sendMessage(cmdHeader("death") + "§7You've never been killed anybody before :O");
        else {
            if (kills == 1.0) player.sendMessage(cmdHeader("death") + "§FYou have " + randomMessageStrLst(describeKill) + " §A" + (int) kills + " person!");
            else player.sendMessage(cmdHeader("death") + "§FYou have " + randomMessageStrLst(describeKill) + " §A" + (int) kills + " people!");
        }
        return true;
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        return null;
    }
}
