package com.dysthy.hungerCore.commands.subcommands;
import com.dysthy.hungerCore.HungerCore;
import com.dysthy.hungerCore.commands.SubCommand;
import com.dysthy.hungerCore.external.BarrierIntegration;
import com.dysthy.hungerCore.util.HcChat;
import org.bukkit.entity.Player;
import java.util.List;

public class BorderCommand extends SubCommand {
    
    @Override
    public String getName() {
        return "border";
    }
    
    @Override
    public String getDescription() {
        return "Obtiene información del borde del mundo y tormentas";
    }
    
    @Override
    public List<String> getTabCompleteSuggestions(Player player, String[] args) {
        return List.of("info", "distance", "storm");
    }
    
    @Override
    public void perform(Player player, String[] args) {
        if (args.length == 1) {
            
            showBorderInfo(player);
            return;
        }
        
        String subCommand = args[1].toLowerCase();
        
        switch (subCommand) {
            case "info":
                showBorderInfo(player);
                break;
            case "distance":
                showDistanceToBorder(player);
                break;
            case "storm":
                showStormInfo(player);
                break;
            default:
                HcChat.hint(player, "Uso: /hc border [info|distance|storm]");
                break;
        }
    }
    
    private void showBorderInfo(Player player) {
        BarrierIntegration barrierIntegration = HungerCore.getInstance().getBarrierIntegration();
        
        if (!barrierIntegration.isEnabled()) {
            HcChat.warn(player, "Barrier no disponible; datos nativos.");
        }
        
        String info = barrierIntegration.getWorldBorderAndStormInfo(player.getWorld());
        HcChat.header(player, "Borde del mundo");
        player.sendMessage(HcChat.prefixed(net.kyori.adventure.text.Component.text(info)));
    }
    
    private void showDistanceToBorder(Player player) {
        BarrierIntegration barrierIntegration = HungerCore.getInstance().getBarrierIntegration();
        
        double distance = barrierIntegration.getDistanceToWorldBorder(player);
        String direction = com.dysthy.hungerCore.utils.WorldBorderUtils.getDirectionToBorder(player.getLocation());
        
        HcChat.header(player, "Distancia al borde");
        HcChat.tellLegacy(player, "§eDistancia §7· §f" + String.format("%.1f", distance) + " bloques");
        HcChat.tellLegacy(player, "§eRumbo §7· §f" + direction);
        
        if (barrierIntegration.isNearWorldBorder(player, 50.0)) {
            HcChat.error(player, "¡Cuidado! Estás muy cerca del borde.");
        }
    }
    
    private void showStormInfo(Player player) {
        BarrierIntegration barrierIntegration = HungerCore.getInstance().getBarrierIntegration();
        
        HcChat.header(player, "Tormenta");
        
        if (!barrierIntegration.isEnabled()) {
            HcChat.error(player, "Barrier no disponible.");
            return;
        }
        
        if (barrierIntegration.hasActiveStorm(player.getWorld())) {
            double distanceToStorm = barrierIntegration.getDistanceToStormCenter(player);
            boolean inStorm = barrierIntegration.isPlayerInStorm(player);
            
            HcChat.tellLegacy(player, "§eAl centro §7· §f" + String.format("%.1f", distanceToStorm) + " bloques");
            HcChat.tellLegacy(player, "§eDentro §7· §f" + (inStorm ? "sí" : "no"));
            
            if (inStorm) {
                HcChat.error(player, "Estás dentro de la tormenta.");
            }
        } else {
            HcChat.success(player, "No hay tormenta activa aquí.");
        }
    }
}
