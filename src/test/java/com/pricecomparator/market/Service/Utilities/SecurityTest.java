package com.pricecomparator.market.Service.Utilities;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class SecurityTest {

    @Test
    void testGenerateRandomSalt_LengthAndCharacters() {
        int length = 16;
        String salt = Security.generateRandomSalt(length);
        assertNotNull(salt);
        assertEquals(length, salt.length());
        assertTrue(salt.matches("^[A-Za-z0-9]+$"));
    }

    @Test
    void testGenerateRandomSalt_IsRandom() {
        String salt1 = Security.generateRandomSalt(16);
        String salt2 = Security.generateRandomSalt(16);
        assertNotEquals(salt1, salt2);
    }

    @Test
    void testGenerateSaltedPassword_IsHashed() {
        String password = "securePassword";
        String salt = "randomSalt";
        String hashed = Security.generateSaltedPassword(password, salt);
        assertNotNull(hashed);
        assertNotEquals(password + salt, hashed);
    }

    @Test
    void testGenerateSaltedPassword_VerifyMatch() {
        String password = "securePassword";
        String salt = "randomSalt";
        String hash = Security.generateSaltedPassword(password, salt);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assertTrue(encoder.matches(password + salt, hash));
    }

    @Test
    void testGenerateSaltedPassword_DoesNotMatchWrongInput() {
        String password = "securePassword";
        String salt = "randomSalt";
        String hash = Security.generateSaltedPassword(password, salt);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assertFalse(encoder.matches("wrongPassword" + salt, hash));
    }
}
