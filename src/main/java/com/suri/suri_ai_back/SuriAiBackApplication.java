package com.suri.suri_ai_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SuriAiBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(SuriAiBackApplication.class, args);
	}

}
