package com.sri.airfp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
@PropertySource("classpath:application-secret.properties")
public class AirfpApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirfpApplication.class, args);
	}

}
