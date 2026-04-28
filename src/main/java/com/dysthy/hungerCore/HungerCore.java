package com.dysthy.hungerCore;
import com.dysthy.hungerCore.commands.CommandManager;
import com.dysthy.hungerCore.config.ChestsFile;
import com.dysthy.hungerCore.config.MechanicsConfig;
import com.dysthy.hungerCore.config.SpawnsFile;
import com.dysthy.hungerCore.config.SponsorsConfig;
import com.dysthy.hungerCore.config.TeleportConfig;
import com.dysthy.hungerCore.events.TributeListener;
import com.dysthy.hungerCore.external.HungerCraftExpansion;
import com.dysthy.hungerCore.external.BarrierIntegration;
import com.dysthy.hungerCore.gui.SponsorMenuListener;
import com.dysthy.hungerCore.timer.DeathsTask;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
public final class HungerCore extends JavaPlugin {
   private static HungerCore instance;
   private BarrierIntegration barrierIntegration;
   private TeleportConfig teleportConfig;
   public void onEnable() {
      System.out.println("HungerCore is enabled");
      instance = this;
      
      
      this.barrierIntegration = new BarrierIntegration(this);
      
      
      this.teleportConfig = new TeleportConfig(this);
      
      ((PluginCommand)Objects.requireNonNull(this.getCommand("hc"))).setExecutor(new CommandManager(this));
      SpawnsFile.getInstance().init(this);
      ChestsFile.getInstance().init(this);
      SponsorsConfig.getInstance().init(this);
      MechanicsConfig.getInstance().init(this);
      if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
         (new HungerCraftExpansion()).register();
      }
      this.getServer().getPluginManager().registerEvents(new TributeListener(), this);
      this.getServer().getPluginManager().registerEvents(new SponsorMenuListener(), this);
      (new DeathsTask(5)).runTaskTimerAsynchronously(this, 0L, 20L);
      
      
      TributeListener.updateAlivePlayers();
   }
   public void onDisable() {
   }
   public static HungerCore getInstance() {
      return instance;
   }
   
   
   public BarrierIntegration getBarrierIntegration() {
      return barrierIntegration;
   }
   
   
   public TeleportConfig getTeleportConfig() {
      return teleportConfig;
   }
   
   public MechanicsConfig getMechanicsConfig() {
      return MechanicsConfig.getInstance();
   }
}
