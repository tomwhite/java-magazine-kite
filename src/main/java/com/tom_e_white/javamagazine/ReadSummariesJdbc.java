package com.tom_e_white.javamagazine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ReadSummariesJdbc {

  public static void main(String[] args) throws Exception {
    Class.forName("org.apache.hive.jdbc.HiveDriver");

    Connection connection = DriverManager.getConnection("jdbc:hive2://localhost:21050/;" +
        "auth=noSasl");
    Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery("SELECT * FROM summaries");
    while (resultSet.next()) {
      String source = resultSet.getString("source");
      String bucket = resultSet.getString("bucket");
      String count = resultSet.getString("count");
      System.out.printf("source=%s, bucket=%s, count=%s\n", source, bucket, count);
    }

    resultSet.close();
    statement.close();
    connection.close();
  }
}
