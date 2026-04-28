package com.dysthy.hungerCore.events;
import com.dysthy.hungerCore.HungerCore;
import com.dysthy.hungerCore.commands.subcommands.BountyCommand;
import com.dysthy.hungerCore.commands.subcommands.ChestsCommand;
import com.dysthy.hungerCore.config.ChestsFile;
import com.dysthy.hungerCore.config.MechanicsConfig;
import com.dysthy.hungerCore.utils.LocationUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.dysthy.hungerCore.util.HcChat;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
public class TributeListener implements Listener {
   public static Map<String, Integer> kills = new HashMap();
   public static List<String> latestDeaths = new ArrayList();
   private static Map<String, Location> deathLocations = new HashMap();
   public static int alivePlayers = 0;
   @EventHandler
   public void onTributeDeath(PlayerDeathEvent event) {
      Player player = event.getPlayer();
      if (!player.isOp()) {
         
         deathLocations.put(player.getName(), player.getLocation());
         
         latestDeaths.add(player.getName());
         kills.remove(player.getName());
         
         
         alivePlayers = Math.max(0, alivePlayers - 1);
         
         if (event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            kills.put(killer.getName(), (Integer)kills.getOrDefault(killer.getName(), 0) + 1);
            MechanicsConfig.getInstance().onTributeKill(killer, player);
            if (BountyCommand.tributeWanted != null) {
               if (player.getName().equalsIgnoreCase(BountyCommand.tributeWanted)) {
                  BountyCommand.tributeWanted = null;
               }
            }
         }
      }
   }
   @EventHandler
   public void onTributeRespawn(PlayerRespawnEvent event) {
      Player player = event.getPlayer();
      if (!player.isOp()) {
         
         Location deathLocation = deathLocations.get(player.getName());
         if (deathLocation != null) {
         
         Bukkit.getScheduler().runTaskLater(HungerCore.getInstance(), () -> {
            player.teleport(deathLocation);
            player.setGameMode(GameMode.SPECTATOR);
         }, 1L);
         }
      }
   }
   @EventHandler
   public void onTributeBountySelect(TributeBountySelectEvent event) {
      Iterator var2 = Bukkit.getOnlinePlayers().iterator();
      while(var2.hasNext()) {
         Player player = (Player)var2.next();
         if (player != null && player.isOp()) {
            HcChat.accent(player, "Nueva recompensa · " + event.getTribute());
         }
      }
   }
   @EventHandler
   public void onTributeBreakResourceChest(BlockBreakEvent event) {
      Player player = event.getPlayer();
      Block block = event.getBlock();
      if (!player.isOp()) {
         if (!Arrays.stream(ChestsCommand.allowedBlocks).noneMatch((material) -> {
            return block.getType() == material;
         })) {
            Location location = event.getBlock().getLocation();
            List<String> rawLocations = ChestsFile.getInstance().getRawLocations();
            String encodedLocation = LocationUtils.encodeLocation(location, false);
            if (rawLocations.contains(encodedLocation)) {
               event.setCancelled(true);
            }
         }
      }
   }
   
   public static void clearDeathLocation(String playerName) {
      deathLocations.remove(playerName);
   }
   
   public static Location getDeathLocation(String playerName) {
      return deathLocations.get(playerName);
   }
   
   public static void clearAllDeathLocations() {
      deathLocations.clear();
   }
   
   public static void updateAlivePlayers() {
      alivePlayers = (int) Bukkit.getOnlinePlayers().stream()
              .filter(p -> !p.isOp() && p.getGameMode() != GameMode.SPECTATOR)
              .count();
   }
   
   public static int getAlivePlayers() {
      return alivePlayers;
   }
}
