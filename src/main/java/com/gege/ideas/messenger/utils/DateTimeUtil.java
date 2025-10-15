package com.gege.ideas.messenger.utils;

import java.time.Duration;
import java.time.Instant;

public class DateTimeUtil {

   public static long getDifferenceInMinutesFromNow(long pastMillis) {
      Instant past = Instant.ofEpochMilli(pastMillis);
      Instant now = Instant.now();
      return Duration.between(past, now).toMinutes();
   }
}
