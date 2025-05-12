package com.pricecomparator.market.Service.Utilities;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Random;

public class Security {
    public static String generateRandomSalt(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }

        return sb.toString();
    }

    public static String generateSaltedPassword(String password, String salt) {
        String saltedPassword = password + salt;
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(saltedPassword);
    }
}
