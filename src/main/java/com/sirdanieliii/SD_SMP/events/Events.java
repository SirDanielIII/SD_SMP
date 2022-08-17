package com.sirdanieliii.SD_SMP.events;

import com.sirdanieliii.SD_SMP.items.ItemManager;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;

import java.util.Objects;

import static com.sirdanieliii.SD_SMP.configuration.ConfigManager.*;
import static com.sirdanieliii.SD_SMP.configuration.Utilities.randomMessageStrLst;

public class Events implements Listener {
    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        event.setMotd(MoTD.get(0) + "\n" + MoTD.get(1));
    }

    @EventHandler
    //Player Join Message
    public static void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (customJoinMessages) {
            String message = randomMessageStrLst(joinMessages);
            event.setJoinMessage("§E" + player.getName() + " " + message + " :)");
            playerSetup(player);
        }
        player.sendTitle(welcome.get(0), welcome.get(1), 20, 70, 20);
    }

    @EventHandler
    //Player Quit Message
    public static void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (customQuitMessages) {
            String message = randomMessageStrLst(quitMessages);
            event.setQuitMessage("§C" + player.getName() + " " + message);
        }
    }

    @EventHandler
    //Player Sleep Message
    public static void onPlayerSleep(final PlayerBedEnterEvent event) {
        if (customSleepMessages) {
            if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
                Player player = event.getPlayer();
                String message = randomMessageStrLst(sleepMessages);
                Bukkit.broadcastMessage("§B" + player.getName() + " " + message);
            }
        }
    }

    @EventHandler
    public static void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        double deaths;
        YamlDocument config = getPlayerConfig(player);

        if (killer != null) {
            // Add to death count (Player)
            deaths = config.getDouble("death_by_player") + 1.0D;
            config.set("death_by_player", deaths);
            savePlayerConfig(config);

            // Add to kill count (Killer)
            config = getPlayerConfig(killer);
            double kills = config.getDouble("murders") + 1.0D;
            config.set("murders", kills);
            savePlayerConfig(config);
        } else {
            deaths = config.getDouble("death_by_other") + 1.0D;
            config.set("death_by_other", deaths);
            savePlayerConfig(config);
        }
    }

    @EventHandler
    public static void powers(PlayerInteractEvent event) { // Wand Powers
        EquipmentSlot hand = event.getHand();
        Player player;
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && Objects.equals(hand, EquipmentSlot.HAND) && event.getItem() != null &&
                Objects.equals(event.getItem().getItemMeta(), ItemManager.wand.getItemMeta())) {
            player = event.getPlayer();
            player.getWorld().strikeLightning((Objects.requireNonNull(event.getClickedBlock())).getLocation());
            player.getWorld().createExplosion((Objects.requireNonNull(event.getClickedBlock())).getLocation(), 3.5F);
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR && Objects.equals((Objects.requireNonNull(event.getItem())).getItemMeta(),
                ItemManager.wand.getItemMeta())) {
            player = event.getPlayer();
            Fireball fire = player.getWorld().spawn(event.getPlayer().getLocation().add(new Vector(0.0D, 1.5D, 0.0D))
                    .add(player.getVelocity()), Fireball.class);
            fire.setFireTicks(0);
            fire.setShooter(player);
        }
    }
}