package com.fao.flashcards.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Überprüft die Verfügbarkeit der Datenbank beim Start der Anwendung.
 * Implementiert einen Retry-Mechanismus für den Fall, dass die Datenbank noch nicht verfügbar ist.
 */
@Configuration
@ConditionalOnProperty(name = "spring.profiles.active", havingValue = "!dev", matchIfMissing = true)
public class DatabaseHealthCheck {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseHealthCheck.class);
    private static final int MAX_RETRIES = 10;
    private static final long RETRY_DELAY_MS = 5000; // 5 Sekunden

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseHealthCheck(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Wird nach der Initialisierung des Beans ausgeführt und prüft die Verfügbarkeit der Datenbank.
     * Versucht mehrmals, eine Verbindung zur Datenbank herzustellen, wenn diese nicht sofort verfügbar ist.
     */
    @PostConstruct
    public void checkDatabaseConnection() {
        logger.info("Überprüfe Datenbankverbindung...");
        
        int retryCount = 0;
        boolean connected = false;
        LocalDateTime startTime = LocalDateTime.now();
        
        while (!connected && retryCount < MAX_RETRIES) {
            try {
                // Versuche, eine Verbindung zur Datenbank herzustellen
                try (Connection connection = dataSource.getConnection()) {
                    // Führe eine einfache Abfrage aus, um die Verbindung zu testen
                    jdbcTemplate.execute("SELECT 1");
                    connected = true;
                    
                    Duration duration = Duration.between(startTime, LocalDateTime.now());
                    logger.info("Datenbankverbindung erfolgreich hergestellt nach {} Sekunden", duration.getSeconds());
                }
            } catch (SQLException e) {
                retryCount++;
                logger.warn("Datenbankverbindung fehlgeschlagen (Versuch {}/{}): {}", 
                        retryCount, MAX_RETRIES, e.getMessage());
                
                if (retryCount < MAX_RETRIES) {
                    logger.info("Warte {} ms vor dem nächsten Verbindungsversuch...", RETRY_DELAY_MS);
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        logger.error("Thread wurde während des Wartens unterbrochen", ie);
                        break;
                    }
                }
            }
        }
        
        if (!connected) {
            String errorMessage = "Konnte keine Verbindung zur Datenbank herstellen nach " + MAX_RETRIES + " Versuchen";
            logger.error(errorMessage);
            throw new DatabaseConnectionException(errorMessage);
        }
    }
    
    /**
     * Exception, die geworfen wird, wenn keine Verbindung zur Datenbank hergestellt werden kann.
     */
    public static class DatabaseConnectionException extends RuntimeException {
        public DatabaseConnectionException(String message) {
            super(message);
        }
    }
}