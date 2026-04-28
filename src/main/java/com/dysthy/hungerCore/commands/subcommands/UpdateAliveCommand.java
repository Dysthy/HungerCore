package com.dysthy.hungerCore.commands.subcommands;
import com.dysthy.hungerCore.commands.SubCommand;
import com.dysthy.hungerCore.events.TributeListener;
import com.dysthy.hungerCore.util.HcChat;
import org.bukkit.entity.Player;
import java.util.List;
public class UpdateAliveCommand extends SubCommand {
   
   public String getName() {
      return "updatealive";
   }
   public String getDescription() {
      return "Actualizar el contador de jugadores vivos";
   }
   public List<String> getTabCompleteSuggestions(Player player, String[] args) {
      return List.of();
   }
   public void perform(Player player, String[] args) {
      if (!player.isOp()) {
         HcChat.error(player, "Sin permiso.");
         return;
      }
      
      TributeListener.updateAlivePlayers();
      int aliveCount = TributeListener.getAlivePlayers();
      
      HcChat.success(player, "Vivos actualizado · " + aliveCount + " jugadores.");
   }
} 