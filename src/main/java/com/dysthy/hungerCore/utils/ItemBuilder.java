package com.dysthy.hungerCore.utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
public class ItemBuilder {
   private final ItemStack is;
   public ItemBuilder(Material m) {
      this(m, 1);
   }
   public ItemBuilder(ItemStack is) {
      this.is = is;
   }
   public ItemBuilder(Material m, int amount) {
      this.is = new ItemStack(m, amount);
   }
   public ItemBuilder clone() {
      return new ItemBuilder(this.is);
   }
   public ItemBuilder setName(String name) {
      ItemMeta im = this.is.getItemMeta();
      im.displayName(Component.text(name));
      this.is.setItemMeta(im);
      return this;
   }
   public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level) {
      this.is.addUnsafeEnchantment(ench, level);
      return this;
   }
   public ItemBuilder removeEnchantment(Enchantment ench) {
      this.is.removeEnchantment(ench);
      return this;
   }
   public ItemBuilder setSkullOwner(String owner) {
      try {
         SkullMeta im = (SkullMeta)this.is.getItemMeta();
         im.setOwningPlayer(Bukkit.getOfflinePlayer(owner));
         this.is.setItemMeta(im);
      } catch (ClassCastException var3) {
      }
      return this;
   }
   public ItemBuilder addEnchant(Enchantment ench, int level) {
      ItemMeta im = this.is.getItemMeta();
      im.addEnchant(ench, level, true);
      this.is.setItemMeta(im);
      return this;
   }
   public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
      this.is.addEnchantments(enchantments);
      return this;
   }
   public ItemBuilder setLore(String... lore) {
      List<Component> components = new ArrayList();
      List<String> lines = Arrays.asList(lore);
      lines.forEach((line) -> {
         components.add(Component.text(line));
      });
      ItemMeta im = this.is.getItemMeta();
      im.lore(components);
      this.is.setItemMeta(im);
      return this;
   }
   public ItemBuilder setLore(List<String> lore) {
      List<Component> components = new ArrayList();
      lore.forEach((line) -> {
         components.add(Component.text(line));
      });
      ItemMeta im = this.is.getItemMeta();
      im.lore(components);
      this.is.setItemMeta(im);
      return this;
   }
   public ItemBuilder removeLoreLine(String line) {
      ItemMeta im = this.is.getItemMeta();
      List<Component> lore = new ArrayList((Collection)Objects.requireNonNull(im.lore()));
      if (!lore.contains(Component.text(line))) {
         return this;
      } else {
         lore.remove(Component.text(line));
         im.lore(lore);
         this.is.setItemMeta(im);
         return this;
      }
   }
   public ItemBuilder removeLoreLine(int index) {
      ItemMeta im = this.is.getItemMeta();
      List<Component> lore = new ArrayList((Collection)Objects.requireNonNull(im.lore()));
      if (index >= 0 && index <= lore.size()) {
         lore.remove(index);
         im.lore(lore);
         this.is.setItemMeta(im);
         return this;
      } else {
         return this;
      }
   }
   public ItemBuilder addLoreLine(String line) {
      ItemMeta im = this.is.getItemMeta();
      List<Component> lore = new ArrayList();
      if (im.hasLore()) {
         lore = new ArrayList((Collection)Objects.requireNonNull(im.lore()));
      }
      lore.add(Component.text(line));
      im.lore(lore);
      this.is.setItemMeta(im);
      return this;
   }
   public ItemBuilder addLoreLine(String line, int pos) {
      ItemMeta im = this.is.getItemMeta();
      List<Component> lore = new ArrayList((Collection)Objects.requireNonNull(im.lore()));
      lore.set(pos, Component.text(line));
      im.lore(lore);
      this.is.setItemMeta(im);
      return this;
   }
   public ItemBuilder setLeatherArmorColor(Color color) {
      try {
         LeatherArmorMeta im = (LeatherArmorMeta)this.is.getItemMeta();
         im.setColor(color);
         this.is.setItemMeta(im);
      } catch (ClassCastException var3) {
      }
      return this;
   }
   public ItemStack build() {
      return this.is;
   }
}
