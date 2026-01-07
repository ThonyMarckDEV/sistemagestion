package com.example.sistemagestion.config;

import com.example.sistemagestion.model.Role;
import com.example.sistemagestion.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            // Si no encuentra el rol USER, lo crea
            if (roleRepository.findByNombre("USER").isEmpty()) {
                roleRepository.save(new Role(null, "USER"));
            }
            // Si no encuentra el rol ADMIN, lo crea
            if (roleRepository.findByNombre("ADMIN").isEmpty()) {
                roleRepository.save(new Role(null, "ADMIN"));
            }
        };
    }
}