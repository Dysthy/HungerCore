package com.dysthy.hungerCore.commands.subcommands;
import com.dysthy.hungerCore.HungerCore;
import com.dysthy.hungerCore.commands.SubCommand;
import com.dysthy.hungerCore.timer.TimerTask;
import com.dysthy.hungerCore.util.HcChat;
import com.dysthy.hungerCore.utils.TimeUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
public class TimerCommand extends SubCommand {
   public static Map<String, String> timerData = new HashMap();
   public String getName() {
      return "timer";
   }
   public String getDescription() {
      return "Configure timers for whatever expected";
   }
   public List<String> getTabCompleteSuggestions(Player player, String[] args) {
      List<String> suggestions = new ArrayList();
      if (args.length == 2) {
         suggestions = List.of("start");
      } else if (args.length == 3) {
         ((List)suggestions).add("10s");
         ((List)suggestions).add("5m");
         ((List)suggestions).add("1h");
         ((List)suggestions).add("5m15s");
         ((List)suggestions).add("1h45m");
         ((List)suggestions).add("1h10m45s");
      } else if (args.length == 4) {
         ((List)suggestions).add("deathmatch");
         ((List)suggestions).add("borde");
      }
      return (List)suggestions;
   }
   public void perform(Player player, String[] args) {
      if (args.length == 0) {
         HcChat.hint(player, "Uso: /hc timer start <tiempo> <id>");
      } else {
         if (args[1].equalsIgnoreCase("start")) {
            try {
               int time = TimeUtils.parseTime(args[2]);
               String identifier = args[3];
               TimerTask timerTask = new TimerTask(time, (remaining) -> {
                  String formatedTime = TimeUtils.formatTime(remaining);
                  timerData.put(identifier, formatedTime);
               }, () -> {
                  timerData.put(identifier, "OVERTIME");
               });
               timerTask.runTaskTimerAsynchronously(HungerCore.getInstance(), 0L, 20L);
            } catch (Exception var6) {
               HcChat.error(player, "Tiempo o ID inválido.");
            }
         }
      }
   }
}
