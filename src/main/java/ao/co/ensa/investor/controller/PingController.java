package ao.co.ensa.investor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Tag(name = "Ping", description = "Availability check")
public class PingController {

    @GetMapping({"/", "/ping"})
    @Operation(summary = "Ping", description = "Returns pong. Use to verify the API is up.")
    public ResponseEntity<Map<String, String>> ping() {
        return ResponseEntity.ok(Map.of("status", "UP", "message", "pong"));
    }
}
