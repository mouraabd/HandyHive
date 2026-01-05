package com.handyhive.backend.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class HybridPasswordEncoder implements PasswordEncoder {

    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        // Always encode new passwords using BCrypt
        return bcrypt.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String storedPassword) {
        if (storedPassword == null) {
            return false;
        }

        // BCrypt hashes typically start with $2a$, $2b$, $2y$
        if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$") || storedPassword.startsWith("$2y$")) {
            return bcrypt.matches(rawPassword, storedPassword);
        }

        // Legacy fallback: stored as plaintext
        return storedPassword.equals(rawPassword.toString());
    }
}
