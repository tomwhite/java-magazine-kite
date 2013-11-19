package com.tom_e_white.javamagazine;

import com.google.common.base.Objects;

public class Event {
  private long id;
  private long timestamp;
  private String source;

  public long getId() { return id; }
  public void setId(long id) { this.id = id; }

  public long getTimestamp() { return timestamp; }
  public void setTimestamp(long ts) { this.timestamp = ts; }

  public String getSource() { return source; }
  public void setSource(String source) { this.source = source; }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("id", id)
        .add("timestamp", timestamp)
        .add("source", source)
        .toString();
  }
}
