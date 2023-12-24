package ca.sirdanieliii.SD_SMP.commands.subcommands.death;

import ca.sirdanieliii.SD_SMP.commands.CommandManager;
import ca.sirdanieliii.SD_SMP.commands.SubCommand;
import ca.sirdanieliii.SD_SMP.configuration.ConfigManager;
import ca.sirdanieliii.SD_SMP.configuration.configs.ConfigPlayer;
import ca.sirdanieliii.SD_SMP.utilities.Utilities;
import org.bukkit.entity.Player;

import java.util.List;

public class deathTotal extends SubCommand {
    @Override
    public String getName() {
        return "total";
    }

    @Override
    public String getCmdGroup() {
        return "death";
    }

    @Override
    public String getDescription() {
        return "Returns your lifetime death count";
    }

    @Override
    public String getSyntax() {
        return ConfigManager.generalMsgs.get("death_total");
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (!(player.hasPermission("sd_smp.death.total"))) {
            player.sendMessage(ConfigManager.errorMessage("permission"));
            return false;
        }
        ConfigPlayer config = new ConfigPlayer(player);
        double total = config.getConfig().getDouble("death_by_player") + config.getConfig().getDouble("death_by_nonplayer");
        if (total == 0.0) player.sendMessage(Utilities.translateMsgClr(CommandManager.cmdHeader("death") + "&7You have never died before :O"));
        else if (total == 1.0) player.sendMessage(Utilities.translateMsgClr(CommandManager.cmdHeader("death") + "&FYou have died &Eonly once &Fin total!"));
        else player.sendMessage(Utilities.translateMsgClr(CommandManager.cmdHeader("death") + "&FYou have died " + "&C" + (int) total + " times &Fin total! Imagine."));
        return true;
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        return null;
    }
}
