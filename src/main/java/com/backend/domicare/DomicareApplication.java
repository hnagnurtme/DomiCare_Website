package com.backend.domicare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// Swagger 2 is deprecated; use OpenAPI 3 configuration instead
public class DomicareApplication {

	public static void main(String[] args) {
		SpringApplication.run(DomicareApplication.class, args);
	}

}
