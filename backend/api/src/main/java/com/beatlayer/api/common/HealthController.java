package com.beatlayer.api.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
class HealthController {
  @GetMapping("/health")
  Map<String, Object> health() {
    return Map.of("status", "UP");
  }
}
