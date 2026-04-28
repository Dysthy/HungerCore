package com.dysthy.hungerCore.commands.subcommands;
import com.dysthy.hungerCore.commands.SubCommand;
import com.dysthy.hungerCore.events.TributeListener;
import com.dysthy.hungerCore.util.HcChat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.List;
public class ResetPlayerKillsCommand extends SubCommand {
   
   public String getName() {
      return "resetplayerkills";
   }
   public String getDescription() {
      return "Resetear las kills de un jugador específico";
   }
   public List<String> getTabCompleteSuggestions(Player player, String[] args) {
      if (args.length == 2) {
         return TributeListener.kills.keySet().stream().toList();
      }
      return List.of();
   }
   public void perform(Player player, String[] args) {
      if (!player.isOp()) {
         HcChat.error(player, "Sin permiso.");
         return;
      }
      if (args.length < 2) {
         HcChat.hint(player, "Uso: /hc resetplayerkills <jugador>");
         return;
      }
      String targetName = args[1];
      
      if (!TributeListener.kills.containsKey(targetName)) {
         HcChat.warn(player, targetName + " no tiene kills registradas.");
         return;
      }
      
      TributeListener.kills.remove(targetName);
      
      HcChat.success(player, "Kills de " + targetName + " en cero.");
   }
} 