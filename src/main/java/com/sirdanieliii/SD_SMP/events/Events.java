package com.sirdanieliii.SD_SMP.events;

import com.sirdanieliii.SD_SMP.items.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Objects;

import static com.sirdanieliii.SD_SMP.SD_SMP.PLAYER_CONFIG;
import static com.sirdanieliii.SD_SMP.SD_SMP.SMP_CONFIG;
import static com.sirdanieliii.SD_SMP.configuration.CreatePlayerConfig.createPlayerConfig;
import static com.sirdanieliii.SD_SMP.events.Utilities.randomMessage;

public class Events implements Listener {
    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        SMP_CONFIG.setup("", "config");
        SMP_CONFIG.reload();
        event.setMotd(SMP_CONFIG.getConfig().getString("MoTD.1") + "\n" + SMP_CONFIG.getConfig().getString("MoTD.2"));
    }

    @EventHandler
    //Player Join Message
    public static void onPlayerJoin(PlayerJoinEvent event) {
        SMP_CONFIG.setup("", "config");
        SMP_CONFIG.reload();
        Player player = event.getPlayer();
        if (SMP_CONFIG.getConfig().getBoolean("enable-custom-join-messages")) {
            String message = randomMessage("join", player);
            event.setJoinMessage("§E" + player.getName() + " " + message + " :)");
            createPlayerConfig(player);
        }
        player.sendTitle(SMP_CONFIG.getConfig().getString("welcome.title"),
                SMP_CONFIG.getConfig().getString("welcome.subtitle"), 20, 70, 20);
    }

    @EventHandler
    //Player Quit Message
    public static void onPlayerLeave(PlayerQuitEvent event) {
        SMP_CONFIG.setup("", "config");
        SMP_CONFIG.reload();
        Player player = event.getPlayer();
        if (SMP_CONFIG.getConfig().getBoolean("enable-custom-quit-messages")) {
            String message = randomMessage("quit", player);
            event.setQuitMessage("§C" + player.getName() + " " + message);
        }
    }

    @EventHandler
    //Player Sleep Message
    public static void onPlayerSleep(final PlayerBedEnterEvent event) {
        if (SMP_CONFIG.getConfig().getBoolean("enable-custom-sleep-messages")) {
            if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
                Player player = event.getPlayer();
                String message = randomMessage("sleep", player);
                Bukkit.broadcastMessage("§B" + player.getName() + " " + message);
            }
        }
    }

    @EventHandler
    public static void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        double deaths;
        PLAYER_CONFIG.setup("playerdata", player.getUniqueId().toString());
        PLAYER_CONFIG.reload();
        if (killer != null) {
            // Add to death count (Player)
            deaths = PLAYER_CONFIG.getConfig().getDouble("death_by_player") + 1.0D;
            PLAYER_CONFIG.getConfig().set("death_by_player", deaths);
            PLAYER_CONFIG.save();
            // Add to kill count (Killer)
            PLAYER_CONFIG.setup("playerdata", killer.getUniqueId().toString());
            PLAYER_CONFIG.reload();
            double kills = PLAYER_CONFIG.getConfig().getDouble("murders") + 1.0D;
            PLAYER_CONFIG.getConfig().set("murders", kills);
        } else {
            deaths = PLAYER_CONFIG.getConfig().getDouble("death_by_other") + 1.0D;
            PLAYER_CONFIG.getConfig().set("death_by_other", deaths);
        }
        PLAYER_CONFIG.save();
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

        if (event.getAction() == Action.RIGHT_CLICK_AIR && Objects.equals(((ItemStack) Objects.requireNonNull(event.getItem())).getItemMeta(),
                ItemManager.wand.getItemMeta())) {
            player = event.getPlayer();
            Fireball fire = player.getWorld().spawn(event.getPlayer().getLocation().add(new Vector(0.0D, 1.5D, 0.0D))
                    .add(player.getVelocity()), Fireball.class);
            fire.setFireTicks(0);
            fire.setShooter(player);
        }
    }
}