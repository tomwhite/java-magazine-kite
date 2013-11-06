package com.tom_e_white.javamagazine.model;

import com.google.common.base.Objects;

public class Summary {
  private String source;
  private long bucket;
  private int count;

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public long getBucket() {
    return bucket;
  }

  public void setBucket(long bucket) {
    this.bucket = bucket;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public void incrementCount() {
    count++;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("source", source)
        .add("bucket", bucket)
        .add("count", count)
        .toString();
  }
}
