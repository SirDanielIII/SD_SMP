package ca.sirdanieliii.SD_SMP.commands.subcommands.death;

import ca.sirdanieliii.SD_SMP.commands.CommandManager;
import ca.sirdanieliii.SD_SMP.commands.SubCommand;
import ca.sirdanieliii.SD_SMP.configuration.ConfigManager;
import ca.sirdanieliii.SD_SMP.configuration.configs.ConfigPlayer;
import ca.sirdanieliii.SD_SMP.utilities.Utilities;
import org.bukkit.entity.Player;

import java.util.List;

public class deathKills extends SubCommand {
    @Override
    public String getName() {
        return "kills";
    }

    @Override
    public String getCmdGroup() {
        return "death";
    }

    @Override
    public String getDescription() {
        return "Return your player kill count";
    }

    @Override
    public String getSyntax() {
        return ConfigManager.generalMsgs.get("death_kills");
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (!(player.hasPermission("sd_smp.death.murders"))) {
            player.sendMessage(ConfigManager.errorMessage("permission"));
            return false;
        }
        ConfigPlayer config = new ConfigPlayer(player);
        double kills = config.getConfig().getDouble("kills");
        if (kills == 0.0) player.sendMessage(Utilities.translateMsgClr(CommandManager.cmdHeader("death") + "&7You have never been killed anybody before :O"));
        else {
            if (kills == 1.0)
                player.sendMessage(Utilities.translateMsgClr(CommandManager.cmdHeader("death") + "&FYou have " + Utilities.randomMsgFromLst(ConfigManager.describeKill) + " &A" + (int) kills + " person!"));
            else
                player.sendMessage(Utilities.translateMsgClr(CommandManager.cmdHeader("death") + "&FYou have " + Utilities.randomMsgFromLst(ConfigManager.describeKill) + " &A" + (int) kills + " people!"));
        }
        return true;
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        return null;
    }
}
