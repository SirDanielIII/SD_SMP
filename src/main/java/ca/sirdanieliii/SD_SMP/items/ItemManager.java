package ca.sirdanieliii.SD_SMP.items;

import ca.sirdanieliii.SD_SMP.utilities.Utilities;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

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

        meta.setDisplayName(Utilities.translateMsgClr("&6&LWand"));
        List<String> lore = new ArrayList<>();
        lore.add(Utilities.translateMsgClr("&EThey who hold this wand "));
        lore.add(Utilities.translateMsgClr("&Ecannot comprehend its full power..."));
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DAMAGE_ALL, 29999, true);
        meta.addEnchant(Enchantment.KNOCKBACK, 25, true);
        meta.addEnchant(Enchantment.FIRE_ASPECT, 10, true);
        meta.addEnchant(Enchantment.LOOT_BONUS_MOBS, 5, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        wand = item;
    }
}