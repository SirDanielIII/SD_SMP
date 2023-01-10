package com.sirdanieliii.SD_SMP.commands.subcommands;

import com.sirdanieliii.SD_SMP.commands.SubCommand;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.entity.Player;

import java.util.List;

import static com.sirdanieliii.SD_SMP.commands.CommandManager.cmdHeader;
import static com.sirdanieliii.SD_SMP.configuration.ConfigManager.*;
import static com.sirdanieliii.SD_SMP.configuration.Utilities.randomMessageStrLst;
import static com.sirdanieliii.SD_SMP.configuration.Utilities.translateColourCodes;

public class deathPlayer extends SubCommand {
    @Override
    public String getName() {
        return "player";
    }

    @Override
    public String getDescription() {
        return "&7Returns your PVP death count";
    }

    @Override
    public String getSyntax() {
        return "&4" + errorMessages.get("death_player");
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (!(player.hasPermission("sd_smp.death.player"))) {
            player.sendMessage(errorMessage("permission"));
            return false;
        }
        YamlDocument config = getPlayerConfig(player);
        double deaths = config.getDouble("death_by_player");
        if (deaths == 0) player.sendMessage(translateColourCodes(cmdHeader("death") + "&7You have never been killed by a player :O"));
        else {
            if (deaths == 1) player.sendMessage(translateColourCodes(cmdHeader("death") + "&FYou have been " + randomMessageStrLst(describeKill) + " &Conce!"));
            else player.sendMessage(translateColourCodes(cmdHeader("death") + "&FYou have been " + randomMessageStrLst(describeKill) + " &C" + (int) deaths + " times!"));
        }
        return true;
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        return null;
    }
}
