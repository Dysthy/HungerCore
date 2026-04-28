package com.dysthy.hungerCore.config;
import com.dysthy.hungerCore.utils.IFileConfiguration;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.plugin.Plugin;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
public class SponsorsConfig {
    private static final SponsorsConfig instance = new SponsorsConfig();
    private IFileConfiguration config;
    private Plugin plugin;
    private List<ItemStack> sponsorItems;
    public void init(Plugin plugin) {
        try {
            this.plugin = plugin;
            this.config = new IFileConfiguration("sponsors", plugin);
            
            
            if (this.config.getList("items") == null) {
                this.config.set("items", List.of());
            }
            
            
            loadItems();
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error al crear/leer archivo de patrocinadores", e);
        }
    }
    
    public void loadItems() {
        sponsorItems = new ArrayList<>();
        
        if (this.config.getList("items") == null) {
            plugin.getLogger().warning("No se encontraron items en la configuración de patrocinadores");
            return;
        }
        List<?> itemsList = this.config.getList("items");
        if (itemsList == null || itemsList.isEmpty()) {
            plugin.getLogger().warning("La lista de items está vacía");
            return;
        }
        for (int i = 0; i < itemsList.size(); i++) {
            try {
                Object itemObj = itemsList.get(i);
                
                if (itemObj instanceof java.util.Map) {
                    @SuppressWarnings("unchecked")
                    java.util.Map<String, Object> itemMap = (java.util.Map<String, Object>) itemObj;
                    ItemStack item = parseItemFromMap(itemMap);
                    if (item != null) {
                        sponsorItems.add(item);
                    }
                } else if (itemObj instanceof org.bukkit.configuration.ConfigurationSection) {
                    ItemStack item = parseItemFromSection((org.bukkit.configuration.ConfigurationSection) itemObj);
                    if (item != null) {
                        sponsorItems.add(item);
                    }
                } else {
                    
                    org.bukkit.configuration.ConfigurationSection itemSection = 
                        this.config.getConfigurationSection("items." + i);
                    if (itemSection != null) {
                        ItemStack item = parseItemFromSection(itemSection);
                        if (item != null) {
                            sponsorItems.add(item);
                        }
                    } else {
                        plugin.getLogger().warning("No se pudo parsear el item en índice " + i + ". Tipo: " + 
                            (itemObj != null ? itemObj.getClass().getName() : "null"));
                    }
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Error al parsear item de patrocinador en índice " + i + ": " + e.getMessage(), e);
                e.printStackTrace();
            }
        }
        
        plugin.getLogger().info("Cargados " + sponsorItems.size() + " items patrocinables de " + itemsList.size() + " items en configuración");
    }
    
    @SuppressWarnings("deprecation")
    private ItemStack parseItemFromSection(org.bukkit.configuration.ConfigurationSection section) {
        String materialStr = section.getString("material");
        if (materialStr == null) {
            plugin.getLogger().warning("Item sin material especificado");
            return null;
        }
        Material material = Material.matchMaterial(materialStr);
        if (material == null) {
            plugin.getLogger().warning("Material inválido: " + materialStr);
            return null;
        }
        int amount = section.getInt("amount", 1);
        ItemStack item = new ItemStack(material, amount);
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        
        if (material == Material.POTION || material == Material.SPLASH_POTION || 
            material == Material.LINGERING_POTION || material == Material.TIPPED_ARROW) {
            String potionTypeStr = section.getString("potion_type");
            if (potionTypeStr != null && !potionTypeStr.isEmpty()) {
                try {
                    PotionMeta potionMeta = (PotionMeta) meta;
                    PotionType potionType = PotionType.valueOf(potionTypeStr.toUpperCase());
                    boolean extended = section.getBoolean("extended", false);
                    boolean upgraded = section.getBoolean("upgraded", false);
                    potionMeta.setBasePotionData(new PotionData(potionType, extended, upgraded));
                    meta = potionMeta; 
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Tipo de poción inválido: " + potionTypeStr);
                }
            }
        }
        
        String name = section.getString("name");
        if (name != null && !name.isEmpty()) {
            meta.displayName(Component.text(name));
        }
        
        List<String> enchantments = section.getStringList("enchantments");
        if (enchantments != null && !enchantments.isEmpty()) {
            for (String enchantStr : enchantments) {
                try {
                    String[] parts = enchantStr.split(":");
                    if (parts.length == 2) {
                        String enchantName = parts[0].trim();
                        int level = Integer.parseInt(parts[1].trim());
                        
                        NamespacedKey key = NamespacedKey.minecraft(enchantName.toLowerCase());
                        Enchantment enchantment = Registry.ENCHANTMENT.get(key);
                        
                        if (enchantment != null) {
                            meta.addEnchant(enchantment, level, true);
                        } else {
                            plugin.getLogger().warning("Encantamiento inválido: " + enchantName + " (intentando con key: " + key + ")");
                        }
                    } else {
                        plugin.getLogger().warning("Formato de encantamiento inválido: " + enchantStr + " (debe ser ENCANTAMIENTO:NIVEL)");
                    }
                } catch (Exception e) {
                    plugin.getLogger().warning("Error al parsear encantamiento: " + enchantStr + " - " + e.getMessage());
                }
            }
        }
        
        List<String> lore = section.getStringList("lore");
        if (lore != null && !lore.isEmpty()) {
            List<Component> loreComponents = new ArrayList<>();
            for (String loreLine : lore) {
                loreComponents.add(Component.text(loreLine));
            }
            meta.lore(loreComponents);
        }
        
        item.setItemMeta(meta);
        return item;
    }
    
    @SuppressWarnings("deprecation")
    private ItemStack parseItemFromMap(java.util.Map<String, Object> map) {
        Object materialObj = map.get("material");
        if (materialObj == null) {
            plugin.getLogger().warning("Item sin material especificado");
            return null;
        }
        String materialStr = materialObj.toString();
        Material material = Material.matchMaterial(materialStr);
        if (material == null) {
            plugin.getLogger().warning("Material inválido: " + materialStr);
            return null;
        }
        int amount = 1;
        Object amountObj = map.get("amount");
        if (amountObj instanceof Number) {
            amount = ((Number) amountObj).intValue();
        } else if (amountObj != null) {
            try {
                amount = Integer.parseInt(amountObj.toString());
            } catch (NumberFormatException e) {
                plugin.getLogger().warning("Cantidad inválida para item: " + amountObj);
            }
        }
        ItemStack item = new ItemStack(material, amount);
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        
        if (material == Material.POTION || material == Material.SPLASH_POTION || 
            material == Material.LINGERING_POTION || material == Material.TIPPED_ARROW) {
            Object potionTypeObj = map.get("potion_type");
            if (potionTypeObj != null) {
                try {
                    String potionTypeStr = potionTypeObj.toString().toUpperCase();
                    PotionMeta potionMeta = (PotionMeta) meta;
                    PotionType potionType = PotionType.valueOf(potionTypeStr);
                    boolean extended = false;
                    boolean upgraded = false;
                    
                    Object extendedObj = map.get("extended");
                    if (extendedObj instanceof Boolean) {
                        extended = (Boolean) extendedObj;
                    }
                    
                    Object upgradedObj = map.get("upgraded");
                    if (upgradedObj instanceof Boolean) {
                        upgraded = (Boolean) upgradedObj;
                    }
                    
                    potionMeta.setBasePotionData(new PotionData(potionType, extended, upgraded));
                    meta = potionMeta; 
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Tipo de poción inválido: " + potionTypeObj);
                }
            }
        }
        
        Object nameObj = map.get("name");
        if (nameObj != null) {
            String name = nameObj.toString();
            if (!name.isEmpty()) {
                meta.displayName(Component.text(name));
            }
        }
        
        Object enchantmentsObj = map.get("enchantments");
        if (enchantmentsObj instanceof List) {
            @SuppressWarnings("unchecked")
            List<String> enchantments = (List<String>) enchantmentsObj;
            for (String enchantStr : enchantments) {
                try {
                    String[] parts = enchantStr.split(":");
                    if (parts.length == 2) {
                        String enchantName = parts[0].trim();
                        int level = Integer.parseInt(parts[1].trim());
                        
                        NamespacedKey key = NamespacedKey.minecraft(enchantName.toLowerCase());
                        Enchantment enchantment = Registry.ENCHANTMENT.get(key);
                        
                        if (enchantment != null) {
                            meta.addEnchant(enchantment, level, true);
                        } else {
                            plugin.getLogger().warning("Encantamiento inválido: " + enchantName + " (intentando con key: " + key + ")");
                        }
                    } else {
                        plugin.getLogger().warning("Formato de encantamiento inválido: " + enchantStr + " (debe ser ENCANTAMIENTO:NIVEL)");
                    }
                } catch (Exception e) {
                    plugin.getLogger().warning("Error al parsear encantamiento: " + enchantStr + " - " + e.getMessage());
                }
            }
        }
        
        Object loreObj = map.get("lore");
        if (loreObj instanceof List) {
            @SuppressWarnings("unchecked")
            List<String> lore = (List<String>) loreObj;
            List<Component> loreComponents = new ArrayList<>();
            for (Object loreLineObj : lore) {
                loreComponents.add(Component.text(loreLineObj.toString()));
            }
            meta.lore(loreComponents);
        }
        
        item.setItemMeta(meta);
        return item;
    }
    
    public List<ItemStack> getSponsorItems() {
        return new ArrayList<>(sponsorItems);
    }
    
    public void reload() {
        try {
            this.config.reload();
            loadItems();
            plugin.getLogger().info("Configuración de patrocinadores recargada");
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error al recargar configuración de patrocinadores", e);
        }
    }
    
    public IFileConfiguration getConfig() {
        return this.config;
    }
    
    public static SponsorsConfig getInstance() {
        return instance;
    }
}
