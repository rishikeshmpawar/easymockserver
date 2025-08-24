package com.sustaining.easymockserver.core;

import com.sustaining.easymockserver.model.Expectation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExpectationsHolder {
  private static final Logger LOGGER = LoggerFactory.getLogger(ExpectationsHolder.class);
  private final Map<Integer, Expectation> expectations = new HashMap<>();

  public Map<Integer, Expectation> getExpectations() {
    return expectations;
  }

  public Expectation addExpectation(final Expectation expectation) {
    return expectations.put(expectation.getId(), expectation);
  }

  public void cleanUpExpectations() {
    expectations.clear();
  }

  public Expectation getMatchingExpectation(final String method, final String path) {
    for (Expectation expectation : expectations.values()) {
      Expectation.MockHttpRequest req = expectation.getHttpRequest();
      if (req != null && method.equalsIgnoreCase(req.getMethod()) && path.equals(req.getPath())) {
        LOGGER.info("expectation matched: {}", expectation);
        return expectation;
      }
    }

    LOGGER.warn("No matching expectation found for method={} path={}", method, path);
    return null;
  }
}
