package com.sirdanieliii.SD_SMP;

import com.sirdanieliii.SD_SMP.items.ItemManager;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.apache.commons.lang.ObjectUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.sirdanieliii.SD_SMP.configuration.ConfigManager.*;
import static com.sirdanieliii.SD_SMP.configuration.Utilities.randomMessageStrLst;
import static com.sirdanieliii.SD_SMP.configuration.Utilities.replaceErrorVariable;

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
        }
        playerSetup(player);
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
            double kills = config.getDouble("kills") + 1.0D;
            config.set("kills", kills);
            savePlayerConfig(config);
            // Lightning strike on player death
            if (lightningOnPlayerKill) player.getWorld().strikeLightningEffect(player.getLocation());
        } else {
            deaths = config.getDouble("death_by_nonplayer") + 1.0D;
            config.set("death_by_nonplayer", deaths);
            savePlayerConfig(config);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public static void disableEndPortal(PlayerPortalEvent event) {
        if (disableEndPortal && event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            event.setCancelled(true);
            Player player = event.getPlayer();
            player.sendMessage(errorMessage("disable_end_portal"));
            player.getWorld().strikeLightning(player.getLocation());
            for (Player p : Bukkit.getOnlinePlayers()) p.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 20, 25);
        }
    }


    @EventHandler
    public static void interactWithSmithingTable(PlayerInteractEvent event) {
        try {
            if (Objects.requireNonNull(event.getClickedBlock()).getType() == Material.SMITHING_TABLE) {
                if (disableSmithingTable) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(errorMessage("disable_smithing_table"));
                } else {
                    List<String> items = new ArrayList<>();
                    if (craftNetheriteTools) items.add("tools");
                    if (craftNetheriteArmour) items.add("armour");
                    if (items.size() > 0) event.getPlayer().sendMessage(replaceErrorVariable(errorMessage("disable_netherite_items"), String.join(" & ", items)));
                }
            }
        } catch (NullPointerException ignored) {
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public static void craftWithSmithingTable(PrepareSmithingEvent event) {
        Material[] tools = {Material.NETHERITE_AXE, Material.NETHERITE_HOE, Material.NETHERITE_PICKAXE, Material.NETHERITE_SHOVEL, Material.NETHERITE_SWORD};
        Material[] armour = {Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS};
        try {
            for (Material i : tools) if (craftNetheriteTools) if (Objects.requireNonNull(event.getResult()).getType().equals(i)) event.setResult(null);
            for (Material i : armour) if (craftNetheriteArmour) if (Objects.requireNonNull(event.getResult()).getType().equals(i)) event.setResult(null);
        } catch (NullPointerException ignored) {
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public static void onElytraFly(EntityToggleGlideEvent event) {
        Player player = (Player) event.getEntity();
        List<World.Environment> dimensions = new ArrayList<>();
        if (!elytraFlightOverworld) dimensions.add(World.Environment.NORMAL);
        if (!elytraFlightNether) dimensions.add(World.Environment.NETHER);
        if (!elytraFlightTheEnd) dimensions.add(World.Environment.THE_END);
        for (World.Environment i : dimensions) {
            if (player.getWorld().getEnvironment().equals(i)) {
                event.setCancelled(true);
                player.sendMessage(errorMessage("disabled_elytra_flight"));
                ItemStack elytra = player.getInventory().getChestplate();
                boolean equipped = true;
                try {
                    for (int slot = 10; slot < 32; slot++) { // Loop through inventory contents, skipping the main hotbar, side & armour slots
                        if (player.getInventory().getItem(slot) == null) {
                            player.getInventory().setItem(slot, elytra);
                            player.getInventory().setChestplate(null);
                            equipped = false;
                            break;
                        }
                    }
                    if (equipped) {
                        player.getWorld().dropItem(player.getLocation(), Objects.requireNonNull(elytra));
                        player.getInventory().setChestplate(null);
                        player.sendMessage(errorMessage("force_dropped_elytra"));
                    }
                } catch (NullPointerException ignored) {
                }
            }
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

        if (event.getAction() == Action.RIGHT_CLICK_AIR && Objects.equals((Objects.requireNonNull(event.getItem())).getItemMeta(), ItemManager.wand.getItemMeta())) {
            player = event.getPlayer();
            Fireball fire = player.getWorld().spawn(event.getPlayer().getLocation().add(new Vector(0.0D, 1.5D, 0.0D)).add(player.getVelocity()), Fireball.class);
            fire.setVelocity(fire.getVelocity().multiply(21));
            fire.setCustomName("KABOOM");
            fire.setShooter(player);
        }
    }
}