package ca.sirdanieliii.SD_SMP.events;

import ca.sirdanieliii.SD_SMP.configuration.ConfigManager;
import ca.sirdanieliii.SD_SMP.configuration.configs.ConfigPlayer;
import ca.sirdanieliii.SD_SMP.items.ItemManager;
import ca.sirdanieliii.SD_SMP.utilities.Utilities;
import org.bukkit.*;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ca.sirdanieliii.SD_SMP.configuration.ConfigManager.signColourCodeSupport;
import static ca.sirdanieliii.SD_SMP.utilities.Utilities.translateMsgClr;

public class Events implements Listener {
    @EventHandler
    //Player Join Message
    public static void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Custom Messages & Configuration Setup
        if (ConfigManager.customJoinMessages) {
            String message = Utilities.randomMsgFromLst(ConfigManager.joinMessages);
            if (ConfigManager.customJoinMessagesSmileyFace) {
                event.setJoinMessage(translateMsgClr(ConfigManager.customJoinMessagesClr + player.getName() + " " + message + " :)"));
            } else {
                event.setJoinMessage(translateMsgClr(ConfigManager.customJoinMessagesClr + player.getName() + " " + message));
            }
        }
        ConfigManager.setupPlayerConfig(player);
        if (ConfigManager.welcome_enable) {
            player.sendTitle(translateMsgClr(ConfigManager.welcome.get(0)), translateMsgClr(ConfigManager.welcome.get(1)), ConfigManager.welcome_fade_in, ConfigManager.welcome_stay, ConfigManager.welcome_fade_out);
        }
        if (ConfigManager.healthUnderName) {
            Scoreboards.addPlayerToScoreboard(player);
        }
    }

    @EventHandler
    //Player Quit Message
    public static void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (ConfigManager.customQuitMessages) {
            String message = Utilities.randomMsgFromLst(ConfigManager.quitMessages);
            event.setQuitMessage(translateMsgClr(ConfigManager.customQuitMessagesClr + player.getName() + " " + message));
        }
        // Cancel player scoreboard to avoid errors when quitting the server
        if (Scoreboards.scoreboardTasks.containsKey(player.getUniqueId())) {
            Scoreboards.scoreboardTasks.get(player.getUniqueId()).stopThisScoreboardTask(Scoreboards.scoreboardTasks);
        }
    }

    @EventHandler
    //Player Sleep Message
    public static void onPlayerSleep(final PlayerBedEnterEvent event) {
        if (ConfigManager.customSleepMessages) {
            if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
                Player player = event.getPlayer();
                String message = Utilities.randomMsgFromLst(ConfigManager.sleepMessages);
                Bukkit.broadcastMessage(translateMsgClr(ConfigManager.customSleepMessagesClr + player.getName() + " " + message));
            }
        }
    }

    @EventHandler
    public static void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        int deaths;
        ConfigPlayer config = new ConfigPlayer(player);
        if (killer != null) {
            // Add to death count (Player)
            deaths = config.getConfig().getInt("death_by_player") + 1;
            config.getConfig().set("death_by_player", deaths);
            config.save();
            // Add to kill count (Killer)
            config = new ConfigPlayer(player);
            double kills = config.getConfig().getInt("kills") + 1;
            config.getConfig().set("kills", kills);
            config.save();
            // Lightning strike on player death
            if (ConfigManager.lightningOnPlayerKill) {
                player.getWorld().strikeLightningEffect(player.getLocation());
            }
        } else {
            deaths = config.getConfig().getInt("death_by_nonplayer") + 1;
            config.getConfig().set("death_by_nonplayer", deaths);
            config.save();
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public static void disablePortals(PlayerPortalEvent event) {
        if (ConfigManager.disableNetherPortal && event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            event.setCancelled(true);
            Player player = event.getPlayer();
            player.sendMessage(ConfigManager.errorMessage("disable_nether_portal"));
            player.getWorld().strikeLightning(player.getLocation());
            for (Player p : Bukkit.getOnlinePlayers())
                p.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 20, 25);
        }
        if (ConfigManager.disableEndPortal && event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            event.setCancelled(true);
            Player player = event.getPlayer();
            player.sendMessage(ConfigManager.errorMessage("disable_end_portal"));
            player.getWorld().strikeLightning(player.getLocation());
            for (Player p : Bukkit.getOnlinePlayers())
                p.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 20, 25);
        }
    }

    @EventHandler
    public static void disableNetheriteMining(BlockBreakEvent event) {
        if (ConfigManager.disableMiningNetherite && !event.getPlayer().getGameMode().equals(GameMode.CREATIVE) && event.getBlock().getType().equals(Material.ANCIENT_DEBRIS)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ConfigManager.errorMessage("disable_smithing_table"));
        }
    }

    @EventHandler
    public static void interactWithSmithingTable(PlayerInteractEvent event) {
        try {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && !event.getPlayer().getGameMode().equals(GameMode.CREATIVE) && Objects.requireNonNull(event.getClickedBlock()).getType() == Material.SMITHING_TABLE) {
                if (ConfigManager.disableSmithingTable) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ConfigManager.errorMessage("disable_smithing_table"));
                } else {
                    List<String> items = new ArrayList<>();
                    if (ConfigManager.craftNetheriteTools) items.add("tools");
                    if (ConfigManager.craftNetheriteArmour) items.add("armour");
                    if (!items.isEmpty())
                        event.getPlayer().sendMessage(Utilities.replaceStr(ConfigManager.errorMessage("disable_netherite_items"), "{item}", String.join(" & ", items)));
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
            for (Material i : tools)
                if (ConfigManager.craftNetheriteTools)
                    if (Objects.requireNonNull(event.getResult()).getType().equals(i)) event.setResult(null);
            for (Material i : armour)
                if (ConfigManager.craftNetheriteArmour)
                    if (Objects.requireNonNull(event.getResult()).getType().equals(i)) event.setResult(null);
        } catch (NullPointerException ignored) {
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public static void onElytraFly(EntityToggleGlideEvent event) {
        Player player = (Player) event.getEntity();
        ItemStack elytra;
        boolean dropElytra = true;
        List<World.Environment> dimensions = new ArrayList<>();
        if (!ConfigManager.elytraFlightOverworld) dimensions.add(World.Environment.NORMAL);
        if (!ConfigManager.elytraFlightNether) dimensions.add(World.Environment.NETHER);
        if (!ConfigManager.elytraFlightTheEnd) dimensions.add(World.Environment.THE_END);
        // Loop through dimensions where Elytra are disabled
        for (World.Environment i : dimensions) {
            if (player.getWorld().getEnvironment().equals(i)) {
                if (Objects.requireNonNull(player.getInventory().getChestplate()).getType().equals(Material.ELYTRA)) {
                    elytra = player.getInventory().getChestplate();
                    event.setCancelled(true);  // Disable elytra if player is in dimension, only if they're wearing an Elytra (fixes bug)
                    player.sendMessage(ConfigManager.errorMessage("disabled_elytra_flight"));
                } else elytra = null;
                try {
                    for (int slot = 9; slot < 36; slot++) { // Loop through inventory contents, skipping the main hotbar, side & armour slots
                        if (player.getInventory().getItem(slot) == null) { // Check if inventory slot is empty
                            if (elytra.getType().equals(Material.ELYTRA)) { // Only proceed is elytra item-stack is actually an elytra and not null, etc
                                player.getInventory().setItem(slot, elytra); // Copy player's elytra into inventory
                                player.sendMessage(ConfigManager.generalMsgs.get("force_moved_elytra"));
                            }
                            // Remove Elytra from player if wearing one
                            if (player.getInventory().getChestplate().getType().equals(Material.ELYTRA))
                                player.getInventory().setChestplate(null);
                            dropElytra = false;
                            break;
                        }
                    }
                    if (dropElytra && elytra.getType().equals(Material.ELYTRA)) {  // Proceed if elytra needs to be dropped and elytra item-stack is not null, etc
                        player.getWorld().dropItem(player.getLocation(), Objects.requireNonNull(elytra)); // Drop elytra if no inventory space
                        // Remove Elytra from player if wearing one
                        if (player.getInventory().getChestplate().getType().equals(Material.ELYTRA))
                            player.getInventory().setChestplate(null);
                        player.sendMessage(translateMsgClr(ConfigManager.generalMsgs.get("force_dropped_elytra")));
                    }
                } catch (NullPointerException ignored) {
                }
            }
        }
    }

    @EventHandler
    public static void powers(PlayerInteractEvent event) { // Wand Powers
        EquipmentSlot hand = event.getHand();
        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }
        if (!(item.isSimilar(ItemManager.wand)) || !(Objects.equals(hand, EquipmentSlot.HAND))) {
            return;
        }

        Player player = event.getPlayer();

        if (event.getAction() == Action.LEFT_CLICK_AIR) {
            try {
                player.getWorld().strikeLightning(Objects.requireNonNull(Objects.requireNonNull(player.rayTraceBlocks(360)).getHitBlock()).getLocation());
            } catch (NullPointerException ignored) {
            }
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            player.getWorld().strikeLightning((Objects.requireNonNull(event.getClickedBlock())).getLocation());
            player.getWorld().createExplosion((Objects.requireNonNull(event.getClickedBlock())).getLocation(), 3.5F);
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            // Calculate direction based on player's orientation
            Vector direction = player.getLocation().getDirection();

            // Spawn fireball at player's location with an offset
            Fireball fireball = player.getWorld().spawn(event.getPlayer().getLocation().add(new Vector(0.0D, 1.5D, 0.0D)), Fireball.class);
            fireball.setCustomName("KABOOM");
            fireball.setShooter(player);

            double speed = 1.1; // Adjust the speed as needed
            Vector velocity = direction.multiply(speed);
            fireball.setVelocity(velocity);
        }
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        if (ConfigManager.motdEnable) {
            String line1 = translateMsgClr(ConfigManager.motd[0]);
            String line2 = translateMsgClr(ConfigManager.motd[1]);
            event.setMotd(line1 + "\n" + line2);
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (!signColourCodeSupport) {
            return;
        }
        for (int i = 0; i < event.getLines().length; i++) {
            String line = event.getLine(i);
            if (line == null) {
                continue;
            }
            try {
                line = translateMsgClr(line);
            } catch (StringIndexOutOfBoundsException ignored) { // translateMsgClr will error out if an incomplete hex code is given
                continue;
            }
            event.setLine(i, line);
        }
    }
}
