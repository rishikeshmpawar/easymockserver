package com.sustaining.easymockserver.model;


public class MockHttpRequest {
  private String method;
  private String path;

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  @Override
  public String toString() {
    return "MockHttpRequest{" +
      "method='" + method + '\'' +
      ", path='" + path + '\'' +
      '}';
  }
}
