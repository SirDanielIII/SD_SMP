package ca.sirdanieliii.SD_SMP.commands.subcommands.coordsCmds;

import ca.sirdanieliii.SD_SMP.commands.SubCommand;
import ca.sirdanieliii.SD_SMP.configuration.ConfigManager;
import ca.sirdanieliii.SD_SMP.configuration.configs.ConfigPlayer;
import ca.sirdanieliii.SD_SMP.coords.Coord;
import ca.sirdanieliii.SD_SMP.utilities.Utilities;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static ca.sirdanieliii.SD_SMP.utilities.Utilities.replaceStr;

public class coordsList extends SubCommand {
    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getCmdGroup() {
        return "coords";
    }

    @Override
    public String getDescription() {
        return "Lists saved coordinate(s)";
    }

    @Override
    public String getSyntax() {
        return ConfigManager.generalMsgs.get("coords_list");
    }

    @Override // coords list all
    public boolean perform(Player player, String[] args) {
        if (!(player.hasPermission("sd_smp.coords.list"))) {
            player.sendMessage(ConfigManager.errorMessage("permission"));
            return false;
        }
        ConfigPlayer config = new ConfigPlayer(player);
        // /coords list <name|all> <dimension> [world]
        switch (args.length) {
            case (1) -> { // coords list
                player.sendMessage(ConfigManager.errorMessage("coords_list"));
                return false;
            }
            case (2) -> { // /coords list <name|all>
                if (args[1].equalsIgnoreCase("all")) {
                    Coord.showAllCoords(config, player, 0);
                    return true;
                }
                // Do name logic here


                player.sendMessage(ConfigManager.errorMessage("no_args_to_set_coord"));
                return false;
            }
            case (3) -> { // /coords list <name|all> dimension or /coords list all <page>


            } case (4) -> { // /coords list <name|all> [dimension] [world] or /coords list all <dimension> <page>

            }
            // Number of arguments are more than 7
            default -> {
                player.sendMessage(Utilities.replaceStr(ConfigManager.errorMessage("too_many_arguments"), "{cmd_syntax}", ConfigManager.generalMsgs.get("coords_list")));
            }
        }
        return true;
    }

    private ArrayList<ArrayList<String>> getAllCoords(ConfigPlayer config, String[] dimensions) {
        ArrayList<ArrayList<String>> coordsAll = new ArrayList<>();
        return coordsAll;
    }

    private String isAtMSG(ConfigPlayer config, String dimension, String name) {
        return "";
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        // /coords list <name | all> <dimension>
        ConfigPlayer config = new ConfigPlayer(player);
        ArrayList<String> secondArgs = new ArrayList<>(List.of("all"));
        List<String> list = new ArrayList<>();
        return list;
    }
}
