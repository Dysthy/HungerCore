package com.dysthy.hungerCore.utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import java.util.ArrayList;
import java.util.List;

public class VanillaTeamManager {
    private static final String TEAM_PREFIX = "hc_team_";
    private static final ChatColor[] TEAM_COLORS = {
        ChatColor.RED, ChatColor.BLUE, ChatColor.GREEN, ChatColor.YELLOW,
        ChatColor.AQUA, ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.GRAY,
        ChatColor.DARK_RED, ChatColor.DARK_BLUE, ChatColor.DARK_GREEN, ChatColor.DARK_AQUA,
        ChatColor.DARK_PURPLE, ChatColor.DARK_GRAY, ChatColor.BLACK, ChatColor.WHITE
    };
    
    private final Scoreboard scoreboard;
    private int teamCounter = 1;
    
    public VanillaTeamManager() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager != null) {
            this.scoreboard = manager.getMainScoreboard();
        } else {
            this.scoreboard = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
        }
    }
    
    
    public Team createTeam(String teamName, ChatColor color) {
        
        String teamId = TEAM_PREFIX + teamCounter++;
        
        
        Team existingTeam = scoreboard.getTeam(teamId);
        if (existingTeam != null) {
            existingTeam.unregister();
        }
        
        
        Team team = scoreboard.registerNewTeam(teamId);
        team.setDisplayName(teamName);
        team.setColor(color);
        team.setPrefix(color + "");
        
        return team;
    }
    
    
    public Team createTeamWithAutoColor(String teamName, int teamNumber) {
        ChatColor color = TEAM_COLORS[(teamNumber - 1) % TEAM_COLORS.length];
        return createTeam(teamName, color);
    }
    
    
    public boolean addPlayerToTeam(Player player, Team team) {
        if (team == null || player == null) {
            return false;
        }
        
        try {
            team.addEntry(player.getName());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    
    public boolean removePlayerFromTeam(Player player, Team team) {
        if (team == null || player == null) {
            return false;
        }
        
        try {
            team.removeEntry(player.getName());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    
    public Team getPlayerTeam(Player player) {
        if (player == null) {
            return null;
        }
        
        return scoreboard.getPlayerTeam(player);
    }
    
    
    public boolean hasTeam(Player player) {
        return getPlayerTeam(player) != null;
    }
    
    
    public List<Team> getAllTeams() {
        List<Team> teams = new ArrayList<>();
        for (Team team : scoreboard.getTeams()) {
            if (team.getName().startsWith(TEAM_PREFIX)) {
                teams.add(team);
            }
        }
        return teams;
    }
    
    
    public boolean deleteTeam(Team team) {
        if (team == null) {
            return false;
        }
        
        try {
            team.unregister();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    
    public void clearAllTeams() {
        for (Team team : getAllTeams()) {
            try {
                team.unregister();
            } catch (Exception e) {
                
            }
        }
        teamCounter = 1;
    }
    
    
    public List<Player> getTeamMembers(Team team) {
        List<Player> members = new ArrayList<>();
        if (team == null) {
            return members;
        }
        
        for (String entry : team.getEntries()) {
            Player player = Bukkit.getPlayer(entry);
            if (player != null && player.isOnline()) {
                members.add(player);
            }
        }
        
        return members;
    }
    
    
    public boolean hasTeamBypass(Player player) {
        if (player == null) return false;
        
        if (player.isOp()) {
            return true;
        }
        
        return player.hasPermission("hc.teams.bypass") || 
               player.hasPermission("hc.bypass") || 
               player.hasPermission("hungercore.teams.bypass") ||
               player.hasPermission("hungercore.bypass");
    }
    
    
    public List<Player> filterPlayersWithoutBypass(List<Player> players) {
        return players.stream()
                .filter(player -> !hasTeamBypass(player))
                .collect(java.util.stream.Collectors.toList());
    }
    
    
    public Scoreboard getScoreboard() {
        return scoreboard;
    }
}
