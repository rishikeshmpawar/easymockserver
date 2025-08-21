package com.sustaining.easymockserver.model;


public class Expectation {
  private int id;
  private MockHttpRequest httpRequest;
  private MockHttpResponse httpResponse;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public MockHttpRequest getHttpRequest() {
    return httpRequest;
  }

  public void setHttpRequest(MockHttpRequest httpRequest) {
    this.httpRequest = httpRequest;
  }

  public MockHttpResponse getHttpResponse() {
    return httpResponse;
  }

  public void setHttpResponse(MockHttpResponse httpResponse) {
    this.httpResponse = httpResponse;
  }

  @Override
  public String toString() {
    return "Expectation{" +
      "id=" + id +
      ", httpRequest=" + httpRequest +
      ", httpResponse=" + httpResponse +
      '}';
  }
}