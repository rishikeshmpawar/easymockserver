package com.sustaining.easymockserver.model;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public class MockServerRequest {
  private IncomingRequest incomingRequest;
  private Expectation expectation;

  public MockServerRequest() {
    this.incomingRequest = new IncomingRequest();
    this.expectation = null;
  }

  public MockServerRequest(final HttpServletRequest httpServletRequest,
                           final String body,
                           final Map<String, String> headers) {
    this();
    this.incomingRequest.setHttpServletRequest(httpServletRequest);
    this.incomingRequest.setBody(body);
    this.incomingRequest.setHeaders(headers);
  }

  public Expectation getExpectation() {
    return expectation;
  }

  public void setExpectation(Expectation expectation) {
    this.expectation = expectation;
  }

  public IncomingRequest getIncomingRequest() {
    return incomingRequest;
  }

  public void setIncomingRequest(IncomingRequest incomingRequest) {
    this.incomingRequest = incomingRequest;
  }

  @Override
  public String toString() {
    return "MockServerRequest{" +
      "incomingRequest=" + incomingRequest +
      ", expectation=" + expectation +
      '}';
  }

  public static class IncomingRequest {
    private HttpServletRequest httpServletRequest;
    private String body;
    private Map<String, String> headers;

    public HttpServletRequest getHttpServletRequest() {
      return httpServletRequest;
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
      this.httpServletRequest = httpServletRequest;
    }

    public String getBody() {
      return body;
    }

    public void setBody(String body) {
      this.body = body;
    }

    public Map<String, String> getHeaders() {
      return headers;
    }

    public void setHeaders(Map<String, String> headers) {
      this.headers = headers;
    }

    @Override
    public String toString() {
      return "IncomingRequest{" +
        "httpServletRequest=" + httpServletRequest +
        ", body='" + body + '\'' +
        ", headers=" + headers +
        '}';
    }
  }
}

