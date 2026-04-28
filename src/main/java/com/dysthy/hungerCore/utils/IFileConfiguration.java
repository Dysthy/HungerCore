package com.dysthy.hungerCore.utils;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
public class IFileConfiguration extends YamlConfiguration {
   private final Plugin plugin;
   private final File configFile;
   public IFileConfiguration(String name, Plugin plugin) throws IOException, InvalidConfigurationException {
      this.plugin = plugin;
      this.configFile = new File(plugin.getDataFolder(), name + ".yml");
      this.loadConfig();
   }
   public void save() {
      try {
         this.save(this.configFile);
      } catch (IOException var2) {
         this.plugin.getLogger().log(Level.SEVERE, "There was an error while saving the " + this.configFile.getName() + " configuration!", var2);
      }
   }
   public void reload() {
      try {
         this.loadConfig();
      } catch (IOException var2) {
         this.plugin.getLogger().log(Level.SEVERE, "There was an error while creating the " + this.configFile.getName() + " configuration file!", var2);
      } catch (InvalidConfigurationException var3) {
         this.plugin.getLogger().log(Level.SEVERE, "The configuration \"" + this.configFile.getName() + "\" is invalid!", var3);
      }
   }
   private void loadConfig() throws IOException, InvalidConfigurationException {
      if (!this.plugin.getDataFolder().exists()) {
         this.plugin.getDataFolder().mkdir();
      }
      if (!this.configFile.exists()) {
         try {
            this.plugin.saveResource(this.configFile.getName(), false);
         } catch (IllegalArgumentException var2) {
            this.configFile.createNewFile();
         }
      }
      this.load(this.configFile);
   }
}
