package com.tom_e_white.javamagazine;

import com.cloudera.cdk.data.DatasetDescriptor;
import com.cloudera.cdk.data.DatasetRepositories;
import com.cloudera.cdk.data.DatasetRepository;

public class CreateDatasets {

  public static void main(String[] args) throws Exception {
    DatasetRepository repo = DatasetRepositories.open(args[0]);

    DatasetDescriptor descriptor =
        new DatasetDescriptor.Builder()
            .schema(Event.class).build();
    repo.create("events", descriptor);

    DatasetDescriptor summaryDescriptor =
        new DatasetDescriptor.Builder()
            .schema(Summary.class).build();
    repo.create("summaries", summaryDescriptor);
  }
}
