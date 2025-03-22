package com.nextfin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NextfinApp {

	public static void main(String[] args) {
		SpringApplication.run(NextfinApp.class, args);
	}

}
