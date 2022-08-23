package com.sirdanieliii.SD_SMP.commands.subcommands;

import com.sirdanieliii.SD_SMP.commands.SubCommand;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.entity.Player;

import java.util.List;

import static com.sirdanieliii.SD_SMP.commands.CommandManager.cmdHeader;
import static com.sirdanieliii.SD_SMP.configuration.ConfigManager.*;

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
        return "§4" + errorMessages.get("death_kdr");
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (!(player.hasPermission("sd_smp.death.kdr"))) {
            player.sendMessage(errorMessage("permission"));
            return false;
        }
        YamlDocument config = getPlayerConfig(player);
        double kills = config.getDouble("kills");
        double death_by_player = config.getDouble("death_by_player");
        if (kills == 0) player.sendMessage(cmdHeader("death") + "§7You have not killed anybody yet :O");
        else {
            // Calculate K/D
            double kd;
            if (death_by_player == 0) kd = kills;  // Fix Math Error When Diving By Zero
            else kd = kills / death_by_player;
            // Display K/D
            if (kd < 0.5) player.sendMessage(cmdHeader("death") + "§FYour K/D ratio is §C" + String.format("%.2f", kd));
            else if (kd > 0.5 && kd < 1.0) player.sendMessage(cmdHeader("death") + "§FYour K/D ratio is §E" + String.format("%.2f", kd));
            else if (kd >= 1.0) player.sendMessage(cmdHeader("death") + "§FYour K/D ratio is §A" + String.format("%.2f", kd));
        }
        return true;
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        return null;
    }
}
