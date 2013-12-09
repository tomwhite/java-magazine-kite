Writing Big Data Applications with Hadoop and Kite
==================================================

The projects contains the code accompanying the article in
Oracle's Java Magazine.

## Pre-requisites

Before running the code, first
[set up a QuickStart VM](https://github.com/kite-sdk/kite-examples#setting-up-the-quickstart-vm)
and run through the pre-requisites for [configuring Flume](https://github.com/kite-sdk/kite-examples/tree/master/logging#pre-requisites)
using the `flume.properties` file in *this* project.

## Running

To build the project, type

```bash
mvn package
```

The log data ends up in a dataset named "events". Before running the logger we need
to create the dataset on the filesystem with the following command:

```bash
mvn exec:java -Dexec.mainClass="com.tom_e_white.javamagazine.CreateDatasets" -Dexec.args="repo:hive"
```

or

```bash
mvn kite:create-dataset \
  -Dkite.datasetName=events \
  -Dkite.avroSchemaReflectClass=com.tom_e_white.javamagazine.Event
mvn kite:create-dataset \
  -Dkite.datasetName=summaries \
  -Dkite.avroSchemaReflectClass=com.tom_e_white.javamagazine.Summary
```

If you have run the example before and you get an error saying
that a dataset already exists, then you can delete the
datasets by running

```bash
mvn exec:java -Dexec.mainClass="com.tom_e_white.javamagazine.DeleteDatasets" -Dexec.args="repo:hive"
```

You can see the dataset directory hierarchy in [`/user/hive/warehouse/events`](http://localhost:8888/filebrowser/#/user/hive/warehouse//events),

Now we can run the application to do the logging.

```bash
mvn exec:java -Dexec.mainClass="com.tom_e_white.javamagazine.GenerateEvents"
```

The program writes a stream of events to the logger,
10 per second. The events are sent to the Flume agent
over IPC, and the agent writes the events to the HDFS file sink.

The Flume sink will write the events to a temporary file in
[`/user/hive/warehouse/events`](http://localhost:8888/filebrowser/#/user/hive/warehouse//events),
with a _.tmp_ extension. It will periodically roll the file
and remove the _.tmp_ extension.

Let the program run for a minute or so, then wait until the
last file no longer has the _.tmp_ extension. If you don't see
new files, make sure you have followed the [Setting up the QuickStart VM](https://github.com/kite-sdk/kite-examples#setting-up-the-quickstart-vm)
directions.

The next step is to run the Crunch program, `GenerateSummaries`:

```bash
mvn kite:run-tool -Dkite.args="repo:hive"
```

Read the summaries from Java:

```bash
mvn exec:java -Dexec.mainClass="com.tom_e_white.javamagazine.ReadSummaries" -Dexec.args="repo:hive"
```

Or using Impala via JDBC:

```bash
mvn exec:java -Dexec.mainClass="com.tom_e_white.javamagazine.ReadSummariesJdbc"
```

It's also possible to do freeform SQL queries using the
[Hue UI](http://localhost:8888/impala/execute/).

```
SELECT source, COUNT(1) AS cnt FROM events GROUP BY source
SELECT * FROM summaries
```

Finally, clean up by deleting all the datasets with

```bash
mvn exec:java -Dexec.mainClass="com.tom_e_white.javamagazine.DeleteDatasets" -Dexec.args="repo:hive"
```

or

```bash
mvn kite:delete-dataset -Dkite.datasetName=events
mvn kite:delete-dataset -Dkite.datasetName=summaries
```

