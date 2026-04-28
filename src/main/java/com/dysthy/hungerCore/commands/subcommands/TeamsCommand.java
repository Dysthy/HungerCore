package com.dysthy.hungerCore.commands.subcommands;

import com.dysthy.hungerCore.commands.SubCommand;
import com.dysthy.hungerCore.util.HcChat;
import com.dysthy.hungerCore.utils.VanillaTeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeamsCommand extends SubCommand {

    private final VanillaTeamManager teamManager = new VanillaTeamManager();

    @Override
    public String getName() {
        return "teams";
    }

    @Override
    public String getDescription() {
        return "Gestionar equipos de tributos";
    }

    @Override
    public List<String> getTabCompleteSuggestions(Player player, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 2) {
            suggestions.add("create");
            suggestions.add("list");
            suggestions.add("info");
            suggestions.add("delete");
            suggestions.add("add");
            suggestions.add("remove");
            suggestions.add("random");
            suggestions.add("teleport");
        } else if (args.length == 3) {
            if (args[1].equalsIgnoreCase("create") || args[1].equalsIgnoreCase("info") ||
                args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("remove") ||
                args[1].equalsIgnoreCase("add")) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    suggestions.add(onlinePlayer.getName());
                }
            } else if (args[1].equalsIgnoreCase("random")) {
                for (int i = 1; i <= 8; i++) {
                    suggestions.add(String.valueOf(i));
                }
            } else if (args[1].equalsIgnoreCase("teleport")) {
                for (Team team : teamManager.getAllTeams()) {
                    suggestions.add(team.getName());
                }
            }
        } else if (args.length == 4) {
            if (args[1].equalsIgnoreCase("create")) {
                for (int i = 1; i <= 12; i++) {
                    suggestions.add(String.valueOf(i));
                }
            } else if (args[1].equalsIgnoreCase("teleport")) {
                suggestions.add("~ ~ ~");
                suggestions.add("0 100 0");
                suggestions.add("100 100 100");
            }
        }
        return suggestions;
    }

    @Override
    public void perform(Player player, String[] args) {
        if (!player.hasPermission("hc.teams")) {
            HcChat.error(player, "No tienes permisos para usar este comando.");
            return;
        }
        if (args.length < 2) {
            HcChat.hint(player, "Uso: /hc teams <create|list|info|delete|add|remove|random|teleport> [jugador] [numero]");
            return;
        }
        String action = args[1].toLowerCase();
        switch (action) {
            case "create":
                handleCreate(player, args);
                break;
            case "list":
                handleList(player);
                break;
            case "info":
                handleInfo(player, args);
                break;
            case "delete":
                handleDelete(player, args);
                break;
            case "add":
                handleAdd(player, args);
                break;
            case "remove":
                handleRemove(player, args);
                break;
            case "random":
                handleRandom(player, args);
                break;
            case "teleport":
                handleTeleport(player, args);
                break;
            default:
                HcChat.error(player, "Accion desconocida. Opciones: create, list, info, delete, add, remove, random, teleport.");
                break;
        }
    }

    private void handleCreate(Player sender, String[] args) {
        if (args.length < 4) {
            HcChat.hint(sender, "Uso: /hc teams create <jugador> <numero>");
            return;
        }
        String playerName = args[2];
        String teamNumber = args[3];
        Player targetPlayer = Bukkit.getPlayer(playerName);
        if (targetPlayer == null) {
            HcChat.error(sender, "Jugador no encontrado: " + playerName);
            return;
        }
        int number;
        try {
            number = Integer.parseInt(teamNumber);
        } catch (NumberFormatException e) {
            HcChat.error(sender, "El numero de equipo debe ser valido (ej: 1, 2, 3)");
            return;
        }
        Team existingTeam = teamManager.getPlayerTeam(targetPlayer);
        if (existingTeam != null) {
            HcChat.error(sender, playerName + " ya pertenece a un equipo: " + existingTeam.getDisplayName());
            return;
        }
        String teamName = "Distrito " + number;
        Team team = teamManager.createTeamWithAutoColor(teamName, number);
        if (team == null) {
            HcChat.error(sender, "No se pudo crear el equipo " + teamName);
            return;
        }
        teamManager.addPlayerToTeam(targetPlayer, team);
        HcChat.success(sender, "Equipo creado para " + playerName + ": " + team.getDisplayName());
    }

    private void handleList(Player sender) {
        List<Team> teams = teamManager.getAllTeams();
        if (teams.isEmpty()) {
            HcChat.warn(sender, "No hay equipos creados.");
            return;
        }
        HcChat.header(sender, "Equipos creados");
        for (Team team : teams) {
            int memberCount = team.getEntries().size();
            HcChat.tellLegacy(sender, team.getColor() + team.getDisplayName() + " §7· §f" + memberCount + " miembros");
        }
    }

    private void handleInfo(Player sender, String[] args) {
        if (args.length < 3) {
            HcChat.hint(sender, "Uso: /hc teams info <jugador>");
            return;
        }
        String playerName = args[2];
        Player targetPlayer = Bukkit.getPlayer(playerName);
        if (targetPlayer == null) {
            HcChat.error(sender, "Jugador no encontrado: " + playerName);
            return;
        }
        Team team = teamManager.getPlayerTeam(targetPlayer);
        if (team == null) {
            HcChat.warn(sender, playerName + " no pertenece a ningun equipo.");
            return;
        }
        HcChat.header(sender, "Informacion del equipo");
        HcChat.tellLegacy(sender, "§eNombre: §f" + team.getDisplayName());
        HcChat.tellLegacy(sender, "§eColor: §f" + team.getColor());
        HcChat.tellLegacy(sender, "§eMiembros: §7" + team.getEntries().size());
        for (String entry : team.getEntries()) {
            HcChat.bullet(sender, entry);
        }
    }

    private void handleDelete(Player sender, String[] args) {
        if (args.length < 3) {
            HcChat.hint(sender, "Uso: /hc teams delete <jugador>");
            return;
        }
        String playerName = args[2];
        Player targetPlayer = Bukkit.getPlayer(playerName);
        if (targetPlayer == null) {
            HcChat.error(sender, "Jugador no encontrado: " + playerName);
            return;
        }
        Team team = teamManager.getPlayerTeam(targetPlayer);
        if (team == null) {
            HcChat.warn(sender, playerName + " no pertenece a ningun equipo.");
            return;
        }
        String teamName = team.getDisplayName();
        teamManager.deleteTeam(team);
        HcChat.success(sender, "Equipo eliminado: " + teamName);
    }

    private void handleAdd(Player sender, String[] args) {
        if (args.length < 4) {
            HcChat.hint(sender, "Uso: /hc teams add <jugador> <lider>");
            return;
        }
        String playerName = args[2];
        String leaderName = args[3];
        Player targetPlayer = Bukkit.getPlayer(playerName);
        Player leader = Bukkit.getPlayer(leaderName);
        if (targetPlayer == null) {
            HcChat.error(sender, "Jugador no encontrado: " + playerName);
            return;
        }
        if (leader == null) {
            HcChat.error(sender, "Lider no encontrado: " + leaderName);
            return;
        }
        Team team = teamManager.getPlayerTeam(leader);
        if (team == null) {
            HcChat.error(sender, leaderName + " no tiene equipo.");
            return;
        }
        boolean added = teamManager.addPlayerToTeam(targetPlayer, team);
        if (added) {
            HcChat.success(sender, playerName + " se unio al equipo " + team.getDisplayName());
        } else {
            HcChat.error(sender, "No se pudo añadir " + playerName + " al equipo");
        }
    }

    private void handleRemove(Player sender, String[] args) {
        if (args.length < 3) {
            HcChat.hint(sender, "Uso: /hc teams remove <jugador>");
            return;
        }
        String playerName = args[2];
        Player targetPlayer = Bukkit.getPlayer(playerName);
        if (targetPlayer == null) {
            HcChat.error(sender, "Jugador no encontrado: " + playerName);
            return;
        }
        Team team = teamManager.getPlayerTeam(targetPlayer);
        if (team == null) {
            HcChat.warn(sender, playerName + " no pertenece a ningun equipo.");
            return;
        }
        boolean removed = teamManager.removePlayerFromTeam(targetPlayer, team);
        if (removed) {
            HcChat.success(sender, playerName + " salio del equipo " + team.getDisplayName());
        } else {
            HcChat.error(sender, "No se pudo sacar del equipo a " + playerName);
        }
    }

    private void handleRandom(Player sender, String[] args) {
        if (args.length < 3) {
            HcChat.hint(sender, "Uso: /hc teams random <tamano>");
            return;
        }
        int teamSize;
        try {
            teamSize = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            HcChat.error(sender, "El tamano debe ser un numero valido.");
            return;
        }
        if (teamSize < 1) {
            HcChat.error(sender, "El tamano del equipo debe ser al menos 1.");
            return;
        }
        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        if (onlinePlayers.isEmpty()) {
            HcChat.error(sender, "No hay jugadores conectados para armar equipos.");
            return;
        }
        List<Player> playersWithoutBypass = teamManager.filterPlayersWithoutBypass(onlinePlayers);
        playersWithoutBypass.removeIf(player -> teamManager.hasTeam(player));
        if (playersWithoutBypass.isEmpty()) {
            HcChat.error(sender, "No hay jugadores validos disponibles.");
            return;
        }
        Collections.shuffle(playersWithoutBypass);
        int totalPlayers = playersWithoutBypass.size();
        int totalTeams = (int) Math.ceil((double) totalPlayers / teamSize);
        int teamCounter = 1;
        int bypassedPlayers = onlinePlayers.size() - playersWithoutBypass.size();
        HcChat.header(sender, "Creando equipos aleatorios");
        HcChat.tellLegacy(sender, "§eTamano por equipo §7· §f" + teamSize);
        HcChat.tellLegacy(sender, "§eEquipos previstos §7· §f" + totalTeams);
        HcChat.tellLegacy(sender, "§eJugadores disponibles §7· §f" + totalPlayers);
        if (bypassedPlayers > 0) {
            HcChat.info(sender, "Ignorados (bypass/op): " + bypassedPlayers);
        }
        for (int i = 0; i < totalTeams; i++) {
            if (playersWithoutBypass.isEmpty()) break;
            Player leader = playersWithoutBypass.remove(0);
            String teamName = "Distrito " + teamCounter;
            Team team = teamManager.createTeamWithAutoColor(teamName, teamCounter);
            if (team == null) {
                HcChat.error(sender, "No se pudo crear el equipo #" + teamCounter);
                continue;
            }
            teamManager.addPlayerToTeam(leader, team);
            ChatColor teamColor = team.getColor();
            String teamColorCode = "§" + Integer.toHexString(teamColor.ordinal());
            HcChat.tellLegacy(sender, "§a" + teamName + " §7· lider " + teamColorCode + leader.getName());
            for (int j = 1; j < teamSize && !playersWithoutBypass.isEmpty(); j++) {
                Player member = playersWithoutBypass.remove(0);
                boolean added = teamManager.addPlayerToTeam(member, team);
                if (added) {
                    HcChat.tellLegacy(sender, "§7    ⧫ " + teamColorCode + member.getName() + " §7→ " + teamName);
                } else {
                    HcChat.error(sender, "  No se pudo añadir " + member.getName() + " a " + teamName);
                }
            }
            teamCounter++;
        }
        if (!playersWithoutBypass.isEmpty()) {
            HcChat.warn(sender, "Sin cupo · " + playersWithoutBypass.size());
            for (Player player : playersWithoutBypass) {
                HcChat.bullet(sender, player.getName());
            }
        }
        HcChat.success(sender, "Equipos aleatorios listos.");
    }

    private void handleTeleport(Player sender, String[] args) {
        if (args.length < 5) {
            HcChat.hint(sender, "Uso: /hc teams teleport <equipo> <x> <y> <z> [mundo]");
            return;
        }
        String teamName = args[2];
        Team team = null;
        for (Team t : teamManager.getAllTeams()) {
            if (t.getName().equals(teamName) || t.getDisplayName().equals(teamName)) {
                team = t;
                break;
            }
        }
        if (team == null) {
            HcChat.error(sender, "No hay equipo con nombre: " + teamName);
            return;
        }
        try {
            double x = parseCoordinate(args[3], sender.getLocation().getX());
            double y = parseCoordinate(args[4], sender.getLocation().getY());
            double z = parseCoordinate(args[5], sender.getLocation().getZ());
            String worldName = args.length > 6 ? args[6] : null;
            org.bukkit.Location location;
            if (worldName != null) {
                org.bukkit.World world = Bukkit.getWorld(worldName);
                if (world == null) {
                    HcChat.error(sender, "Mundo no encontrado: " + worldName);
                    return;
                }
                location = new org.bukkit.Location(world, x, y, z);
            } else {
                location = new org.bukkit.Location(sender.getWorld(), x, y, z);
            }
            List<Player> members = teamManager.getTeamMembers(team);
            int teleportedCount = 0;
            HcChat.header(sender, "Teletransporte de equipo");
            HcChat.tellLegacy(sender, "§eEquipo §7· §f" + team.getDisplayName());
            HcChat.tellLegacy(sender, "§eDestino §7· §f" + location.getWorld().getName() + " " + String.format("%.1f, %.1f, %.1f", x, y, z));
            for (Player member : members) {
                if (member != null && member.isOnline()) {
                    member.teleport(location);
                    HcChat.success(member, "Teleport al punto del equipo.");
                    HcChat.tellLegacy(sender, "§a  ✓ §f" + member.getName());
                    teleportedCount++;
                } else {
                    HcChat.error(sender, "  ⊗ " + (member != null ? member.getName() : "offline"));
                }
            }
            HcChat.success(sender, "Teleport listo · " + teleportedCount + "/" + members.size());
        } catch (NumberFormatException e) {
            HcChat.error(sender, "Coordenadas invalidas. Usa numeros o ~ relativo.");
        }
    }

    private double parseCoordinate(String value, double current) {
        if (value.startsWith("~")) {
            String offset = value.substring(1);
            return current + (offset.isEmpty() ? 0 : Double.parseDouble(offset));
        }
        return Double.parseDouble(value);
    }
}
