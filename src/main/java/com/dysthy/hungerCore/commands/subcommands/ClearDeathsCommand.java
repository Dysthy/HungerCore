package com.dysthy.hungerCore.commands.subcommands;
import com.dysthy.hungerCore.commands.SubCommand;
import com.dysthy.hungerCore.events.TributeListener;
import com.dysthy.hungerCore.util.HcChat;
import org.bukkit.entity.Player;
import java.util.List;
public class ClearDeathsCommand extends SubCommand {
   
   public String getName() {
      return "cleardeaths";
   }
   public String getDescription() {
      return "Limpiar todas las ubicaciones de muerte";
   }
   public List<String> getTabCompleteSuggestions(Player player, String[] args) {
      return List.of();
   }
   public void perform(Player player, String[] args) {
      if (!player.isOp()) {
         HcChat.error(player, "Sin permiso.");
         return;
      }
      
      TributeListener.clearAllDeathLocations();
      
      HcChat.success(player, "Marcas de muerte borradas.");
   }
} 