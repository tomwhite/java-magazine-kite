package com.tom_e_white.javamagazine;

import org.kitesdk.data.Dataset;
import org.kitesdk.data.DatasetReader;
import org.kitesdk.data.DatasetRepositories;
import org.kitesdk.data.DatasetRepository;

public class ReadSummaries {

  public static void main(String[] args) throws Exception {
    DatasetRepository repo = DatasetRepositories.open(args[0]);
    Dataset<Summary> summaries = repo.load("summaries");
    DatasetReader<Summary> reader = summaries.newReader();
    reader.open();
    try {
      for (Summary summary : reader) {
        System.out.println(summary);
      }
    } finally {
      reader.close();
    }
  }
}
