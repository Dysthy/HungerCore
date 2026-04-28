package com.dysthy.hungerCore.commands.subcommands;
import com.dysthy.hungerCore.commands.SubCommand;
import com.dysthy.hungerCore.events.TributeListener;
import com.dysthy.hungerCore.util.HcChat;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;
public class RemovePlayerCommand extends SubCommand {
    @Override
    public String getName() {
        return "removeplayer";
    }
    @Override
    public String getDescription() {
        return "Remueve un jugador que se desconectó del juego";
    }
    @Override
    public List<String> getTabCompleteSuggestions(Player player, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 2) {
            for (String tributeName : TributesCommand.tributes.keySet()) {
                Player onlinePlayer = Bukkit.getPlayer(tributeName);
                if (onlinePlayer == null || !onlinePlayer.isOnline()) {
                    suggestions.add(tributeName);
                }
            }
        }
        return suggestions;
    }
    @Override
    public void perform(Player sender, String[] args) {
        if (args.length < 2) {
            HcChat.hint(sender, "Uso: /hc removeplayer <jugador>");
            HcChat.info(sender, "Remueve un jugador que se desconectó del juego");
            return;
        }
        String playerName = args[1];
        
        if (!TributesCommand.tributes.containsKey(playerName)) {
            HcChat.error(sender, playerName + " no está registrado como tributo.");
            return;
        }
        Player targetPlayer = Bukkit.getPlayer(playerName);
        boolean wasOnline = targetPlayer != null && targetPlayer.isOnline();
        int tributeId = TributesCommand.tributes.remove(playerName);
        
        TributeListener.clearDeathLocation(playerName);
        
        TributeListener.kills.remove(playerName);
        if (wasOnline && targetPlayer != null) {
            targetPlayer.setGameMode(GameMode.SPECTATOR);
            HcChat.success(sender, playerName + " ha sido removido del juego y puesto en modo espectador.");
        } else {
            HcChat.success(sender, playerName + " ha sido removido del juego (estaba desconectado).");
        }
        TributeListener.updateAlivePlayers();
        HcChat.info(sender, "Tributo #" + tributeId + " eliminado. Jugadores vivos: " + TributeListener.getAlivePlayers());
    }
}
