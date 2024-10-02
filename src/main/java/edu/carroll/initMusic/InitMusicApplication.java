package edu.carroll.initMusic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement
public class InitMusicApplication {

	public static void main(String[] args) {
		SpringApplication.run(InitMusicApplication.class, args);
	}

}
