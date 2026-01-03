package com.handyhive.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HandyHiveBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(HandyHiveBackendApplication.class, args);
    }

    // ❌ REMOVED the 'resetAllPasswords' bean here because it caused the crash.
    // If you need data seeding, create a new CommandLineRunner for ProviderRepository/CustomerRepository.
}