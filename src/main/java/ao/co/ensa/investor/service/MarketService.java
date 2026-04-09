package ao.co.ensa.investor.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class MarketService {

    private static final String BODIVA_URL =
            "https://www.bodiva.ao/website/api/GetAllDaillyTradeAmountVariation_no.php?securityCode=ENSAAAAA&timeline=0";

    private final RestTemplate restTemplate;

    public MarketService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Fetches ENSA daily trade data from BODIVA.
     * Result is cached in Redis for 10 minutes under the "bodivaMarket" cache.
     */
    @Cacheable(value = "bodivaMarket", key = "'ensa-trades'")
    public Object fetchBodivaTradeData() {
        log.info("Fetching fresh BODIVA trade data from upstream API");
        return restTemplate.getForObject(BODIVA_URL, Object.class);
    }
}
