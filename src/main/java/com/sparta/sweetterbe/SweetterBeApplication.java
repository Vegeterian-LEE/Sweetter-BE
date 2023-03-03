package com.sparta.sweetterbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SweetterBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SweetterBeApplication.class, args);
	}

}
