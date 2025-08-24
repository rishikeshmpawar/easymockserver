package com.sustaining.easymockserver.controller;

import com.sustaining.easymockserver.core.RequestHandler;
import com.sustaining.easymockserver.model.MockServerRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;

@RestController
public class MockController {
  private static final Logger LOGGER = LoggerFactory.getLogger(MockController.class);

  @Autowired
  RequestHandler requestHandler;

  @RequestMapping("/**")
  public Mono<ResponseEntity<?>> handleRequest(final HttpServletRequest httpServletRequest,
                                               final @RequestBody(required = false) String body,
                                               final @RequestHeader Map<String, String> headers) {
    return Mono.fromCallable(() -> {
        LOGGER.debug("incoming request, method: {}, URI: {}, headers: {}, body: {}",
          httpServletRequest.getMethod(), httpServletRequest, headers, body);
        MockServerRequest mockServerRequest = new MockServerRequest(httpServletRequest, body, headers);
        return requestHandler.findExpectation(mockServerRequest);
      })
      .subscribeOn(Schedulers.boundedElastic())
      .flatMap(mockServerRequest -> requestHandler.sendResponse(mockServerRequest));
  }
}