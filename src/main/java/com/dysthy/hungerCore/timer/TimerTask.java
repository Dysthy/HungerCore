package com.dysthy.hungerCore.timer;
import java.util.function.IntConsumer;
import org.bukkit.scheduler.BukkitRunnable;
public class TimerTask extends BukkitRunnable {
   private int counter;
   private final IntConsumer countdownAction;
   private final Runnable completionAction;
   public TimerTask(int limit, IntConsumer countdownAction, Runnable completionAction) {
      this.counter = limit;
      this.countdownAction = countdownAction;
      this.completionAction = completionAction;
   }
   public void run() {
      if (this.counter == 0) {
         this.completionAction.run();
         this.cancel();
      }
      this.countdownAction.accept(this.counter);
      --this.counter;
   }
}
