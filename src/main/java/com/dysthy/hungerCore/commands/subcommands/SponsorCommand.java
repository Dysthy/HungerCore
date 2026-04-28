package com.dysthy.hungerCore.commands.subcommands;
import com.dysthy.hungerCore.commands.SubCommand;
import com.dysthy.hungerCore.config.SponsorsConfig;
import com.dysthy.hungerCore.gui.SponsorMenu;
import com.dysthy.hungerCore.storage.SponsorManager;
import com.dysthy.hungerCore.util.HcChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
public class SponsorCommand extends SubCommand {
    private final Random random = new Random();
    @Override
    public String getName() {
        return "sponsor";
    }
    @Override
    public String getDescription() {
        return "Abrir menú de votación, recargar configuración, forzar votación o cerrar votaciones";
    }
    @Override
    public List<String> getTabCompleteSuggestions(Player player, String[] args) {
        if (args.length == 2) {
            return List.of("reload", "force", "close");
        }
        return List.of();
    }
    @Override
    public void perform(Player player, String[] args) {
        
        if (args.length > 1 && args[1].equalsIgnoreCase("reload")) {
            if (!player.isOp()) {
                HcChat.error(player, "Sin permiso para esta acción.");
                return;
            }
            SponsorsConfig.getInstance().reload();
            HcChat.success(player, "Patrocinadores recargados.");
            return;
        }
        
        if (args.length > 1 && args[1].equalsIgnoreCase("close")) {
            if (!player.isOp()) {
                HcChat.error(player, "Sin permiso para cerrar votación.");
                return;
            }
            executeVoting(player);
            return;
        }
        
        if (args.length > 1 && args[1].equalsIgnoreCase("force")) {
            if (!player.isOp()) {
                HcChat.error(player, "Sin permiso para forzar votación.");
                return;
            }
            
            boolean hasAlivePlayers = false;
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!onlinePlayer.isOp() && onlinePlayer.getGameMode() != GameMode.SPECTATOR) {
                    hasAlivePlayers = true;
                    break;
                }
            }
            if (!hasAlivePlayers) {
                HcChat.error(player, "No hay tributos vivos para votar.");
                return;
            }
            
            int count = 0;
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.getGameMode() == GameMode.SPECTATOR && !onlinePlayer.isOp()) {
                    org.bukkit.inventory.Inventory menu = SponsorMenu.createVotingMenu(onlinePlayer);
                    onlinePlayer.openInventory(menu);
                    HcChat.tell(
                        onlinePlayer,
                        Component.text("")
                            .append(Component.text("¡Votación abierta!", NamedTextColor.GOLD, TextDecoration.BOLD))
                            .append(Component.text(" Elige a quién patrocinar.", NamedTextColor.YELLOW))
                    );
                    count++;
                }
            }
            if (count > 0) {
                HcChat.success(player, "Menú enviado a " + count + " espectador(es).");
            } else {
                HcChat.error(player, "No hay espectadores online.");
            }
            return;
        }
        
        if (player.getGameMode() == GameMode.SPECTATOR || player.isOp()) {
            
            boolean hasAlivePlayers = false;
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!onlinePlayer.isOp() && onlinePlayer.getGameMode() != GameMode.SPECTATOR) {
                    hasAlivePlayers = true;
                    break;
                }
            }
            if (!hasAlivePlayers) {
                HcChat.error(player, "No hay tributos vivos para votar.");
                return;
            }
            
            org.bukkit.inventory.Inventory menu = SponsorMenu.createVotingMenu(player);
            player.openInventory(menu);
        } else {
            HcChat.error(player, "Solo muertos en combate u ops pueden votar desde aquí.");
        }
    }
    
    private void executeVoting(Player executor) {
        SponsorManager sponsorManager = SponsorManager.getInstance();
        UUID winnerUUID = sponsorManager.getWinner();
        
        if (winnerUUID == null) {
            HcChat.error(executor, "No hay votos registrados.");
            broadcastToSpectators(HcChat.prefixed(Component.text("No hubo suficientes votos.", NamedTextColor.GOLD)));
            return;
        }
        Player winner = Bukkit.getPlayer(winnerUUID);
        if (winner == null) {
            HcChat.error(executor, "El ganador ya no está conectado.");
            return;
        }
        
        SponsorsConfig config = SponsorsConfig.getInstance();
        var sponsorItems = config.getSponsorItems();
        
        if (sponsorItems.isEmpty()) {
            HcChat.error(executor, "No hay ítems de patrocinio en config.");
            broadcastToSpectators(HcChat.prefixed(Component.text("No hay ítems configurados.", NamedTextColor.RED)));
            return;
        }
        ItemStack randomItem = sponsorItems.get(random.nextInt(sponsorItems.size())).clone();
        
        
        HashMap<Integer, ItemStack> leftover = winner.getInventory().addItem(randomItem);
        
        String itemName = getItemDisplayName(randomItem);
        int votes = sponsorManager.getVotes(winnerUUID);
        
        
        if (!leftover.isEmpty()) {
            
            winner.getWorld().dropItemNaturally(winner.getLocation(), randomItem);
            HcChat.tellLegacy(winner, "§6¡Ganaste con §e" + votes + " §6votos! §f" + itemName + " §7(suelo, inventario lleno)");
        } else {
            HcChat.tellLegacy(winner, "§6¡Ganaste con §e" + votes + " §6votos! §f" + itemName);
        }
        
        Component announce = Component.empty()
            .append(Component.text(winner.getName(), NamedTextColor.YELLOW))
            .append(Component.text(" ganó con ", NamedTextColor.GOLD))
            .append(Component.text(String.valueOf(votes), NamedTextColor.YELLOW))
            .append(Component.text(" votos · ", NamedTextColor.GOLD))
            .append(Component.text(itemName, NamedTextColor.WHITE));
        broadcastToSpectators(HcChat.prefixed(announce));
        
        sponsorManager.clearAllVotes();
        HcChat.success(executor, "Votación cerrada y datos limpiados.");
    }
    
    private void broadcastToSpectators(Component message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.SPECTATOR) {
                player.sendMessage(message);
            }
        }
    }
    
    private String getItemDisplayName(ItemStack item) {
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            net.kyori.adventure.text.Component displayName = item.getItemMeta().displayName();
            if (displayName != null) {
                net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer serializer = 
                    net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection();
                return serializer.serialize(displayName);
            }
        }
        return item.getType().name().toLowerCase().replace('_', ' ');
    }
}
