package com.dysthy.hungerCore.commands;
import com.dysthy.hungerCore.commands.subcommands.BountyCommand;
import com.dysthy.hungerCore.commands.subcommands.ChestsCommand;
import com.dysthy.hungerCore.commands.subcommands.ClearDeathsCommand;
import com.dysthy.hungerCore.commands.subcommands.LobbyCommand;
import com.dysthy.hungerCore.commands.subcommands.RemovePlayerCommand;
import com.dysthy.hungerCore.commands.subcommands.ReviveCommand;
import com.dysthy.hungerCore.commands.subcommands.ReviveAllCommand;
import com.dysthy.hungerCore.commands.subcommands.ResetKillsCommand;
import com.dysthy.hungerCore.commands.subcommands.ResetPlayerKillsCommand;
import com.dysthy.hungerCore.commands.subcommands.SpawnpointCommand;
import com.dysthy.hungerCore.commands.subcommands.TeamsCommand;
import com.dysthy.hungerCore.commands.subcommands.UpdateAliveCommand;
import com.dysthy.hungerCore.commands.subcommands.TimerCommand;
import com.dysthy.hungerCore.commands.subcommands.TributesCommand;
import com.dysthy.hungerCore.commands.subcommands.BorderCommand;
import com.dysthy.hungerCore.commands.subcommands.SponsorCommand;
import com.dysthy.hungerCore.util.HcChat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
public class CommandManager implements CommandExecutor, TabExecutor, TabCompleter {
   private final Map<String, SubCommand> subCommands = new HashMap();
   public CommandManager(Plugin plugin) {
      this.subCommands.put("spawnpoints", new SpawnpointCommand());
      this.subCommands.put("chests", new ChestsCommand());
      this.subCommands.put("tributes", new TributesCommand());
      this.subCommands.put("lobby", new LobbyCommand());
      this.subCommands.put("timer", new TimerCommand());
      this.subCommands.put("bounty", new BountyCommand());
      this.subCommands.put("revive", new ReviveCommand());
      this.subCommands.put("reviveall", new ReviveAllCommand());
      this.subCommands.put("resetkills", new ResetKillsCommand());
      this.subCommands.put("resetplayerkills", new ResetPlayerKillsCommand());
      this.subCommands.put("cleardeaths", new ClearDeathsCommand());
       this.subCommands.put("updatealive", new UpdateAliveCommand());
       this.subCommands.put("border", new BorderCommand());
       this.subCommands.put("removeplayer", new RemovePlayerCommand());
       this.subCommands.put("teams", new TeamsCommand());
       this.subCommands.put("sponsor", new SponsorCommand());
      Iterator var2 = this.subCommands.values().iterator();
      while(var2.hasNext()) {
         SubCommand subCommand = (SubCommand)var2.next();
         List<Class<?>> interfaces = Arrays.asList(subCommand.getClass().getInterfaces());
         if (!interfaces.isEmpty() && interfaces.contains(Listener.class)) {
            plugin.getServer().getPluginManager().registerEvents((Listener)subCommand, plugin);
         }
      }
   }
   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
      if (sender instanceof Player) {
         Player player = (Player)sender;
         
         if (args.length == 0) {
            HcChat.error(player, "Indica un subcomando. Usa la pestaña o /hc sponsor si eres espectador.");
            return true;
         }
         
         String subCommandArg = args[0].toLowerCase();
         SubCommand subCommand = (SubCommand)this.subCommands.get(subCommandArg);
         
         if (subCommand == null) {
            HcChat.error(player, "Subcomando desconocido: " + subCommandArg);
            return true;
         }
         
         
         if (!subCommandArg.equals("sponsor") && !player.isOp()) {
            return true;
         }
         
         subCommand.perform(player, args);
         return false;
      } else {
         return true;
      }
   }
   @Nullable
   public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
      List<String> suggestions = new ArrayList();
      
      if (!(sender instanceof Player)) {
         return suggestions;
      }
      
      Player player = (Player)sender;
      SubCommand subCommand;
      
      if (args.length == 1) {
         Iterator var6 = this.subCommands.values().iterator();
         while(var6.hasNext()) {
            subCommand = (SubCommand)var6.next();
            
            if (subCommand.getName().equals("sponsor") || player.isOp()) {
               if (subCommand.getName().startsWith(args[0].toLowerCase())) {
                  ((List)suggestions).add(subCommand.getName());
               }
            }
         }
      } else {
         String subCommandArg = args[0].toLowerCase();
         subCommand = (SubCommand)this.subCommands.get(subCommandArg);
         if (subCommand == null) {
            return (List)suggestions;
         }
         
         if (subCommandArg.equals("sponsor") || player.isOp()) {
            suggestions = subCommand.getTabCompleteSuggestions(player, args);
         }
      }
      return (List)suggestions;
   }
}
