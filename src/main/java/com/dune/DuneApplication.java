package com.dune;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DuneApplication {

	public static void main(String[] args) {
		SpringApplication.run(DuneApplication.class, args);
	}

}
