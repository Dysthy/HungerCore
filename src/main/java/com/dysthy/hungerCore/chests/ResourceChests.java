package com.dysthy.hungerCore.chests;
import com.dysthy.hungerCore.config.ChestsFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.inventory.ItemStack;
public class ResourceChests {
   private final List<Location> locations;
   private final int CONTAINER_SIZE = 27;
   private final Random random = ThreadLocalRandom.current();
   public ResourceChests(List<Location> locations) {
      this.locations = locations;
   }
   public void clear() {
      this.locations.forEach((location) -> {
         Block block = location.getWorld().getBlockAt(location);
         BlockState blockState = block.getState();
         if (blockState instanceof Container) {
            Container container = (Container)blockState;
            container.getSnapshotInventory().clear();
            container.update();
         }
      });
   }
   public void fill() {
      List<ItemStack> availableItems = ChestsFile.getInstance().getContent();
      
      if (availableItems.isEmpty()) {
         return;
      }
      this.locations.forEach((location) -> {
         Block block = location.getWorld().getBlockAt(location);
         BlockState blockState = block.getState();
         if (blockState instanceof Container) {
            Container container = (Container)blockState;
            Map<Integer, ItemStack> items = generateRandomContent(availableItems, 5);
            
            if (!items.isEmpty()) {
               container.getSnapshotInventory().clear();
               
               for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
                  container.getSnapshotInventory().setItem(entry.getKey(), entry.getValue());
               }
               
               container.update();
            }
         }
      });
   }
   public void fillWithCustomQuantity(int quantity) {
      List<ItemStack> availableItems = ChestsFile.getInstance().getContent();
      
      if (availableItems.isEmpty()) {
         return;
      }
      this.locations.forEach((location) -> {
         Block block = location.getWorld().getBlockAt(location);
         BlockState blockState = block.getState();
         if (blockState instanceof Container) {
            Container container = (Container)blockState;
            Map<Integer, ItemStack> items = generateRandomContent(availableItems, quantity);
            
            if (!items.isEmpty()) {
               container.getSnapshotInventory().clear();
               
               for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
                  container.getSnapshotInventory().setItem(entry.getKey(), entry.getValue());
               }
               
               container.update();
            }
         }
      });
   }
   private Map<Integer, ItemStack> generateRandomContent(List<ItemStack> availableItems, int quantity) {
      Map<Integer, ItemStack> content = new HashMap<>();
      
      if (availableItems.isEmpty()) {
         return content;
      }
      int maxItems = Math.min(quantity, availableItems.size());
      int maxSlots = Math.min(quantity, CONTAINER_SIZE);
      
      
      List<Integer> availableSlots = new ArrayList<>();
      for (int i = 0; i < CONTAINER_SIZE; i++) {
         availableSlots.add(i);
      }
      
      
      for (int i = 0; i < maxItems; i++) {
         if (availableSlots.isEmpty()) break;
         
         
         ItemStack randomItem = availableItems.get(random.nextInt(availableItems.size()));
         
         
         int slotIndex = random.nextInt(availableSlots.size());
         int slot = availableSlots.remove(slotIndex);
         
         content.put(slot, randomItem.clone());
      }
      return content;
   }
   public int getChestCount() {
      return locations.size();
   }
   public List<Location> getLocations() {
      return new ArrayList<>(locations);
   }
   public void fillInRadius(Location center, double radius, int quantity) {
      List<ItemStack> availableItems = ChestsFile.getInstance().getContent();
      
      if (availableItems.isEmpty()) {
         return;
      }
      this.locations.stream()
         .filter(location -> location.getWorld().equals(center.getWorld()) && 
                           location.distance(center) <= radius)
         .forEach(location -> {
            Block block = location.getWorld().getBlockAt(location);
            BlockState blockState = block.getState();
            if (blockState instanceof Container) {
               Container container = (Container)blockState;
               Map<Integer, ItemStack> items = generateRandomContent(availableItems, quantity);
               
               if (!items.isEmpty()) {
                  container.getSnapshotInventory().clear();
                  
                  for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
                     container.getSnapshotInventory().setItem(entry.getKey(), entry.getValue());
                  }
                  
                  container.update();
               }
            }
         });
   }
   public int getChestCountInRadius(Location center, double radius) {
      return (int) this.locations.stream()
         .filter(location -> location.getWorld().equals(center.getWorld()) && 
                           location.distance(center) <= radius)
         .count();
   }
   public void fillUnconfiguredChestsInRadius(Location center, double radius, int quantity) {
      List<ItemStack> availableItems = ChestsFile.getInstance().getContent();
      
      if (availableItems.isEmpty()) {
         return;
      }
      
      List<Location> configuredLocations = new ArrayList<>(this.locations);
      
      
      for (int x = (int) (center.getX() - radius); x <= center.getX() + radius; x++) {
         for (int y = (int) (center.getY() - radius); y <= center.getY() + radius; y++) {
            for (int z = (int) (center.getZ() - radius); z <= center.getZ() + radius; z++) {
               Location location = new Location(center.getWorld(), x, y, z);
               
               
               if (location.distance(center) <= radius) {
                  
                  boolean isConfigured = configuredLocations.stream()
                     .anyMatch(configured -> configured.getWorld().equals(location.getWorld()) &&
                                           configured.getBlockX() == location.getBlockX() &&
                                           configured.getBlockY() == location.getBlockY() &&
                                           configured.getBlockZ() == location.getBlockZ());
                  
                  if (!isConfigured) {
                     Block block = location.getWorld().getBlockAt(location);
                     BlockState blockState = block.getState();
                     if (blockState instanceof Container) {
                        Container container = (Container) blockState;
                        Map<Integer, ItemStack> items = generateRandomContent(availableItems, quantity);
                        
                        if (!items.isEmpty()) {
                           container.getSnapshotInventory().clear();
                           
                           for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
                              container.getSnapshotInventory().setItem(entry.getKey(), entry.getValue());
                           }
                           
                           container.update();
                        }
                     }
                  }
               }
            }
         }
      }
   }
   public int getUnconfiguredChestCountInRadius(Location center, double radius) {
      List<Location> configuredLocations = new ArrayList<>(this.locations);
      int count = 0;
      
      for (int x = (int) (center.getX() - radius); x <= center.getX() + radius; x++) {
         for (int y = (int) (center.getY() - radius); y <= center.getY() + radius; y++) {
            for (int z = (int) (center.getZ() - radius); z <= center.getZ() + radius; z++) {
               Location location = new Location(center.getWorld(), x, y, z);
               
               if (location.distance(center) <= radius) {
                  boolean isConfigured = configuredLocations.stream()
                     .anyMatch(configured -> configured.getWorld().equals(location.getWorld()) &&
                                           configured.getBlockX() == location.getBlockX() &&
                                           configured.getBlockY() == location.getBlockY() &&
                                           configured.getBlockZ() == location.getBlockZ());
                  
                  if (!isConfigured) {
                     Block block = location.getWorld().getBlockAt(location);
                     BlockState blockState = block.getState();
                     if (blockState instanceof Container) {
                        count++;
                     }
                  }
               }
            }
         }
      }
      
      return count;
   }
}
