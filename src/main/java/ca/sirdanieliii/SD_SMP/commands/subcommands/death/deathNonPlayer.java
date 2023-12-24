package ca.sirdanieliii.SD_SMP.commands.subcommands.death;

import ca.sirdanieliii.SD_SMP.commands.CommandManager;
import ca.sirdanieliii.SD_SMP.commands.SubCommand;
import ca.sirdanieliii.SD_SMP.configuration.ConfigManager;
import ca.sirdanieliii.SD_SMP.configuration.configs.ConfigPlayer;
import ca.sirdanieliii.SD_SMP.utilities.Utilities;
import org.bukkit.entity.Player;

import java.util.List;


public class deathNonPlayer extends SubCommand {
    @Override
    public String getName() {
        return "nonplayer";
    }

    @Override
    public String getCmdGroup() {
        return "death";
    }

    @Override
    public String getDescription() {
        return "Returns your death count, excluding PVP";
    }

    @Override
    public String getSyntax() {
        return ConfigManager.generalMsgs.get("death_nonplayer");
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (!(player.hasPermission("sd_smp.death.nonplayer"))) {
            player.sendMessage(ConfigManager.errorMessage("permission"));
            return false;
        }
        ConfigPlayer config = new ConfigPlayer(player);
        double deaths = config.getConfig().getDouble("death_by_nonplayer");
        if (deaths == 0) player.sendMessage(Utilities.translateMsgClr(CommandManager.cmdHeader("death") + "&7You have never died on your own before :O"));
        else {
            if (deaths == 1)
                player.sendMessage(Utilities.translateMsgClr(CommandManager.cmdHeader("death") +
                        "&FYou have died &Eonly once &Fdue to " + Utilities.randomMsgFromLst(ConfigManager.describeDeath)));
            else
                player.sendMessage(Utilities.translateMsgClr(CommandManager.cmdHeader("death") +
                        "&FYou have died " + "&C" + (int) deaths + " times &Fdue to " + Utilities.randomMsgFromLst(ConfigManager.describeDeath)));
        }
        return true;
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        return null;
    }
}
