package com.dysthy.hungerCore.storage;
public class Tribute {
   private final String name;
   private int kills;
   private String deathCause;
   private long timeSurvived;
   public Tribute(String name) {
      this.name = name;
      this.kills = 0;
      this.deathCause = "";
      this.timeSurvived = 0L;
   }
   public String getName() {
      return this.name;
   }
   public int getKills() {
      return this.kills;
   }
   public String getDeathCause() {
      return this.deathCause;
   }
   public long getTimeSurvived() {
      return this.timeSurvived;
   }
   public void addKill() {
      ++this.kills;
   }
   public void setKills(int kills) {
      this.kills = kills;
   }
   public void setDeathCause(String deathCause) {
      this.deathCause = deathCause;
   }
   public void setTimeSurvived(long timeSurvived) {
      this.timeSurvived = timeSurvived;
   }
}
