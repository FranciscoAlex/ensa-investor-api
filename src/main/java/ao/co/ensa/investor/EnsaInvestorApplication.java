package ao.co.ensa.investor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * ENSA Investor Portal — Main Application Entry Point
 *
 * Insurance investment platform for ENSA (Empresa Nacional de Seguros de Angola)
 * providing investor registration, authentication, and portfolio management.
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
public class EnsaInvestorApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnsaInvestorApplication.class, args);
    }
}
