package ao.co.ensa.investor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/investor")
@Tag(name = "Investor", description = "Investor portfolio and investment management")
@SecurityRequirement(name = "Bearer Authentication")
public class InvestorController {

    @GetMapping("/portfolio")
    @PreAuthorize("hasRole('INVESTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get investor portfolio")
    public ResponseEntity<String> getPortfolio() {
        // TODO: Implement portfolio retrieval
        return ResponseEntity.ok("{\"message\": \"Portfolio endpoint — coming soon\"}");
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('INVESTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get investor dashboard data")
    public ResponseEntity<String> getDashboard() {
        // TODO: Implement dashboard data
        return ResponseEntity.ok("{\"message\": \"Dashboard endpoint — coming soon\"}");
    }
}
