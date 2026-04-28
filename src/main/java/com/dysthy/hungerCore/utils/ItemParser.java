package com.dysthy.hungerCore.utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
public class ItemParser {
   public static ItemStack parseStringToItemStack(String itemText) {
      String[] sub = itemText.split(" ");
      Material material = Material.matchMaterial(sub[0]);
      if (material == null) {
         throw new IllegalArgumentException("Invalid material " + sub[0]);
      } else {
         int amount = Integer.parseInt(sub[1]);
         ItemStack itemStack = new ItemStack(material, amount);
         ItemMeta itemMeta = itemStack.getItemMeta();
         if (sub.length > 2) {
            for(int i = 2; i < sub.length; ++i) {
               String[] inside = sub[i].split(":");
               NamespacedKey namespacedKey = NamespacedKey.minecraft(inside[0].toUpperCase());
               Enchantment enchantment = (Enchantment)Registry.ENCHANTMENT.get(namespacedKey);
               if (enchantment == null) {
                  throw new IllegalArgumentException("Invalid enchantment " + inside[0]);
               }
               itemMeta.addEnchant(enchantment, Integer.parseInt(inside[1]), true);
            }
         }
         itemStack.setItemMeta(itemMeta);
         return itemStack;
      }
   }
}
