package com.dysthy.hungerCore.commands.subcommands;
import com.dysthy.hungerCore.commands.SubCommand;
import com.dysthy.hungerCore.events.TributeBountySelectEvent;
import com.dysthy.hungerCore.util.HcChat;
import com.dysthy.hungerCore.events.TributeListener;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
public class BountyCommand extends SubCommand {
   public static String tributeWanted;
   public String getName() {
      return "bounty";
   }
   public String getDescription() {
      return "Bounty setup";
   }
   public List<String> getTabCompleteSuggestions(Player player, String[] args) {
      return List.of("select", "current");
   }
   public void perform(Player player, String[] args) {
      if (args.length == 1) {
         HcChat.hint(player, "Uso: /hc bounty select | current");
      } else {
         Map<String, Integer> kills = TributeListener.kills;
         String var10001;
         if (args[1].equalsIgnoreCase("select")) {
            if (kills.isEmpty()) {
               HcChat.warn(player, "No hay kills registradas todavía.");
               return;
            }
            tributeWanted = (String)((Entry)Collections.max(kills.entrySet(), Entry.comparingByValue())).getKey();
            if ((Integer)kills.get(tributeWanted) == 0) {
               HcChat.warn(player, "Nadie lleva kills aún.");
               return;
            }
            Bukkit.getPluginManager().callEvent(new TributeBountySelectEvent(tributeWanted, (Integer)kills.get(tributeWanted)));
            var10001 = tributeWanted;
            HcChat.accent(player, "Recompensa · " + var10001 + " · " + kills.get(tributeWanted) + " kills");
         } else if (args[1].equalsIgnoreCase("current")) {
            if (tributeWanted == null) {
               HcChat.info(player, "No hay bounty activo.");
               return;
            }
            var10001 = tributeWanted;
            HcChat.accent(player, "Bounty activo · " + var10001 + " · " + kills.get(tributeWanted) + " kills");
         }
      }
   }
}
