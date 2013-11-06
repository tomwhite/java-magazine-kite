package com.tom_e_white.javamagazine;

import com.cloudera.cdk.data.Dataset;
import com.cloudera.cdk.data.DatasetRepositories;
import com.cloudera.cdk.data.DatasetRepository;
import com.cloudera.cdk.data.crunch.CrunchDatasets;
import com.tom_e_white.javamagazine.model.Event;
import com.tom_e_white.javamagazine.model.Summary;
import java.io.Serializable;
import org.apache.crunch.DoFn;
import org.apache.crunch.Emitter;
import org.apache.crunch.MapFn;
import org.apache.crunch.PCollection;
import org.apache.crunch.Pair;
import org.apache.crunch.Target;
import org.apache.crunch.types.avro.Avros;
import org.apache.crunch.util.CrunchTool;
import org.apache.hadoop.util.ToolRunner;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class GenerateSummaries extends CrunchTool implements Serializable {

  @Override
  public int run(String[] args) throws Exception {

    // Construct a local filesystem dataset repository rooted at /tmp/data
    DatasetRepository repo = DatasetRepositories.open(args[0]);

    // Turn debug on while in development.
    getPipeline().enableDebug();
    getPipeline().getConfiguration().set("crunch.log.job.progress", "true");

    // Load the events dataset and get the correct partition to sessionize
    Dataset eventsDataset = repo.load("events");

    // Create a parallel collection from the working partition
    PCollection<Event> events = read(
        CrunchDatasets.asSource(eventsDataset, Event.class));

    // Group events by user and cookie id, then create a session for each group
    PCollection<Summary> sessions = events
        .by(new GetTimeBucket(), Avros.pairs(Avros.longs(), Avros.strings()))
        .groupByKey()
        .parallelDo(new MakeSummary(), Avros.reflects(Summary.class));

    // Write the sessions to the "sessions" Dataset
    getPipeline().write(sessions, CrunchDatasets.asTarget(repo.load("summaries")),
        Target.WriteMode.APPEND);

    return run().succeeded() ? 0 : 1;
  }

  public static void main(String[] args) throws Exception {
    int rc = ToolRunner.run(new GenerateSummaries(), args);
    System.exit(rc);
  }

  private class GetTimeBucket extends MapFn<Event, Pair<Long, String>> {
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

  private class MakeSummary extends DoFn<Pair<Pair<Long, String>, Iterable<Event>>, Summary> {
    @Override
    public void process(Pair<Pair<Long, String>, Iterable<Event>> input, Emitter<Summary> emitter) {
      Summary summary = new Summary();
      summary.setBucket(input.first().first());
      summary.setSource(input.first().second());
      for (Event event : input.second()) {
        summary.incrementCount();
      }
      emitter.emit(summary);
    }
  }
}
