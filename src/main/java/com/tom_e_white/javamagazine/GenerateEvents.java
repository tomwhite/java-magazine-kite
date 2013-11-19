package com.tom_e_white.javamagazine;

import java.util.UUID;
import org.apache.log4j.Logger;

public class GenerateEvents {
  public static void main(String[] args) throws Exception {
    Logger logger = Logger.getLogger(GenerateEvents.class);
    long i = 0;
    String source = UUID.randomUUID().toString();
    while (true) {
      Event event = new Event();
      event.setId(i++);
      event.setTimestamp(System.currentTimeMillis());
      event.setSource(source);
      logger.info(event);
      Thread.sleep(100);
    }
  }
}
