package ao.co.ensa.investor.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * Oracle-profile datasource with automatic fallback to Railway MySQL.
 * <p>
 * On startup the bean tries to reach the primary Oracle database within 15 s.
 * If the connection fails (VPN down, tunnel inactive, etc.) it transparently
 * switches to the Railway MySQL instance configured under
 * {@code app.datasource.fallback.*}.
 * <p>
 * When running on the fallback database Flyway migrations are skipped because
 * the Railway instance is already fully migrated.
 */
@Configuration
@Profile("oracle")
public class FallbackDataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(FallbackDataSourceConfig.class);

    // ---- Primary: Oracle (from spring.datasource.*) ----
    @Value("${spring.datasource.url}")
    private String primaryUrl;

    @Value("${spring.datasource.username}")
    private String primaryUsername;

    @Value("${spring.datasource.password}")
    private String primaryPassword;

    @Value("${spring.datasource.driver-class-name:}")
    private String primaryDriver;

    // ---- Fallback: Railway MySQL (from app.datasource.fallback.*) ----
    @Value("${app.datasource.fallback.url}")
    private String fallbackUrl;

    @Value("${app.datasource.fallback.username}")
    private String fallbackUsername;

    @Value("${app.datasource.fallback.password}")
    private String fallbackPassword;

    @Value("${app.datasource.fallback.driver-class-name:com.mysql.cj.jdbc.Driver}")
    private String fallbackDriver;

    private volatile boolean usingFallback = false;

    @Bean
    @Primary
    public DataSource dataSource() {
        // --- Try primary Oracle ---
        log.info("Attempting connection to primary Oracle database at {}", primaryUrl);

        HikariConfig primary = new HikariConfig();
        primary.setJdbcUrl(primaryUrl);
        primary.setUsername(primaryUsername);
        primary.setPassword(primaryPassword);
        if (primaryDriver != null && !primaryDriver.isEmpty()) {
            primary.setDriverClassName(primaryDriver);
        }
        primary.setMaximumPoolSize(10);
        primary.setMinimumIdle(2);
        primary.setConnectionTimeout(15000);
        primary.setInitializationFailTimeout(15000);

        try {
            HikariDataSource ds = new HikariDataSource(primary);
            log.info("Connected to primary Oracle database");
            return ds;
        } catch (Exception e) {
            log.warn("Primary Oracle database unavailable: {}", e.getMessage());
        }

        // --- Fallback to Railway MySQL ---
        log.info("Falling back to Railway MySQL database at {}", fallbackUrl);
        usingFallback = true;

        HikariConfig fallback = new HikariConfig();
        fallback.setJdbcUrl(fallbackUrl);
        fallback.setUsername(fallbackUsername);
        fallback.setPassword(fallbackPassword);
        fallback.setDriverClassName(fallbackDriver);
        fallback.setMaximumPoolSize(10);
        fallback.setMinimumIdle(2);
        fallback.setConnectionTimeout(20000);
        fallback.setInitializationFailTimeout(20000);

        HikariDataSource ds = new HikariDataSource(fallback);
        log.info("Connected to fallback Railway MySQL database");
        return ds;
    }

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            if (usingFallback) {
                log.info("Skipping Flyway migration — fallback Railway MySQL is already migrated");
                return;
            }
            flyway.migrate();
        };
    }
}
