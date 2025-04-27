package com.danosoftware.spring_boot_logging;

import com.danosoftware.spring_boot_logging.masking.LogbackInitializer;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootLoggingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootLoggingApplication.class, args);
	}

	@PostConstruct
	public void init() {
		LogbackInitializer.init();
	}

}
