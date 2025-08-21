package com.sustaining.easymockserver.model;

public class MockHttpResponse {
  private int statusCode=200;
  private long delay=0;
  private String body="";

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public long getDelay() {
    return delay;
  }

  public void setDelay(long delay) {
    this.delay = delay;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  @Override
  public String toString() {
    return "MockHttpResponse{" +
      "statusCode=" + statusCode +
      ", delay=" + delay +
      ", body='" + body + '\'' +
      '}';
  }
}
