package com.dysthy.hungerCore.commands.subcommands;
import com.dysthy.hungerCore.commands.SubCommand;
import com.dysthy.hungerCore.events.TributeListener;
import com.dysthy.hungerCore.util.HcChat;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import java.util.List;
public class ReviveAllCommand extends SubCommand {
   
   public String getName() {
      return "reviveall";
   }
   public String getDescription() {
      return "Revivir a todos los jugadores muertos";
   }
   public List<String> getTabCompleteSuggestions(Player player, String[] args) {
      return List.of();
   }
   public void perform(Player player, String[] args) {
      if (!player.isOp()) {
         HcChat.error(player, "Sin permiso.");
         return;
      }
      int revivedCount = 0;
      
      
      for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
         if (onlinePlayer.getGameMode() == GameMode.SPECTATOR && !onlinePlayer.isOp()) {
            
            onlinePlayer.setGameMode(GameMode.SURVIVAL);
            onlinePlayer.setHealth(onlinePlayer.getMaxHealth());
            onlinePlayer.setFoodLevel(20);
            
            
            TributeListener.clearDeathLocation(onlinePlayer.getName());
            
            
            TributeListener.alivePlayers++;
            
            
            HcChat.success(onlinePlayer, "Te ha revivido " + player.getName() + ".");
            
            revivedCount++;
         }
      }
      
      if (revivedCount > 0) {
         HcChat.success(player, "Revividos: " + revivedCount + " jugadores.");
      } else {
         HcChat.warn(player, "Nadie en espectador para revivir.");
      }
   }
} 