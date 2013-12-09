package com.tom_e_white.javamagazine;

import org.apache.crunch.PCollection;
import org.apache.crunch.Target;
import org.apache.crunch.types.avro.Avros;
import org.apache.crunch.util.CrunchTool;
import org.apache.hadoop.util.ToolRunner;
import org.kitesdk.data.*;
import org.kitesdk.data.crunch.CrunchDatasets;

public class GenerateSummaries extends CrunchTool {
  @Override
  public int run(String[] args) throws Exception {
    DatasetRepository repo = DatasetRepositories.open(args[0]);
    Dataset<Event> eventsDataset = repo.load("events");
    Dataset<Summary> summariesDataset = repo.load("summaries");

    PCollection<Event> events = read(
        CrunchDatasets.asSource(eventsDataset, Event.class));

    PCollection<Summary> summaries = events
        .by(new GetTimeAndSourceBucket(),
            Avros.pairs(Avros.longs(), Avros.strings()))
        .groupByKey()
        .parallelDo(new MakeSummary(),
            Avros.reflects(Summary.class));

    getPipeline().write(summaries,
        CrunchDatasets.asTarget(summariesDataset),
        Target.WriteMode.APPEND);

    return run().succeeded() ? 0 : 1;
  }
  
  public static void main(String[] args) throws Exception {
    int rc = ToolRunner.run(new GenerateSummaries(), args);
    System.exit(rc);
  }
}
