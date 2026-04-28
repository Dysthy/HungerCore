package com.dysthy.hungerCore.gui;
import com.dysthy.hungerCore.HungerCore;
import com.dysthy.hungerCore.events.TributeListener;
import com.dysthy.hungerCore.storage.SponsorManager;
import com.dysthy.hungerCore.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
public class SponsorMenu {
    private static final String VOTING_MENU_TITLE = "§6§lVotación de Patrocinio";
    
    public static Inventory createVotingMenu(Player voter) {
        
        List<Player> alivePlayers = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.isOp() && player.getGameMode() != org.bukkit.GameMode.SPECTATOR) {
                alivePlayers.add(player);
            }
        }
        
        int size = Math.max(9, ((alivePlayers.size() + 8) / 9) * 9);
        if (size > 54) size = 54; 
        Inventory menu = Bukkit.createInventory(null, size, Component.text(VOTING_MENU_TITLE));
        
        int slot = 0;
        for (Player alivePlayer : alivePlayers) {
            if (slot >= 54) break; 
            ItemStack playerHead = createVotingPlayerHeadItem(alivePlayer, voter.getUniqueId());
            menu.setItem(slot, playerHead);
            slot++;
        }
        return menu;
    }
    
    private static ItemStack createVotingPlayerHeadItem(Player player, UUID voterUUID) {
        ItemBuilder builder = new ItemBuilder(Material.PLAYER_HEAD);
        builder.setSkullOwner(player.getName());
        
        String playerName = player.getName();
        int kills = TributeListener.kills.getOrDefault(playerName, 0);
        int currentVotes = SponsorManager.getInstance().getVotes(player.getUniqueId());
        
        String teamName = "Sin equipo";
        
        SponsorManager sponsorManager = SponsorManager.getInstance();
        boolean alreadyVotedForThis = sponsorManager.getVote(voterUUID) != null && 
                                     sponsorManager.getVote(voterUUID).equals(player.getUniqueId());
        
        String displayName = alreadyVotedForThis 
            ? "§e" + playerName + " §7(§aVotaste aquí§7)"
            : "§a" + playerName;
        builder.setName(displayName);
        
        List<String> lore = new ArrayList<>();
        lore.add("§7─────────────────");
        lore.add("§eKills: §f" + kills);
        lore.add("§eEquipo: §f" + teamName);
        lore.add("§eVotos actuales: §f" + currentVotes);
        lore.add("§7─────────────────");
        
        if (alreadyVotedForThis) {
            lore.add("§eHas votado por este jugador");
            lore.add("§7Clic para cambiar tu voto");
        } else {
            lore.add("§aClic para votar por este jugador");
        }
        builder.setLore(lore);
        ItemStack item = builder.build();
        
        
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            NamespacedKey key = new NamespacedKey(HungerCore.getInstance(), "voting_target_uuid");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, player.getUniqueId().toString());
            item.setItemMeta(meta);
        }
        
        return item;
    }
}
