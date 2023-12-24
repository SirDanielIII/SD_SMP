package ca.sirdanieliii.SD_SMP.commands.subcommands.coordsCmds;

import ca.sirdanieliii.SD_SMP.commands.SubCommand;
import ca.sirdanieliii.SD_SMP.configuration.ConfigManager;
import ca.sirdanieliii.SD_SMP.configuration.configs.ConfigPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class coordsSend extends SubCommand {
    @Override
    public String getName() {
        return "send";
    }

    @Override
    public String getCmdGroup() {
        return "coords";
    }

    @Override
    public String getDescription() {
        return "Sends a saved coordinate or your current location to other player(s)";
    }

    @Override
    public String getSyntax() {
        return ConfigManager.generalMsgs.get("coords_send");
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if (!(player.hasPermission("sd_smp.coords.send"))) {
            player.sendMessage(ConfigManager.errorMessage("permission"));
            return false;
        }
        ConfigPlayer config = new ConfigPlayer(player);
        if (args.length == 1) {
            player.sendMessage(ConfigManager.errorMessage("coords_send"));
            return false;
        } else if (args.length == 2) { // /coords send <word>
            if (args[1].equalsIgnoreCase("here")) player.sendMessage(ConfigManager.errorMessage("missing_players"));
            else player.sendMessage(ConfigManager.errorMessage("no_args_to_send_coord"));
            return false;
        } else if (args.length == 3) { // /coords send here [player]   |   /coords send home [dimension] [player]
        } else {  // /coords send home [dimension] [player]
        }
        return true;
    }

    protected void sendCoords(ConfigPlayer config, Player player, String[] args, int startArg) {
        ArrayList<Integer> coords = new ArrayList<>();
        String dimension;
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        // /coords send <here> [players] OR /coords send <name> <dimension> [players]>
        ConfigPlayer config = new ConfigPlayer(player);
        ArrayList<String> secondArgs = new ArrayList<>(List.of("here"));
        List<String> list = new ArrayList<>();
        return list;
    }
}
