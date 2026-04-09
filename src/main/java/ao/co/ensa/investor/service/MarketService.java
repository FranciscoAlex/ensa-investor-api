package ao.co.ensa.investor.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.http.HttpClient;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

@Service
@Slf4j
public class MarketService {

    private static final String BODIVA_URL =
            "https://www.bodiva.ao/website/api/GetAllDaillyTradeAmountVariation_no.php?securityCode=ENSAAAAA&timeline=0";

    private final RestTemplate restTemplate;

    public MarketService() {
        this.restTemplate = createInsecureRestTemplate();
    }

    private RestTemplate createInsecureRestTemplate() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return null; }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());

            HttpClient httpClient = HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .build();

            return new RestTemplate(new JdkClientHttpRequestFactory(httpClient));
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            log.error("Failed to create insecure RestTemplate, falling back to default", e);
            return new RestTemplate();
        }
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
