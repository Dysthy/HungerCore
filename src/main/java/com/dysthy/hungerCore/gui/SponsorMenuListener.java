package com.dysthy.hungerCore.gui;
import com.dysthy.hungerCore.HungerCore;
import com.dysthy.hungerCore.storage.SponsorManager;
import com.dysthy.hungerCore.util.HcChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.UUID;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
public class SponsorMenuListener implements Listener {
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacySection();
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        
        if (player.getGameMode() != org.bukkit.GameMode.SPECTATOR || player.isOp()) {
            return;
        }
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.NETHER_STAR) {
            return;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        Component displayName = meta.displayName();
        if (displayName == null) return;
        
        String displayNameStr = LEGACY_SERIALIZER.serialize(displayName);
        if (displayNameStr.contains("Patrocinador") || displayNameStr.contains("patrocinador") ||
            displayNameStr.contains("Votación") || displayNameStr.contains("votación")) {
            event.setCancelled(true);
            
            player.openInventory(SponsorMenu.createVotingMenu(player));
        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        
        if (player.getGameMode() != org.bukkit.GameMode.SPECTATOR && !player.isOp()) {
            return;
        }
        Component titleComponent = event.getView().title();
        String inventoryTitle = LEGACY_SERIALIZER.serialize(titleComponent);
        String titleLower = inventoryTitle.toLowerCase();
        
        
        if (!titleLower.contains("votación")) {
            return;
        }
        event.setCancelled(true);
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() != Material.PLAYER_HEAD) {
            return;
        }
        handleVotingClick(player, clickedItem);
    }
    
    private void handleVotingClick(Player voter, ItemStack clickedItem) {
        ItemMeta meta = clickedItem.getItemMeta();
        if (meta == null) return;
        
        NamespacedKey key = new NamespacedKey(HungerCore.getInstance(), "voting_target_uuid");
        if (!meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            return;
        }
        String targetUUIDStr = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        UUID targetUUID;
        try {
            targetUUID = UUID.fromString(targetUUIDStr);
        } catch (IllegalArgumentException e) {
            return;
        }
        Player targetPlayer = Bukkit.getPlayer(targetUUID);
        if (targetPlayer == null || targetPlayer.isOp() || 
            targetPlayer.getGameMode() == org.bukkit.GameMode.SPECTATOR) {
            HcChat.error(voter, "Ese jugador ya no está disponible para votar.");
            return;
        }
        
        SponsorManager sponsorManager = SponsorManager.getInstance();
        UUID previousVote = sponsorManager.getVote(voter.getUniqueId());
        boolean voteRegistered = sponsorManager.castVote(voter.getUniqueId(), targetUUID);
        
        if (voteRegistered) {
            int currentVotes = sponsorManager.getVotes(targetUUID);
            
            if (previousVote != null && !previousVote.equals(targetUUID)) {
                HcChat.tellLegacy(voter, "§aVoto cambiado a §e" + targetPlayer.getName() + "§a. §7Votos: §f" + currentVotes);
            } else {
                HcChat.tellLegacy(voter, "§aTu voto: §e" + targetPlayer.getName() + "§a. §7Votos: §f" + currentVotes);
            }
            
            
            voter.closeInventory();
        }
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        
    }
}
