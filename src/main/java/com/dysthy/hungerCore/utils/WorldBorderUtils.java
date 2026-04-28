package com.dysthy.hungerCore.utils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WorldBorderUtils {
    
    
    public static double getDistanceToWorldBorder(Player player) {
        return getDistanceToWorldBorder(player.getLocation());
    }
    
    
    public static double getDistanceToWorldBorder(Location location) {
        World world = location.getWorld();
        
        
        double centerX = world.getWorldBorder().getCenter().getX();
        double centerZ = world.getWorldBorder().getCenter().getZ();
        double size = world.getWorldBorder().getSize();
        
        
        double minX = centerX - (size / 2);
        double maxX = centerX + (size / 2);
        double minZ = centerZ - (size / 2);
        double maxZ = centerZ + (size / 2);
        
        
        double distanceToX = Math.min(
            Math.abs(location.getX() - minX),
            Math.abs(location.getX() - maxX)
        );
        
        double distanceToZ = Math.min(
            Math.abs(location.getZ() - minZ),
            Math.abs(location.getZ() - maxZ)
        );
        
        return Math.min(distanceToX, distanceToZ);
    }
    
    
    public static boolean isNearWorldBorder(Player player, double threshold) {
        return getDistanceToWorldBorder(player) <= threshold;
    }
    
    
    public static boolean isNearWorldBorder(Location location, double threshold) {
        return getDistanceToWorldBorder(location) <= threshold;
    }
    
    
    public static String getDirectionToBorder(Location location) {
        World world = location.getWorld();
        double centerX = world.getWorldBorder().getCenter().getX();
        double centerZ = world.getWorldBorder().getCenter().getZ();
        double size = world.getWorldBorder().getSize();
        
        double minX = centerX - (size / 2);
        double maxX = centerX + (size / 2);
        double minZ = centerZ - (size / 2);
        double maxZ = centerZ + (size / 2);
        
        double distanceToMinX = Math.abs(location.getX() - minX);
        double distanceToMaxX = Math.abs(location.getX() - maxX);
        double distanceToMinZ = Math.abs(location.getZ() - minZ);
        double distanceToMaxZ = Math.abs(location.getZ() - maxZ);
        
        double minDistance = Math.min(Math.min(distanceToMinX, distanceToMaxX), 
                                    Math.min(distanceToMinZ, distanceToMaxZ));
        
        if (minDistance == distanceToMinX) return "WEST";
        if (minDistance == distanceToMaxX) return "EAST";
        if (minDistance == distanceToMinZ) return "NORTH";
        return "SOUTH";
    }
    
    
    public static String getWorldBorderInfo(World world) {
        double centerX = world.getWorldBorder().getCenter().getX();
        double centerZ = world.getWorldBorder().getCenter().getZ();
        double size = world.getWorldBorder().getSize();
        
        return String.format("Centro: (%.1f, %.1f), Tamaño: %.1f", centerX, centerZ, size);
    }
}
