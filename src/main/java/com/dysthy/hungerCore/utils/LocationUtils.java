package com.dysthy.hungerCore.utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
public class LocationUtils {
   public static String encodeLocation(Location location, boolean playerLocation) {
      String world = location.getWorld().getName();
      double x = location.getX();
      double y = location.getY();
      double z = location.getZ();
      float yaw = location.getYaw();
      float pitch = location.getPitch();
      return playerLocation ? String.format("%s;%.2f;%.2f;%.2f;%f;%f", world, x, y, z, yaw, pitch) : String.format("%s;%.2f;%.2f;%.2f", world, x, y, z);
   }
   public static Location decodeLocation(String encoded, boolean playerLocation) {
      String[] split = encoded.split(";");
      String worldName = split[0];
      double x = Double.parseDouble(split[1]);
      double y = Double.parseDouble(split[2]);
      double z = Double.parseDouble(split[3]);
      World world = Bukkit.getWorld(worldName);
      if (playerLocation) {
         float yaw = Float.parseFloat(split[4]);
         float pitch = Float.parseFloat(split[5]);
         return new Location(world, x, y, z, yaw, pitch);
      } else {
         return new Location(world, x, y, z);
      }
   }
}
