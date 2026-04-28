package com.dysthy.hungerCore.config;
import com.dysthy.hungerCore.utils.IFileConfiguration;
import com.dysthy.hungerCore.utils.ItemParser;
import com.dysthy.hungerCore.utils.LocationUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
public class ChestsFile {
   private static final ChestsFile instance = new ChestsFile();
   private IFileConfiguration config;
   private Plugin plugin;
   public void init(Plugin plugin) {
      try {
         this.plugin = plugin;
         this.config = new IFileConfiguration("chests", plugin);
         if (this.config.getList("content") == null) {
            this.config.set("content", List.of());
         }
         if (this.config.getList("locations") == null) {
            this.config.set("locations", List.of());
         }
      } catch (Exception var3) {
         plugin.getLogger().log(Level.SEVERE, "Error on trying to create/read chests file", var3);
      }
   }
   public List<ItemStack> getContent() {
      List<ItemStack> content = new ArrayList();
      List<String> items = this.config.getStringList("content");
      Iterator var3 = items.iterator();
      while(var3.hasNext()) {
         String itemText = (String)var3.next();
         try {
            ItemStack item = ItemParser.parseStringToItemStack(itemText);
            content.add(item);
         } catch (Exception var6) {
            this.plugin.getLogger().log(Level.SEVERE, "Not possible to get content of item", var6);
         }
      }
      return content;
   }
   public List<String> getRawLocations() {
      return this.config.getStringList("locations");
   }
   public List<Location> getLocations() {
      List<Location> locations = new ArrayList();
      List<String> encodedLocations = this.config.getStringList("locations");
      Iterator var3 = encodedLocations.iterator();
      while(var3.hasNext()) {
         String encodedLocation = (String)var3.next();
         try {
            locations.add(LocationUtils.decodeLocation(encodedLocation, false));
         } catch (Exception var6) {
            this.plugin.getLogger().log(Level.SEVERE, "Error on trying to decode location", var6);
         }
      }
      return locations;
   }
   public int addLocation(Location location) {
      List<String> encodedLocations = this.config.getStringList("locations");
      String encodedLocation = LocationUtils.encodeLocation(location, false);
      encodedLocations.add(encodedLocation);
      this.config.set("locations", encodedLocations);
      this.config.save();
      return encodedLocations.size();
   }
   public void removeLocation(int index) {
      List<String> encodedLocations = this.config.getStringList("locations");
      encodedLocations.remove(index);
      this.config.set("locations", encodedLocations);
      this.config.save();
   }
   public IFileConfiguration getConfig() {
      return this.config;
   }
   public static ChestsFile getInstance() {
      return instance;
   }
}
