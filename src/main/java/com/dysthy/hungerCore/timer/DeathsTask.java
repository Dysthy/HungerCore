package com.dysthy.hungerCore.timer;
import com.dysthy.hungerCore.events.TributeListener;
import java.util.List;
import org.bukkit.scheduler.BukkitRunnable;
public class DeathsTask extends BukkitRunnable {
   private int counter = 0;
   private final int stay;
   public DeathsTask(int stay) {
      this.stay = stay;
   }
   public void run() {
      List<String> latestDeaths = TributeListener.latestDeaths;
      if (latestDeaths != null && !latestDeaths.isEmpty()) {
         ++this.counter;
         if (this.counter == this.stay) {
            latestDeaths.removeFirst();
            this.counter = 0;
         }
      }
   }
}
