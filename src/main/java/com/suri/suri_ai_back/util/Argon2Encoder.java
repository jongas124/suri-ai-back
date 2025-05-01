package com.suri.suri_ai_back.util;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

@Component
public class Argon2Encoder implements PasswordEncoder{
    private Argon2 argon2 = Argon2Factory.create();
    private static final int ITERATIONS = 10;
    private static final int MEMORY = 65536;
    private static final int PARALLELISM = 2;

    @Override
    public String encode(CharSequence rawPassword) {
        String hash;
        try {
            hash = this.argon2.hash(ITERATIONS, MEMORY, PARALLELISM, rawPassword.toString().toCharArray());
        } finally {
            this.argon2.wipeArray(rawPassword.toString().toCharArray());
        }
        return hash;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            return this.argon2.verify(encodedPassword, rawPassword.toString().toCharArray());
        } finally {
            this.argon2.wipeArray(rawPassword.toString().toCharArray());
        }
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return this.argon2.needsRehash(encodedPassword, ITERATIONS, MEMORY, PARALLELISM);
    }
}
