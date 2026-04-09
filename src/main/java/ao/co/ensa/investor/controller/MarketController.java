package ao.co.ensa.investor.controller;

import ao.co.ensa.investor.service.MarketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/market")
@RequiredArgsConstructor
@Tag(name = "Market", description = "ENSA market data (proxied from BODIVA with caching)")
public class MarketController {

    private final MarketService marketService;

    @GetMapping("/bodiva-trades")
    @Operation(
        summary = "Get ENSA daily trade data",
        description = "Returns ENSA (ENSAAAAA) daily trade data from BODIVA. Cached server-side for 10 minutes."
    )
    public ResponseEntity<Object> getBodivaTradeData() {
        Object data = marketService.fetchBodivaTradeData();
        return ResponseEntity.ok(data);
    }
}
