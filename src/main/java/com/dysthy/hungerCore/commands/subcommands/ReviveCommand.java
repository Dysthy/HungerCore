package com.dysthy.hungerCore.commands.subcommands;
import com.dysthy.hungerCore.HungerCore;
import com.dysthy.hungerCore.commands.SubCommand;
import com.dysthy.hungerCore.events.TributeListener;
import com.dysthy.hungerCore.util.HcChat;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import java.util.List;
public class ReviveCommand extends SubCommand {
   
   public String getName() {
      return "revive";
   }
   public String getDescription() {
      return "Revivir un jugador muerto";
   }
   public List<String> getTabCompleteSuggestions(Player player, String[] args) {
      if (args.length == 2) {
         return Bukkit.getOnlinePlayers().stream()
                 .filter(p -> p.getGameMode() == GameMode.SPECTATOR)
                 .map(Player::getName)
                 .toList();
      }
      return List.of();
   }
   public void perform(Player player, String[] args) {
      if (!player.isOp()) {
         HcChat.error(player, "Sin permiso.");
         return;
      }
      if (args.length < 2) {
         HcChat.hint(player, "Uso: /hc revive <jugador>");
         return;
      }
      String targetName = args[1];
      Player target = Bukkit.getPlayer(targetName);
      
      if (target == null) {
         HcChat.error(player, targetName + " no está conectado.");
         return;
      }
      if (target.getGameMode() != GameMode.SPECTATOR) {
         HcChat.error(player, targetName + " no está en espectador.");
         return;
      }
      
      target.setGameMode(GameMode.SURVIVAL);
      target.setHealth(target.getMaxHealth());
      target.setFoodLevel(20);
      
      
      TributeListener.clearDeathLocation(targetName);
      
      
      TributeListener.alivePlayers++;
      
      HcChat.success(player, "Revivido: " + targetName);
      HcChat.success(target, "Te ha revivido " + player.getName() + ".");
   }
} 