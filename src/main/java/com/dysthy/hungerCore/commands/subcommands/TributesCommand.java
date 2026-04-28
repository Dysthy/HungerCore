package com.dysthy.hungerCore.commands.subcommands;
import com.dysthy.hungerCore.HungerCore;
import com.dysthy.hungerCore.commands.SubCommand;
import com.dysthy.hungerCore.config.SpawnsFile;
import com.dysthy.hungerCore.events.TributeListener;
import com.dysthy.hungerCore.util.HcChat;
import com.dysthy.hungerCore.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
public class TributesCommand extends SubCommand implements Listener {
    public static final Map<String, Integer> tributes = new HashMap();
    public String getName() {
        return "tributes";
    }
    public String getDescription() {
        return "Manage tributes locations and team teleports";
    }
    public List<String> getTabCompleteSuggestions(Player player, String[] args) {
        if (args.length == 2) {
            return List.of("map", "lobby", "reset");
        }
        return List.of();
    }
    public void perform(Player player, String[] args) {
        if (args.length == 1) {
            HcChat.warn(player, "Indica una acción. Usa /hc tributes map, lobby, info…");
            return;
        }
        String action = args[1].toLowerCase();
        
        
        if (action.equals("map") && args.length == 2) {
            handleOriginalMapTeleport(player);
        } else if (action.equals("lobby")) {
            handleOriginalLobbyTeleport(player);
        } else if (action.equals("reset")) {
            handleOriginalReset(player);
        } else {
            showHelp(player);
        }
    }
    
    private void handleOriginalMapTeleport(Player player) {
        List<Location> spawnpoints = SpawnsFile.getInstance().getLocations();
        if (spawnpoints.isEmpty()) {
            HcChat.error(player, "No hay spawnpoints en spawns.yml.");
            return;
        }
        int i = 0;
        Iterator var5 = Bukkit.getOnlinePlayers().iterator();
        while(var5.hasNext()) {
            Player onlinePlayer = (Player)var5.next();
            if (onlinePlayer != null && !onlinePlayer.isDead() && !onlinePlayer.isOp()) {
                try {
                    onlinePlayer.teleport((Location)spawnpoints.get(i));
                } catch (IndexOutOfBoundsException var11) {
                    HungerCore.getInstance().getLogger().log(Level.SEVERE, "No spawnpoint set for #" + (i + 1), var11);
                } finally {
                    ++i;
                }
            }
        }
    }
    private void handleOriginalLobbyTeleport(Player player) {
        String encodedLocation = HungerCore.getInstance().getConfig().getString("lobby.location");
        if (encodedLocation == null) {
            HcChat.error(player, "El lobby no está configurado (config.yml).");
            return;
        }
        Location location = LocationUtils.decodeLocation(encodedLocation, true);
        Iterator var5 = Bukkit.getOnlinePlayers().iterator();
        while(var5.hasNext()) {
            Player onlinePlayer = (Player)var5.next();
            if (onlinePlayer != null && !onlinePlayer.isDead()) {
                onlinePlayer.teleport(location);
            }
        }
        HcChat.success(player, "Todos los jugadores enviados al lobby.");
    }
    private void handleOriginalReset(Player player) {
        tributes.clear();
        int id = 1;
        Iterator var16 = Bukkit.getOnlinePlayers().iterator();
        while(var16.hasNext()) {
            Player resetPlayer = (Player)var16.next();
            if (resetPlayer != null && !resetPlayer.isOp()) {
                tributes.put(resetPlayer.getName(), id);
                ++id;
            }
        }
        
        TributeListener.updateAlivePlayers();
        HcChat.success(player, "Números de tributos reiniciados.");
    }
    private void showHelp(Player player) {
        HcChat.header(player, "Comando tributes");
        HcChat.hint(player, "map · arena en spawns · lobby · reset");
    }
}
