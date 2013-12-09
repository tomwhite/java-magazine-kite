package com.tom_e_white.javamagazine;

import org.kitesdk.data.DatasetRepositories;
import org.kitesdk.data.DatasetRepository;

public class DeleteDatasets {
  public static void main(String[] args) throws Exception {
    DatasetRepository repo = DatasetRepositories.open(args[0]);

    repo.delete("events");
    repo.delete("summaries");
  }
}
