package com.dysthy.hungerCore.utils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class TimeUtils {
   private static final int SECONDS_PER_MINUTE = 60;
   private static final int SECONDS_PER_HOUR = 3600;
   public static int parseTime(String time) {
      String regex = "(\\d+)h|(\\d+)m|(\\d+)s";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(time);
      int hours = 0;
      int minutes = 0;
      int seconds = 0;
      while(matcher.find()) {
         if (matcher.group(1) != null) {
            hours = Integer.parseInt(matcher.group(1));
         }
         if (matcher.group(2) != null) {
            minutes = Integer.parseInt(matcher.group(2));
         }
         if (matcher.group(3) != null) {
            seconds = Integer.parseInt(matcher.group(3));
         }
      }
      int totalMinutes = minutes + seconds / 60;
      seconds %= 60;
      int totalHours = hours + totalMinutes / 60;
      minutes = totalMinutes % 60;
      return totalHours * 3600 + minutes * 60 + seconds;
   }
   public static String formatTime(int totalSeconds) {
      int hours = totalSeconds / 3600;
      int minutes = (totalSeconds % 3600) / 60;
      int seconds = totalSeconds % 60;
      return String.format("%02d:%02d", minutes, seconds);
   }
}
