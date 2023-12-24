package ca.sirdanieliii.SD_SMP.commands.subcommands.death;

import ca.sirdanieliii.SD_SMP.commands.CommandManager;
import ca.sirdanieliii.SD_SMP.commands.SubCommand;
import ca.sirdanieliii.SD_SMP.configuration.ConfigManager;
import ca.sirdanieliii.SD_SMP.configuration.configs.ConfigPlayer;
import ca.sirdanieliii.SD_SMP.utilities.Utilities;
import org.bukkit.entity.Player;

import java.util.List;

public class deathKDR extends SubCommand {
    @Override
    public String getName() {
        return "kdr";
    }

    @Override
    public String getCmdGroup() {
        return "death";
    }

    @Override
    public String getDescription() {
        return "Returns your kill-death (K/D) ratio";
    }

    @Override
    public String getSyntax() {
        return ConfigManager.generalMsgs.get("death_kdr");
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (!(player.hasPermission("sd_smp.death.kdr"))) {
            player.sendMessage(ConfigManager.errorMessage("permission"));
            return false;
        }
        ConfigPlayer config = new ConfigPlayer(player);
        double kills = config.getConfig().getDouble("kills");
        double death_by_player = config.getConfig().getDouble("death_by_player");
        if (kills == 0) player.sendMessage(Utilities.translateMsgClr(CommandManager.cmdHeader("death") + "&7You have not killed anybody yet :O"));
        else {
            // Calculate K/D
            double kd;
            if (death_by_player == 0) kd = kills;  // Fix Math Error When Diving By Zero
            else kd = kills / death_by_player;
            // Display K/D
            if (kd < 0.5) player.sendMessage(Utilities.translateMsgClr(CommandManager.cmdHeader("death") + "&FYour K/D ratio is &C" + String.format("%.2f", kd)));
            else if (kd > 0.5 && kd < 1.0) player.sendMessage(Utilities.translateMsgClr(CommandManager.cmdHeader("death") + "&FYour K/D ratio is &E" + String.format("%.2f", kd)));
            else if (kd >= 1.0) player.sendMessage(Utilities.translateMsgClr(CommandManager.cmdHeader("death") + "&FYour K/D ratio is &A" + String.format("%.2f", kd)));
        }
        return true;
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        return null;
    }
}
