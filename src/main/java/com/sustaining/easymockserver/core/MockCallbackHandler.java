package com.sustaining.easymockserver.core;

import com.sustaining.easymockserver.model.Expectation.MockCallback;
import com.sustaining.easymockserver.model.MockServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Service
public class MockCallbackHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(MockCallbackHandler.class);
  private final WebClient webClient;
  private final int DEFAULT_HTTP_REQ_TIMEOUT = 3000;

  public MockCallbackHandler() {
    // Create HttpClient with HTTP/2 and H2C support
    HttpClient httpClient = HttpClient.create()
      .protocol(HttpProtocol.H2C, HttpProtocol.HTTP11)  // Enable HTTP/2 (h2c) and fallback to HTTP/1.1
      ;

    this.webClient = WebClient.builder()
      .clientConnector(new org.springframework.http.client.reactive.ReactorClientHttpConnector(httpClient))
      .build();
  }

  public void handleCallback(final MockServerRequest mockServerRequest) {
    if (mockServerRequest == null || mockServerRequest.getExpectation() == null ||
      mockServerRequest.getExpectation().getMockCallback() == null) {
      LOGGER.debug("initial condition check failed, no-op for callback logic");
      return;
    }

    MockCallback mockCallback = mockServerRequest.getExpectation().getMockCallback();
    LOGGER.debug("sending callback request after {}ms", mockCallback.getDelay());

    Mono.delay(Duration.ofMillis(mockCallback.getDelay()))
      .flatMap(ignore -> {
        WebClient.RequestHeadersUriSpec<?> requestSpec =
          webClient.method(HttpMethod.valueOf(mockCallback.getMethod()));

        WebClient.RequestHeadersSpec<?> headersSpec;
        if ("POST".equalsIgnoreCase(mockCallback.getMethod())
          || "PUT".equalsIgnoreCase(mockCallback.getMethod())
          || "PATCH".equalsIgnoreCase(mockCallback.getMethod())) {
          headersSpec = ((WebClient.RequestBodyUriSpec) requestSpec)
            .uri(mockCallback.getUrl())
            .headers(httpHeaders -> mockCallback.getHeaders().forEach(httpHeaders::add))
            .bodyValue(mockCallback.getBody());
        } else {
          headersSpec = requestSpec
            .uri(mockCallback.getUrl());
        }

        return headersSpec
          .retrieve()
          .toBodilessEntity()
          .timeout(Duration.ofMillis(DEFAULT_HTTP_REQ_TIMEOUT))
          .doOnSuccess(resp ->
            LOGGER.debug("Callback sent, response code: {}", resp.getStatusCode().value()))
          .doOnError(e ->
            LOGGER.debug("Failed to send callback", e));
      })
      .subscribe(); // Fire-and-forget
  }
}