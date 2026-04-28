package com.dysthy.hungerCore.commands.subcommands;
import com.dysthy.hungerCore.commands.SubCommand;
import com.dysthy.hungerCore.config.SpawnsFile;
import com.dysthy.hungerCore.util.HcChat;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
public class SpawnpointCommand extends SubCommand {
   public String getName() {
      return "spawnpoints";
   }
   public String getDescription() {
      return "Manage spawnpoints for players";
   }
   public List<String> getTabCompleteSuggestions(Player player, String[] args) {
      List<String> suggestions = new ArrayList();
      if (args.length == 2) {
         suggestions = List.of("add", "reload", "remove", "tp");
      } else if (args.length == 3) {
         if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("reload")) {
            return (List)suggestions;
         }
         int totalSpawns = SpawnsFile.getInstance().getLocations().size();
         for(int i = 1; i <= totalSpawns; ++i) {
            ((List)suggestions).add(String.valueOf(i));
         }
      }
      return (List)suggestions;
   }
   public void perform(Player player, String[] args) {
      if (args.length == 1) {
         HcChat.hint(player, "Uso: add | remove | tp | reload");
      } else {
         if (args[1].equalsIgnoreCase("add")) {
            Location location = player.getLocation();
            int latestIndex = SpawnsFile.getInstance().addLocation(location);
            HcChat.success(player, "Spawn #" + latestIndex + " añadido.");
         } else {
            int index;
            if (args[1].equalsIgnoreCase("remove")) {
               try {
                  index = Integer.parseInt(args[2]) - 1;
                  if (index < 0 || index >= SpawnsFile.getInstance().getLocations().size()) {
                     HcChat.error(player, "Índice inválido.");
                     return;
                  }
                  SpawnsFile.getInstance().removeLocation(index);
                  HcChat.success(player, "Spawn #" + (index + 1) + " eliminado.");
               } catch (Exception var5) {
                  HcChat.error(player, "Usa solo números.");
               }
            } else if (args[1].equalsIgnoreCase("tp")) {
               try {
                  index = Integer.parseInt(args[2]) - 1;
                  if (index < 0 || index >= SpawnsFile.getInstance().getLocations().size()) {
                     HcChat.error(player, "Índice inválido.");
                     return;
                  }
                  Location location = SpawnsFile.getInstance().getLocation(index);
                  player.teleport(location);
               } catch (Exception var6) {
                  HcChat.error(player, "Usa solo números.");
               }
            } else if (args[1].equalsIgnoreCase("reload")) {
               SpawnsFile.getInstance().getConfig().reload();
               HcChat.success(player, "Spawns recargados.");
            }
         }
      }
   }
}
