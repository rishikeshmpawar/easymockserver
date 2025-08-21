package com.sustaining.easymockserver.controller;

import com.sustaining.easymockserver.core.ExpectationsHolder;
import com.sustaining.easymockserver.model.Expectation;
import com.sustaining.easymockserver.model.MockHttpResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Map;

@RestController
public class MockController {
  private static final Logger LOGGER = LoggerFactory.getLogger(MockController.class);

  @Autowired
  ExpectationsHolder expectationsHolder;

  @RequestMapping("/**")
  public Mono<ResponseEntity<?>> handleRequest(HttpServletRequest request,
                                               @RequestBody(required = false) String body,
                                               @RequestHeader Map<String, String> headers) {
    return Mono.fromCallable(() -> {
        String method = request.getMethod();
        String path = request.getRequestURI();

        LOGGER.debug("Incoming request, Method: {}, URI: {}, Headers: {}", method, path, headers);

        Expectation matchingExpectation = expectationsHolder.getMatchingExpectation(method, path);
        if (matchingExpectation == null) {
          LOGGER.debug("No matching expectations found method: {}, URI: {}, Headers: {}", method, path, headers);
          return Map.entry(0, ResponseEntity.notFound().build());
        }

        MockHttpResponse mockHttpResponse = matchingExpectation.getHttpResponse();
        int status = mockHttpResponse.getStatusCode();

        ResponseEntity.BodyBuilder builder = ResponseEntity.status(status);

        if (status == HttpStatus.NO_CONTENT.value() || status == HttpStatus.NOT_MODIFIED.value()) {
          // Response without body
          return Map.entry(0, builder.build());
        }

        // Response with body
        return Map.entry(0, builder.body(mockHttpResponse.getBody()));
      })
      .subscribeOn(Schedulers.boundedElastic())
      .flatMap(entry -> {
        int delay = entry.getKey();
        ResponseEntity<?> response = entry.getValue();
        return Mono.delay(Duration.ofMillis(delay))
          .map(t -> response);
      });
  }

}
