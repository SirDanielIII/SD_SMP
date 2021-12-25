package com.sirdanieliii.SD_SMP.configuration;

import org.bukkit.entity.Player;

import static com.sirdanieliii.SD_SMP.SD_SMP.PLAYER_CONFIG;
import static com.sirdanieliii.SD_SMP.commands.CommandManager.headers;
import static com.sirdanieliii.SD_SMP.events.Utilities.randomMessage;

public class ReturnDeathData {
    public static void murders(Player player) {
        PLAYER_CONFIG.setup("playerdata", player.getUniqueId().toString());
        PLAYER_CONFIG.reload();
        double kills = PLAYER_CONFIG.getConfig().getDouble("murders");
        if (kills == 0.0) {
            player.sendMessage(headers("DEATH") + "§7You've never been killed anybody before :O");
        } else {
            PLAYER_CONFIG.reload();
            if (kills == 1.0) {
                player.sendMessage(headers("DEATH") + "§FYou have " + randomMessage("kill", player) + " §A" + (int) kills + " person!");
            } else {
                player.sendMessage(headers("DEATH") + "§FYou have " + randomMessage("kill", player) + " §A" + (int) kills + " people!");
            }
        }
    }

    public static void pvpDeaths(Player player) {
        PLAYER_CONFIG.setup("playerdata", player.getUniqueId().toString());
        PLAYER_CONFIG.reload();
        double deaths = PLAYER_CONFIG.getConfig().getDouble("death_by_player");
        if (deaths == 0) {
            player.sendMessage(headers("DEATH") + "§7You've never been killed by a player before :O");
        } else {
            PLAYER_CONFIG.reload();
            if (deaths == 1) {
                player.sendMessage(headers("DEATH") + "§FYou have been " + randomMessage("kill", player) + " §Conce!");
            } else {
                player.sendMessage(headers("DEATH") + "§FYou have been " + randomMessage("kill", player) + " §C" + (int) deaths + " times!");
            }
        }
    }

    public static void nonPVPDeaths(Player player) {
        PLAYER_CONFIG.setup("playerdata", player.getUniqueId().toString());
        PLAYER_CONFIG.reload();
        double deaths = PLAYER_CONFIG.getConfig().getDouble("death_by_other");
        if (deaths == 0) {
            player.sendMessage(headers("DEATH") + "§7You've never died to " + randomMessage("death", player) + " before!");
        } else {
            PLAYER_CONFIG.reload();
            if (deaths == 1) {
                player.sendMessage(headers("DEATH") + "§FYou have died " + "§C" + "only once!" + "§F due to " + randomMessage("death", player));
            } else {
                player.sendMessage(headers("DEATH") + "§FYou have died " + "§C" + (int) deaths + " times §Fdue to " + randomMessage("death", player));
            }
        }
    }

    public static void totalDeaths(Player player) {
        PLAYER_CONFIG.setup("playerdata", player.getUniqueId().toString());
        PLAYER_CONFIG.reload();
        double death_by_player = PLAYER_CONFIG.getConfig().getDouble("death_by_player");
        double death_by_other = PLAYER_CONFIG.getConfig().getDouble("death_by_other");
        double total = death_by_player + death_by_other;
        if (total == 0.0) {
            player.sendMessage(headers("DEATH") + "§7You've never died before!");
        } else if (total == 1.0) {
            player.sendMessage(headers("DEATH") + "§FYou have died " + "§C" + "only once §Fin total!");
        } else {
            player.sendMessage(headers("DEATH") + "§FYou have died " + "§C" + (int) total + " times §Fin total! Imagine.");
        }
    }

    public static void kdr(Player player) {
        PLAYER_CONFIG.setup("playerdata", player.getUniqueId().toString());
        PLAYER_CONFIG.reload();
        double kills = PLAYER_CONFIG.getConfig().getDouble("murders");
        double death_by_player = PLAYER_CONFIG.getConfig().getDouble("death_by_player");
        if (kills == 0) player.sendMessage(headers("DEATH") + "§7You haven't killed anybody yet!");
        else {
            // Calculate K/D
            double kd = 0;
            if (death_by_player == 0) {
                kd = kills;  // Fix Math Error When Diving By Zero
            } else kd = kills / death_by_player;
            // Display K/D
            if (kd < 0.5) {
                player.sendMessage(headers("DEATH") + "§FYour K/D ratio is §C" + String.format("%.2f", kd));
            } else if (kd > 0.5 && kd < 1.0) {
                player.sendMessage(headers("DEATH") + "§FYour K/D ratio is §E" + String.format("%.2f", kd));
            } else if (kd >= 1.0) {
                player.sendMessage(headers("DEATH") + "§FYour K/D ratio is §A" + String.format("%.2f", kd));
            }
        }
    }
}
