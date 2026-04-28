package com.dysthy.hungerCore.config;
import com.dysthy.hungerCore.utils.IFileConfiguration;
import com.dysthy.hungerCore.utils.LocationUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
public class SpawnsFile {
   private static final SpawnsFile instance = new SpawnsFile();
   private IFileConfiguration config;
   private Plugin plugin;
   public void init(Plugin plugin) {
      try {
         this.plugin = plugin;
         this.config = new IFileConfiguration("spawnpoints", plugin);
         if (this.config.getList("locations") == null) {
            this.config.set("locations", List.of());
         }
      } catch (Exception var3) {
         plugin.getLogger().log(Level.SEVERE, "Error on trying to create/read spawns file", var3);
      }
   }
   public IFileConfiguration getConfig() {
      return this.config;
   }
   public List<Location> getLocations() {
      List<Location> locations = new ArrayList();
      List<String> encodedLocations = this.config.getStringList("locations");
      Iterator var3 = encodedLocations.iterator();
      while(var3.hasNext()) {
         String encodedLocation = (String)var3.next();
         try {
            locations.add(LocationUtils.decodeLocation(encodedLocation, true));
         } catch (Exception var6) {
            this.plugin.getLogger().log(Level.SEVERE, "Error on trying to decode location", var6);
         }
      }
      return locations;
   }
   public int addLocation(Location location) {
      List<String> encodedLocations = this.config.getStringList("locations");
      String encodedLocation = LocationUtils.encodeLocation(location, true);
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
   public Location getLocation(int index) {
      List<String> encodedLocations = this.config.getStringList("locations");
      String encodedLocation = (String)encodedLocations.get(index);
      return LocationUtils.decodeLocation(encodedLocation, true);
   }
   public static SpawnsFile getInstance() {
      return instance;
   }
}
