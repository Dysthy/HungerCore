package com.dysthy.hungerCore.external;
import com.dysthy.hungerCore.commands.subcommands.BountyCommand;
import com.dysthy.hungerCore.commands.subcommands.TimerCommand;
import com.dysthy.hungerCore.commands.subcommands.TributesCommand;
import com.dysthy.hungerCore.events.TributeListener;
import com.dysthy.hungerCore.HungerCore;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
public class HungerCraftExpansion extends PlaceholderExpansion {
   private LocalDateTime latestUpdate;
   @NotNull
   public String getIdentifier() {
      return "hc";
   }
   @NotNull
   public String getAuthor() {
      return "Neerixx";
   }
   @NotNull
   public String getVersion() {
      return "1.0-SNAPSHOT";
   }
   public boolean persist() {
      return true;
   }
   public String onRequest(OfflinePlayer player, @NotNull String identifier) {
      Map<String, String> timers = TimerCommand.timerData;
      Iterator var4 = timers.entrySet().iterator();
      Entry entry;
      do {
         if (!var4.hasNext()) {
            int kills;
            if (identifier.equalsIgnoreCase("tribute")) {
               if (player.isOp()) {
                  return "STAFF";
               }
               kills = (Integer)TributesCommand.tributes.getOrDefault(player.getName(), 1);
               return String.format("Tributo #%o", kills);
            }
            if (identifier.equalsIgnoreCase("tributeid")) {
               if (player.isOp()) {
                  return "STAFF";
               }
               return String.valueOf(TributesCommand.tributes.getOrDefault(player.getName(), 1));
            }
            if (identifier.equalsIgnoreCase("kills")) {
               kills = (Integer)TributeListener.kills.getOrDefault(player.getName(), 0);
               return String.valueOf(kills);
            }
            if (identifier.equalsIgnoreCase("latest_death")) {
               if (TributeListener.latestDeaths.isEmpty()) {
                  return "";
               }
               return (String)TributeListener.latestDeaths.getFirst();
            }
            if (identifier.equalsIgnoreCase("bounty")) {
               return (String)Objects.requireNonNullElse(BountyCommand.tributeWanted, "");
            }
            if (identifier.equalsIgnoreCase("alive")) {
               return String.valueOf(TributeListener.getAlivePlayers());
            }
            if (identifier.equalsIgnoreCase("border_distance")) {
               if (player.isOnline()) {
                  try {
                     double distance = HungerCore.getInstance().getBarrierIntegration().getDistanceToWorldBorder(player.getPlayer());
                     return String.format("%.0f", distance);
                  } catch (Exception e) {
                     return "0";
                  }
               }
               return "0";
            }
            if (identifier.equalsIgnoreCase("custom_border_distance")) {
               if (player.isOnline()) {
                  try {
                     double distance = HungerCore.getInstance().getBarrierIntegration().getDistanceToCustomBorder(player.getPlayer());
                     return distance >= 0 ? String.format("%.0f", distance) : "N/A";
                  } catch (Exception e) {
                     return "N/A";
                  }
               }
               return "N/A";
            }
             if (identifier.equalsIgnoreCase("border_direction")) {
               if (player.isOnline()) {
                  try {
                     return com.dysthy.hungerCore.utils.WorldBorderUtils.getDirectionToBorder(player.getPlayer().getLocation());
                  } catch (Exception e) {
                     return "UNKNOWN";
                  }
               }
               return "UNKNOWN";
            }
            if (identifier.equalsIgnoreCase("border_near")) {
               if (player.isOnline()) {
                  try {
                     boolean isNear = HungerCore.getInstance().getBarrierIntegration().isNearWorldBorder(player.getPlayer(), 50.0);
                     return isNear ? "true" : "false";
                  } catch (Exception e) {
                     return "false";
                  }
               }
               return "false";
            }
            return "--:--";
         }
         entry = (Entry)var4.next();
      } while(!identifier.equalsIgnoreCase((String)entry.getKey()));
      return (String)entry.getValue();
   }
}
