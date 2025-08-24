package com.sustaining.easymockserver.core;

import com.sustaining.easymockserver.model.Expectation;
import com.sustaining.easymockserver.model.MockServerRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

import com.sustaining.easymockserver.model.MockServerRequest.IncomingRequest;

@Service
public class RequestHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(RequestHandler.class);

  @Autowired
  ExpectationsHolder expectationsHolder;

  @Autowired
  MockCallbackHandler mockCallbackHandler;

  public MockServerRequest findExpectation(final MockServerRequest mockServerRequest) {
    if (mockServerRequest == null || mockServerRequest.getIncomingRequest() == null) {
      return null;
    }

    IncomingRequest incomingRequest = mockServerRequest.getIncomingRequest();
    HttpServletRequest httpServletRequest = incomingRequest.getHttpServletRequest();
    String method = httpServletRequest.getMethod();
    String path = httpServletRequest.getRequestURI();


    Expectation matchedExp = expectationsHolder.getMatchingExpectation(method, path);
    if (matchedExp == null) {
      LOGGER.debug("no matching expectations found method: {}, URI: {}", method, path);
    }

    mockServerRequest.setExpectation(matchedExp);
    return mockServerRequest;
  }

  public Mono<ResponseEntity<?>> sendResponse(final MockServerRequest mockServerRequest) {
    if (mockServerRequest == null || mockServerRequest.getExpectation() == null) {
      LOGGER.debug("mockServerRequest is null or no matching expectations found");
      return Mono.just(ResponseEntity.notFound().build());
    }

    Expectation expectation = mockServerRequest.getExpectation();
    Expectation.MockHttpResponse mockHttpResponse = expectation.getHttpResponse();
    int status = mockHttpResponse.getStatusCode();
    ResponseEntity.BodyBuilder responseEntityBuilder = ResponseEntity.status(status);
    if (status == HttpStatus.NO_CONTENT.value() || status == HttpStatus.NOT_MODIFIED.value()) {
      mockCallbackHandler.handleCallback(mockServerRequest);
      return Mono.just(responseEntityBuilder.build());
    }

    ResponseEntity<?> response = responseEntityBuilder.body(mockHttpResponse.getBody());
    int delay = expectation.getHttpResponse().getDelay();
    mockCallbackHandler.handleCallback(mockServerRequest);
    return Mono.delay(Duration.ofMillis(delay))
      .map(e -> response);
  }
}
