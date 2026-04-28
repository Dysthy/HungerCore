package com.dysthy.hungerCore.storage;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SponsorManager {
    private static final SponsorManager instance = new SponsorManager();
    
    
    private final Map<UUID, UUID> sponsorVotes;
    
    
    private final Map<UUID, Integer> voteCounts;
    private SponsorManager() {
        this.sponsorVotes = new ConcurrentHashMap<>();
        this.voteCounts = new ConcurrentHashMap<>();
    }
    
    public boolean castVote(UUID voterUUID, UUID targetUUID) {
        
        UUID previousVote = sponsorVotes.get(voterUUID);
        if (previousVote != null) {
            voteCounts.compute(previousVote, (k, v) -> v != null && v > 0 ? v - 1 : 0);
            if (voteCounts.get(previousVote) != null && voteCounts.get(previousVote) <= 0) {
                voteCounts.remove(previousVote);
            }
        }
        
        
        sponsorVotes.put(voterUUID, targetUUID);
        voteCounts.merge(targetUUID, 1, Integer::sum);
        
        return true;
    }
    
    public UUID getWinner() {
        if (voteCounts.isEmpty()) {
            return null;
        }
        
        return voteCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
    
    public int getVotes(UUID playerUUID) {
        return voteCounts.getOrDefault(playerUUID, 0);
    }
    
    public Map<UUID, Integer> getAllVoteCounts() {
        return new HashMap<>(voteCounts);
    }
    
    public int getTotalVotes() {
        return sponsorVotes.size();
    }
    
    public boolean hasVoted(UUID voterUUID) {
        return sponsorVotes.containsKey(voterUUID);
    }
    
    public UUID getVote(UUID voterUUID) {
        return sponsorVotes.get(voterUUID);
    }
    
    public void clearAllVotes() {
        sponsorVotes.clear();
        voteCounts.clear();
    }
    
    public static SponsorManager getInstance() {
        return instance;
    }
}
