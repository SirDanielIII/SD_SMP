package ca.sirdanieliii.SD_SMP.events;

import ca.sirdanieliii.SD_SMP.SD_SMP;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static ca.sirdanieliii.SD_SMP.configuration.ConfigManager.healthUnderNameText;
import static ca.sirdanieliii.SD_SMP.configuration.ConfigManager.healthUnderNameUpdate;
import static ca.sirdanieliii.SD_SMP.utilities.Utilities.translateMsgClr;


public final class Scoreboards {

    private static final String OBJ_NAME = "sd_smp_health";
    private static final String OBJ_CRITERIA = "dummy"; // Can use "health" but we want to avoid read-only exceptions
    private static final String OBJ_DISPLAY = translateMsgClr(healthUnderNameText);
    private static int taskID;

    /**
     * Enable / reload the below-name health display.
     */
    public static void reloadHealthScoreboard() {
        disableHealthScoreboard();
        startUpdating();
    }

    /**
     * Disable the display entirely and unregister our objective.
     */
    public static void disableHealthScoreboard() {
        stopUpdating();

        for (Player p : Bukkit.getOnlinePlayers()) {
            Scoreboard board = p.getScoreboard();
            Objective obj = board.getObjective(OBJ_NAME);
            if (obj != null) obj.unregister();
        }
    }

    /**
     * Start the repeating task (loop if already running).
     */
    private static void startUpdating() {
        if (!Bukkit.getScheduler().isQueued(taskID)) {
            taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(SD_SMP.getThisPlugin(), Scoreboards::tick, 0L, healthUnderNameUpdate);
        }
    }

    /**
     * Stop the repeating task.
     */
    private static void stopUpdating() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    /**
     * Update each online player's health scoreboard with a cached snapshot.
     * This method fixes the 0 health entry bug.
     */
    private static void tick() {
        // Cache each player’s current health once per task tick
        Map<String, Integer> healthNow = new HashMap<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            healthNow.put(p.getName(), (int) Math.ceil(p.getHealth()));
        }

        // For every "viewer," ensure the objective exists and write all scores
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            Scoreboard board = viewer.getScoreboard();
            Objective obj = board.getObjective(OBJ_NAME);

            if (obj == null) {
                obj = board.registerNewObjective(OBJ_NAME, OBJ_CRITERIA, OBJ_DISPLAY);
                obj.setDisplaySlot(DisplaySlot.BELOW_NAME);
            }

            // Write health for every online player onto this board
            for (Map.Entry<String, Integer> entry : healthNow.entrySet()) {
                obj.getScore(entry.getKey()).setScore(entry.getValue());
            }
        }
    }

    /**
     * Clear the leaving player’s health entry.
     * Unregister the objective only if this scoreboard is NOT the main board
     * (i.e. it’s truly a per-player board) or if no other entries remain.
     *
     * @param player A Minecraft player
     */
    public static void clearPlayerHealthObjective(Player player) {
        Scoreboard board = player.getScoreboard();
        Objective obj = board.getObjective(OBJ_NAME);
        if (obj == null) return;

        // Remove just this player’s score line
        board.resetScores(player.getName());

        // If it’s a per-player board (not the shared one) OR now empty, tidy it up
        boolean isMain = board.equals(Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard());
        if (!isMain || Objects.requireNonNull(obj.getScoreboard()).getEntries().isEmpty()) {
            obj.unregister();
        }
    }
}