package com.dysthy.hungerCore.commands.subcommands;
import com.dysthy.hungerCore.HungerCore;
import com.dysthy.hungerCore.commands.SubCommand;
import com.dysthy.hungerCore.util.HcChat;
import com.dysthy.hungerCore.utils.LocationUtils;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
public class LobbyCommand extends SubCommand {
   public String getName() {
      return "lobby";
   }
   public String getDescription() {
      return "Setup lobby location";
   }
   public List<String> getTabCompleteSuggestions(Player player, String[] args) {
      return List.of("reload");
   }
   public void perform(Player player, String[] args) {
      if (args.length == 1) {
         Location location = player.getLocation();
         String encodedLocation = LocationUtils.encodeLocation(location, true);
         HungerCore.getInstance().getConfig().set("lobby.location", encodedLocation);
         HungerCore.getInstance().saveConfig();
         HcChat.success(player, "Lobby guardado en config.");
      } else {
         if (args[1].equalsIgnoreCase("reload")) {
            HungerCore.getInstance().reloadConfig();
            HcChat.success(player, "Config recargada.");
         }
      }
   }
}
