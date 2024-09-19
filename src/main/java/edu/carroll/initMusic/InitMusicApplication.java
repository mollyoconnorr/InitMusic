package edu.carroll.initMusic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class InitMusicApplication {

	public static void main(String[] args) {
		SpringApplication.run(InitMusicApplication.class, args);
	}

}
