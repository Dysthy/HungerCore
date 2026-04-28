package com.dysthy.hungerCore.commands.subcommands;
import com.dysthy.hungerCore.chests.ResourceChests;
import com.dysthy.hungerCore.commands.SubCommand;
import com.dysthy.hungerCore.config.ChestsFile;
import com.dysthy.hungerCore.util.HcChat;
import com.dysthy.hungerCore.utils.LocationUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerQuitEvent;
public class ChestsCommand extends SubCommand implements Listener {
   private final Map<String, ChestState> chestSetup = new HashMap();
   private final TextColor COLOR = TextColor.fromHexString("#69fc56");
   private final TextColor WARNING_COLOR = TextColor.fromHexString("#eefc56");
   public static final Material[] allowedBlocks;
   public String getName() {
      return "chests";
   }
   public String getDescription() {
      return "Manage chests for random items spawns";
   }
   public List<String> getTabCompleteSuggestions(Player player, String[] args) {
      if (args.length == 2) {
         return List.of("add", "remove", "fill", "fillcustom", "near", "wild", "reload", "exit");
      } else if (args.length == 3) {
         if (args[1].equalsIgnoreCase("fillcustom")) {
            return List.of("5", "10", "15", "20", "27");
         } else if (args[1].equalsIgnoreCase("near") || args[1].equalsIgnoreCase("wild")) {
            return List.of("10", "20", "50", "100", "200");
         }
      } else if (args.length == 4) {
         if (args[1].equalsIgnoreCase("near") || args[1].equalsIgnoreCase("wild")) {
            return List.of("5", "10", "15", "20", "27");
         }
      }
      return List.of();
   }
   public void perform(Player player, String[] args) {
      if (args.length == 1) {
         HcChat.tell(player, Component.text("Argumentos inválidos").color(this.COLOR));
         HcChat.tell(player, Component.text("- add | Añadir un nuevo contenedor").color(this.COLOR));
         HcChat.tell(player, Component.text("- remove | Remover un nuevo contenedor").color(this.COLOR));
         HcChat.tell(player, Component.text("- fill | Rellenar los contenedores (5 items)").color(this.COLOR));
         HcChat.tell(player, Component.text("- fillcustom <cantidad> | Rellenar con cantidad específica").color(this.COLOR));
         HcChat.tell(player, Component.text("- near <radio> <cantidad> | Rellenar cofres configurados cercanos").color(this.COLOR));
         HcChat.tell(player, Component.text("- wild <radio> <cantidad> | Rellenar cofres NO configurados cercanos").color(this.COLOR));
         HcChat.tell(player, Component.text("- reload | Recargar configuración").color(this.COLOR));
         HcChat.tell(player, Component.text("- exit | Salir del modo edición").color(this.COLOR));
      } else {
         if (args[1].equalsIgnoreCase("add")) {
            this.chestSetup.put(player.getName(), ChestState.ADD);
            HcChat.tell(player, Component.text("A continuación rompe los contenedores que quieras agregar.").color(this.COLOR));
            HcChat.tell(player, Component.text("AVISO: Para deshabilitar este modo usa /hc chests exit").color(this.WARNING_COLOR));
         } else if (args[1].equalsIgnoreCase("remove")) {
            this.chestSetup.put(player.getName(), ChestState.REMOVE);
            HcChat.tell(player, Component.text("A continuación rompe los contenedores que quieras remover.").color(this.COLOR));
            HcChat.tell(player, Component.text("AVISO: Para deshabilitar este modo usa /hc chests exit").color(this.WARNING_COLOR));
         } else if (args[1].equalsIgnoreCase("reload")) {
            ChestsFile.getInstance().getConfig().reload();
            HcChat.tell(player, Component.text("La configuración chests.yml se ha recargado correctamente.").color(this.COLOR));
         } else if (args[1].equalsIgnoreCase("fill")) {
            List<Location> locations = ChestsFile.getInstance().getLocations();
            if (locations.isEmpty()) {
               HcChat.tell(player, Component.text("AVISO: No se ha configurado ningún contenedor.").color(this.WARNING_COLOR));
               return;
            }
            ResourceChests resourceChests = new ResourceChests(locations);
            resourceChests.fill();
            HcChat.tell(player, Component.text("Todos los contenedores han sido rellenados. (" + locations.size() + ")").color(this.COLOR));
         } else if (args[1].equalsIgnoreCase("fillcustom")) {
            if (args.length < 3) {
               HcChat.tell(player, Component.text("Uso: /hc chests fillcustom <cantidad>").color(this.WARNING_COLOR));
               return;
            }
            
            try {
               int quantity = Integer.parseInt(args[2]);
               if (quantity < 1 || quantity > 27) {
                  HcChat.tell(player, Component.text("La cantidad debe estar entre 1 y 27.").color(this.WARNING_COLOR));
                  return;
               }
               
               List<Location> locations = ChestsFile.getInstance().getLocations();
               if (locations.isEmpty()) {
                  HcChat.tell(player, Component.text("AVISO: No se ha configurado ningún contenedor.").color(this.WARNING_COLOR));
                  return;
               }
               ResourceChests resourceChests = new ResourceChests(locations);
               resourceChests.fillWithCustomQuantity(quantity);
               HcChat.tell(player, Component.text("Todos los contenedores han sido rellenados con " + quantity + " items. (" + locations.size() + ")").color(this.COLOR));
            } catch (NumberFormatException e) {
               HcChat.tell(player, Component.text("La cantidad debe ser un número válido.").color(this.WARNING_COLOR));
            }
         } else if (args[1].equalsIgnoreCase("near")) {
            if (args.length < 4) {
               HcChat.tell(player, Component.text("Uso: /hc chests near <radio> <cantidad>").color(this.WARNING_COLOR));
               return;
            }
            
            try {
               double radius = Double.parseDouble(args[2]);
               int quantity = Integer.parseInt(args[3]);
               
               if (radius < 1 || radius > 1000) {
                  HcChat.tell(player, Component.text("El radio debe estar entre 1 y 1000 bloques.").color(this.WARNING_COLOR));
                  return;
               }
               
               if (quantity < 1 || quantity > 27) {
                  HcChat.tell(player, Component.text("La cantidad debe estar entre 1 y 27.").color(this.WARNING_COLOR));
                  return;
               }
               
               List<Location> locations = ChestsFile.getInstance().getLocations();
               if (locations.isEmpty()) {
                  HcChat.tell(player, Component.text("AVISO: No se ha configurado ningún contenedor.").color(this.WARNING_COLOR));
                  return;
               }
               ResourceChests resourceChests = new ResourceChests(locations);
               Location playerLocation = player.getLocation();
               
               int chestsInRadius = resourceChests.getChestCountInRadius(playerLocation, radius);
               if (chestsInRadius == 0) {
                  HcChat.tell(player, Component.text("No hay cofres configurados en un radio de " + radius + " bloques.").color(this.WARNING_COLOR));
                  return;
               }
               
               resourceChests.fillInRadius(playerLocation, radius, quantity);
               HcChat.tell(player, Component.text("Se han rellenado " + chestsInRadius + " cofres en un radio de " + radius + " bloques con " + quantity + " items.").color(this.COLOR));
                         } catch (NumberFormatException e) {
                HcChat.tell(player, Component.text("El radio y la cantidad deben ser números válidos.").color(this.WARNING_COLOR));
            }
         } else if (args[1].equalsIgnoreCase("wild")) {
            if (args.length < 4) {
               HcChat.tell(player, Component.text("Uso: /hc chests wild <radio> <cantidad>").color(this.WARNING_COLOR));
               return;
            }
            
            try {
               double radius = Double.parseDouble(args[2]);
               int quantity = Integer.parseInt(args[3]);
               
               if (radius < 1 || radius > 1000) {
                  HcChat.tell(player, Component.text("El radio debe estar entre 1 y 1000 bloques.").color(this.WARNING_COLOR));
                  return;
               }
               
               if (quantity < 1 || quantity > 27) {
                  HcChat.tell(player, Component.text("La cantidad debe estar entre 1 y 27.").color(this.WARNING_COLOR));
                  return;
               }
               
               List<Location> locations = ChestsFile.getInstance().getLocations();
               ResourceChests resourceChests = new ResourceChests(locations);
               Location playerLocation = player.getLocation();
               
               int chestsInRadius = resourceChests.getUnconfiguredChestCountInRadius(playerLocation, radius);
               if (chestsInRadius == 0) {
                  HcChat.tell(player, Component.text("No hay cofres no configurados en un radio de " + radius + " bloques.").color(this.WARNING_COLOR));
                  return;
               }
               
               resourceChests.fillUnconfiguredChestsInRadius(playerLocation, radius, quantity);
               HcChat.tell(player, Component.text("Se han rellenado " + chestsInRadius + " cofres no configurados en un radio de " + radius + " bloques con " + quantity + " items.").color(this.COLOR));
            } catch (NumberFormatException e) {
               HcChat.tell(player, Component.text("El radio y la cantidad deben ser números válidos.").color(this.WARNING_COLOR));
            }
         } else if (args[1].equalsIgnoreCase("exit")) {
            if (!this.chestSetup.containsKey(player.getName())) {
               HcChat.tell(player, Component.text("AVISO: No te encuentras en el modo edición.").color(this.WARNING_COLOR));
               return;
            }
            this.chestSetup.remove(player.getName());
            HcChat.tell(player, Component.text("Has abandonado el modo de edición.").color(this.COLOR));
         }
      }
   }
   @EventHandler
   public void onChestSetupInteract(BlockBreakEvent event) {
      Player player = event.getPlayer();
      Block block = event.getBlock();
      if (this.chestSetup.containsKey(player.getName())) {
         if (!Arrays.stream(allowedBlocks).noneMatch((material) -> {
            return block.getType() == material;
         })) {
            event.setCancelled(true);
            Location location = block.getLocation();
            ChestState chestState = (ChestState)this.chestSetup.get(player.getName());
            switch(chestState) {
            case ADD:
               int index = ChestsFile.getInstance().addLocation(location);
               HcChat.tell(player, Component.text("El contenedor #" + index + " se ha añadido correctamente.").color(this.COLOR));
               break;
            case REMOVE:
               List<String> rawLocations = ChestsFile.getInstance().getRawLocations();
               String encodedLocation = LocationUtils.encodeLocation(location, false);
               if (!rawLocations.contains(encodedLocation)) {
                  HcChat.tell(player, Component.text("AVISO: Este contenedor no se encuentra configurado.").color(this.WARNING_COLOR));
                  return;
               }
               int removeIndex = rawLocations.indexOf(encodedLocation);
               ChestsFile.getInstance().removeLocation(removeIndex);
               HcChat.tell(player, Component.text("El cofre se ha removido correctamente").color(this.COLOR));
            }
         }
      }
   }
   @EventHandler
   public void onChestSetupDisconnect(PlayerQuitEvent event) {
      Player player = event.getPlayer();
      if (this.chestSetup.containsKey(player.getName())) {
         this.chestSetup.remove(player.getName());
      }
   }
   static {
      allowedBlocks = new Material[]{Material.CHEST, Material.SHULKER_BOX, Material.BLACK_SHULKER_BOX, Material.BLUE_SHULKER_BOX, Material.BROWN_SHULKER_BOX, Material.CYAN_SHULKER_BOX, Material.GRAY_SHULKER_BOX, Material.LIGHT_BLUE_SHULKER_BOX, Material.LIGHT_GRAY_SHULKER_BOX, Material.GREEN_SHULKER_BOX, Material.LIME_SHULKER_BOX, Material.MAGENTA_SHULKER_BOX, Material.ORANGE_SHULKER_BOX, Material.PINK_SHULKER_BOX, Material.PURPLE_SHULKER_BOX, Material.RED_SHULKER_BOX, Material.WHITE_SHULKER_BOX, Material.YELLOW_SHULKER_BOX, Material.BARREL};
   }
}
