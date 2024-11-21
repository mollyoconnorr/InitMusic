package edu.carroll.initMusic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The main entry point for the InitMusic application.
 * <p>
 * This class configures and starts the Spring Boot application. It enables JPA auditing
 * and transaction management, which are used for handling database operations and ensuring
 * proper transactional behavior within the application.
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement
public class InitMusicApplication {

    /**
     * The main method that starts the InitMusic application.
     *
     * @param args command-line arguments passed to the application (if any)
     */
    public static void main(String[] args) {
        SpringApplication.run(InitMusicApplication.class, args);
    }
}
