package com.sirdanieliii.SD_SMP.commands.subcommands;

import com.sirdanieliii.SD_SMP.commands.SubCommand;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.entity.Player;

import java.util.List;

import static com.sirdanieliii.SD_SMP.commands.CommandManager.cmdHeader;
import static com.sirdanieliii.SD_SMP.configuration.ConfigManager.*;
import static com.sirdanieliii.SD_SMP.configuration.Utilities.randomMessageStrLst;
import static com.sirdanieliii.SD_SMP.configuration.Utilities.translateColourCodes;


public class deathNonPlayer extends SubCommand {
    @Override
    public String getName() {
        return "nonplayer";
    }

    @Override
    public String getDescription() {
        return "&7Returns your death count, excluding PVP";
    }

    @Override
    public String getSyntax() {
        return "&4" + errorMessages.get("death_nonplayer");
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (!(player.hasPermission("sd_smp.death.nonplayer"))) {
            player.sendMessage(errorMessage("permission"));
            return false;
        }
        YamlDocument config = getPlayerConfig(player);
        double deaths = config.getDouble("death_by_nonplayer");
        if (deaths == 0) player.sendMessage(translateColourCodes(cmdHeader("death") + "&7You have never died on your own before :O"));
        else {
            if (deaths == 1)
                player.sendMessage(translateColourCodes(cmdHeader("death") +
                        "&FYou have died &Eonly once &Fdue to " + randomMessageStrLst(describeDeath)));
            else
                player.sendMessage(translateColourCodes(cmdHeader("death") +
                        "&FYou have died " + "&C" + (int) deaths + " times &Fdue to " + randomMessageStrLst(describeDeath)));
        }
        return true;
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        return null;
    }
}
