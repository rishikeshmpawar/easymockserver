package com.sustaining.easymockserver.model;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Expectation {
  private int id;
  private MockHttpRequest httpRequest;
  private MockHttpResponse httpResponse;
  private MockCallback mockCallback;

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

  public MockCallback getMockCallback() {
    return mockCallback;
  }

  public void setMockCallback(MockCallback mockCallback) {
    this.mockCallback = mockCallback;
  }

  @Override
  public String toString() {
    return "Expectation{" +
      "id=" + id +
      ", httpRequest=" + httpRequest +
      ", httpResponse=" + httpResponse +
      ", mockCallback=" + mockCallback +
      '}';
  }

  public static class MockHttpRequest {
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

  public static class MockHttpResponse {
    private int statusCode = 200;
    private int delay = 0;
    private String body = "";

    public int getStatusCode() {
      return statusCode;
    }

    public void setStatusCode(int statusCode) {
      this.statusCode = statusCode;
    }

    public int getDelay() {
      return delay;
    }

    public void setDelay(int delay) {
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

  public static class MockCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(MockCallback.class);
    private String method = "POST";
    private String url;
    private long delay = 0;
    private String body = "{}";

    private Map<String, String> headers;
    private final long DEFAULT_DELAY = 100;

    public String getMethod() {
      return method;
    }

    public void setMethod(String method) {
      this.method = method;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public long getDelay() {
      if (delay == 0 || delay < 50 ) {
        LOGGER.debug("delay({}) is 0 or <50 hence using default delay({})", delay, DEFAULT_DELAY);
        return DEFAULT_DELAY;
      }
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

    public Map<String, String> getHeaders() {
      return headers;
    }

    public void setHeaders(Map<String, String> headers) {
      this.headers = headers;
    }

    @Override
    public String toString() {
      return "MockCallback{" +
        "method='" + method + '\'' +
        ", url='" + url + '\'' +
        ", delay=" + delay +
        ", body='" + body + '\'' +
        ", headers=" + headers +
        ", DEFAULT_DELAY=" + DEFAULT_DELAY +
        '}';
    }
  }
}