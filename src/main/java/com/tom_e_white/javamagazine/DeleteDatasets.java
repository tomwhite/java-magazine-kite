package com.tom_e_white.javamagazine;

import com.cloudera.cdk.data.DatasetRepositories;
import com.cloudera.cdk.data.DatasetRepository;

public class DeleteDatasets {
  public static void main(String[] args) throws Exception {
    DatasetRepository repo = DatasetRepositories.open(args[0]);

    repo.delete("events");
    repo.delete("summaries");
  }
}
