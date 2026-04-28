package com.dysthy.hungerCore.events;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
public class TributeBountySelectEvent extends Event {
   private static final HandlerList handlers = new HandlerList();
   private final String tribute;
   private final int kills;
   public TributeBountySelectEvent(String tribute, int kills) {
      this.tribute = tribute;
      this.kills = kills;
   }
   public String getTribute() {
      return this.tribute;
   }
   public int getKills() {
      return this.kills;
   }
   public static HandlerList getHandlerList() {
      return handlers;
   }
   @NotNull
   public HandlerList getHandlers() {
      return handlers;
   }
}
