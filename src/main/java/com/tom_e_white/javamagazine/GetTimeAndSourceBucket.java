package com.tom_e_white.javamagazine;

import org.apache.crunch.MapFn;
import org.apache.crunch.Pair;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

class GetTimeAndSourceBucket extends
    MapFn<Event, Pair<Long, String>> {
  @Override
  public Pair<Long, String> map(Event event) {
    long minuteBucket = new DateTime(event.getTimestamp())
        .withZone(DateTimeZone.UTC)
        .minuteOfDay()
        .roundFloorCopy()
        .getMillis();
    return Pair.of(minuteBucket, event.getSource());
  }
}
