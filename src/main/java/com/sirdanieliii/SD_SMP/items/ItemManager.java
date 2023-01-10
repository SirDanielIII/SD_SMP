package com.sirdanieliii.SD_SMP.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static com.sirdanieliii.SD_SMP.configuration.Utilities.translateColourCodes;

public class ItemManager {
    public static ItemStack wand;

    public ItemManager() {
    }

    public static void init() {
        createWand();
    }

    private static void createWand() {
        ItemStack item = new ItemStack(Material.STICK, 1);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;

        meta.setDisplayName(translateColourCodes("&6&LWand"));
        List<String> lore = new ArrayList<>();
        lore.add(translateColourCodes("&EThey who hold this wand "));
        lore.add(translateColourCodes("&Ecannot comprehend its full power..."));
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DAMAGE_ALL, 10000, true);
        meta.addEnchant(Enchantment.KNOCKBACK, 25, true);
        meta.addEnchant(Enchantment.FIRE_ASPECT, 10, true);
        meta.addEnchant(Enchantment.LOOT_BONUS_MOBS, 5, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        wand = item;
    }
}
