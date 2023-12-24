package ca.sirdanieliii.SD_SMP.commands.subcommands.death;

import ca.sirdanieliii.SD_SMP.commands.CommandManager;
import ca.sirdanieliii.SD_SMP.commands.SubCommand;
import ca.sirdanieliii.SD_SMP.configuration.ConfigManager;
import ca.sirdanieliii.SD_SMP.configuration.configs.ConfigPlayer;
import ca.sirdanieliii.SD_SMP.utilities.Utilities;
import org.bukkit.entity.Player;

import java.util.List;

public class deathPlayer extends SubCommand {
    @Override
    public String getName() {
        return "player";
    }

    @Override
    public String getCmdGroup() {
        return "death";
    }

    @Override
    public String getDescription() {
        return "Returns your PVP death count";
    }

    @Override
    public String getSyntax() {
        return ConfigManager.generalMsgs.get("death_player");
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (!(player.hasPermission("sd_smp.death.player"))) {
            player.sendMessage(ConfigManager.errorMessage("permission"));
            return false;
        }
        ConfigPlayer config = new ConfigPlayer(player);
        double deaths = config.getConfig().getDouble("death_by_player");
        if (deaths == 0) player.sendMessage(Utilities.translateMsgClr(CommandManager.cmdHeader("death") + "&7You have never been killed by a player :O"));
        else {
            if (deaths == 1) player.sendMessage(Utilities.translateMsgClr(CommandManager.cmdHeader("death") + "&FYou have been " + Utilities.randomMsgFromLst(ConfigManager.describeKill) + " &Conce!"));
            else player.sendMessage(Utilities.translateMsgClr(CommandManager.cmdHeader("death") + "&FYou have been " + Utilities.randomMsgFromLst(ConfigManager.describeKill) + " &C" + (int) deaths + " times!"));
        }
        return true;
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        return null;
    }
}
