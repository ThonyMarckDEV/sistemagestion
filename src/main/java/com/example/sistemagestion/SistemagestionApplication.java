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

	// AGREGA ESTE MÉTODO
	@PostConstruct
	public void init() {
		// Esto fuerza a toda la aplicación a usar la hora de Perú (UTC-5)
		TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
	}

}
