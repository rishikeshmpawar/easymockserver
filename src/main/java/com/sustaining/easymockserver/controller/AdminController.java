package com.sustaining.easymockserver.controller;

import com.sustaining.easymockserver.core.ExpectationsHolder;
import com.sustaining.easymockserver.model.Expectation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AdminController {
  private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

  @Autowired
  ExpectationsHolder expectationsHolder;

  @PostMapping("/admin/expectations/add")
  public Map<Integer, Expectation> addExpectations(@RequestBody Expectation newExpectation) {
    expectationsHolder.addExpectation(newExpectation);
    LOGGER.info("Added new expectation: {}", newExpectation);
    return expectationsHolder.getExpectations();
  }

  @GetMapping("/admin/expectations")
  public Map<Integer, Expectation> getExpectations() {
    return expectationsHolder.getExpectations();
  }

  @DeleteMapping("/admin/expectations/cleanup")
  public ResponseEntity<String > cleanupExpectations() {
    expectationsHolder.cleanUpExpectations();
    return ResponseEntity.ok().build();
  }
}
