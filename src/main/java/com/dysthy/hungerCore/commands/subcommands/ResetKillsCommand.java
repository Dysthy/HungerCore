package com.dysthy.hungerCore.commands.subcommands;
import com.dysthy.hungerCore.commands.SubCommand;
import com.dysthy.hungerCore.events.TributeListener;
import com.dysthy.hungerCore.util.HcChat;
import org.bukkit.entity.Player;
import java.util.List;
public class ResetKillsCommand extends SubCommand {
   
   public String getName() {
      return "resetkills";
   }
   public String getDescription() {
      return "Resetear las kills de todos los jugadores";
   }
   public List<String> getTabCompleteSuggestions(Player player, String[] args) {
      return List.of();
   }
   public void perform(Player player, String[] args) {
      if (!player.isOp()) {
         HcChat.error(player, "Sin permiso.");
         return;
      }
      
      TributeListener.kills.clear();
      
      HcChat.success(player, "Kills globales en cero.");
   }
} 