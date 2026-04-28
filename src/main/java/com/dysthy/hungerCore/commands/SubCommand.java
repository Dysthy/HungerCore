package com.dysthy.hungerCore.commands;
import java.util.List;
import org.bukkit.entity.Player;
public abstract class SubCommand {
   public abstract String getName();
   public abstract String getDescription();
   public abstract List<String> getTabCompleteSuggestions(Player var1, String[] var2);
   public abstract void perform(Player var1, String[] var2);
}
