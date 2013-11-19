package com.tom_e_white.javamagazine;

import com.cloudera.cdk.data.Dataset;
import com.cloudera.cdk.data.DatasetReader;
import com.cloudera.cdk.data.DatasetRepositories;
import com.cloudera.cdk.data.DatasetRepository;

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
