package com.tom_e_white.javamagazine;

import java.io.Serializable;
import org.apache.crunch.DoFn;
import org.apache.crunch.Emitter;
import org.apache.crunch.Pair;

class MakeSummary extends
    DoFn<Pair<Pair<Long, String>, Iterable<Event>>, Summary>
    implements Serializable {
  @Override
  public void process(
      Pair<Pair<Long, String>, Iterable<Event>> input,
      Emitter<Summary> emitter) {

    Summary summary = new Summary();
    summary.setBucket(input.first().first());
    summary.setSource(input.first().second());
    for (Event event : input.second()) {
      summary.incrementCount();
    }
    emitter.emit(summary);
  }
}
