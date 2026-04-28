package com.dysthy.hungerCore.config;
import com.dysthy.hungerCore.HungerCore;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class TeleportConfig {
    private final HungerCore plugin;
    private File configFile;
    private FileConfiguration config;
    private final Random random = new Random();
    public TeleportConfig(HungerCore plugin) {
        this.plugin = plugin;
        loadConfig();
    }
    private void loadConfig() {
        configFile = new File(plugin.getDataFolder(), "teleports.yml");
        
        if (!configFile.exists()) {
            plugin.saveResource("teleports.yml", false);
        }
        
        config = YamlConfiguration.loadConfiguration(configFile);
        setupDefaults();
    }
    private void setupDefaults() {
        if (!config.contains("cornucopia")) {
            config.set("cornucopia.x", 0);
            config.set("cornucopia.y", 64);
            config.set("cornucopia.z", 0);
            config.set("cornucopia.world", "world");
            config.set("cornucopia.radius", 50.0);
        }
        if (!config.contains("lobby")) {
            config.set("lobby.x", 0);
            config.set("lobby.y", 64);
            config.set("lobby.z", 100);
            config.set("lobby.world", "world");
        }
        if (!config.contains("arena")) {
            config.set("arena.x", 0);
            config.set("arena.y", 64);
            config.set("arena.z", 0);
            config.set("arena.world", "world");
            config.set("arena.radius", 30.0);
        }
        if (!config.contains("random")) {
            config.set("random.min_x", -1000);
            config.set("random.max_x", 1000);
            config.set("random.min_z", -1000);
            config.set("random.max_z", 1000);
            config.set("random.world", "world");
        }
        saveConfig();
    }
    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Error al guardar teleports.yml: " + e.getMessage());
        }
    }
    public void reloadConfig() {
        loadConfig();
    }
    
    public Location getRandomLocation(World world) {
        int minX = config.getInt("random.min_x", -1000);
        int maxX = config.getInt("random.max_x", 1000);
        int minZ = config.getInt("random.min_z", -1000);
        int maxZ = config.getInt("random.max_z", 1000);
        int x = random.nextInt(maxX - minX + 1) + minX;
        int z = random.nextInt(maxZ - minZ + 1) + minZ;
        int y = world.getHighestBlockYAt(x, z) + 1;
        return new Location(world, x, y, z);
    }
    
    public Location getCornucopiaLocation(World world) {
        double x = config.getDouble("cornucopia.x", 0);
        double y = config.getDouble("cornucopia.y", 64);
        double z = config.getDouble("cornucopia.z", 0);
        
        return new Location(world, x, y, z);
    }
    
    public Location getLobbyLocation(World world) {
        double x = config.getDouble("lobby.x", 0);
        double y = config.getDouble("lobby.y", 64);
        double z = config.getDouble("lobby.z", 100);
        
        return new Location(world, x, y, z);
    }
    
    public Location getArenaCenter(World world) {
        double x = config.getDouble("arena.x", 0);
        double y = config.getDouble("arena.y", 64);
        double z = config.getDouble("arena.z", 0);
        
        return new Location(world, x, y, z);
    }
    
    public double getCornucopiaRadius() {
        return config.getDouble("cornucopia.radius", 50.0);
    }
    
    public double getArenaRadius() {
        return config.getDouble("arena.radius", 30.0);
    }
    
    public Location getCornucopiaTeamLocation(Location center, int teamIndex, int totalTeams) {
        double angle = (2 * Math.PI * teamIndex) / totalTeams;
        double radius = getCornucopiaRadius();
        double x = center.getX() + (radius * Math.cos(angle));
        double z = center.getZ() + (radius * Math.sin(angle));
        double y = center.getWorld().getHighestBlockYAt((int)x, (int)z) + 1;
        
        return new Location(center.getWorld(), x, y, z);
    }
    
    public Location getArenaTeamLocation(Location center, int teamIndex, int totalTeams) {
        double angle = (2 * Math.PI * teamIndex) / totalTeams;
        double radius = getArenaRadius();
        double x = center.getX() + (radius * Math.cos(angle));
        double z = center.getZ() + (radius * Math.sin(angle));
        double y = center.getWorld().getHighestBlockYAt((int)x, (int)z) + 1;
        
        return new Location(center.getWorld(), x, y, z);
    }
    
    public void setCornucopiaLocation(Location location) {
        config.set("cornucopia.x", location.getX());
        config.set("cornucopia.y", location.getY());
        config.set("cornucopia.z", location.getZ());
        config.set("cornucopia.world", location.getWorld().getName());
        saveConfig();
    }
    
    public void setLobbyLocation(Location location) {
        config.set("lobby.x", location.getX());
        config.set("lobby.y", location.getY());
        config.set("lobby.z", location.getZ());
        config.set("lobby.world", location.getWorld().getName());
        saveConfig();
    }
    
    public void setArenaCenter(Location location) {
        config.set("arena.x", location.getX());
        config.set("arena.y", location.getY());
        config.set("arena.z", location.getZ());
        config.set("arena.world", location.getWorld().getName());
        saveConfig();
    }
    
    public void setCornucopiaRadius(double radius) {
        config.set("cornucopia.radius", radius);
        saveConfig();
    }
    
    public void setArenaRadius(double radius) {
        config.set("arena.radius", radius);
        saveConfig();
    }
}
