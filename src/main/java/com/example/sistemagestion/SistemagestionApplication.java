package com.example.sistemagestion;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class SistemagestionApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemagestionApplication.class, args);
	}

	@PostConstruct
	public void init() {
		//	Tiempo de Peru
		TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
	}

}
