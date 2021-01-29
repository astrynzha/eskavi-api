package de.frauenhofer.iosb.eskavi.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

public class PasswordEncoder {
    public static String hash(String rawPassword) {
        //TODO replace magic number with constant from property file
        int strength = 10;
        BCryptPasswordEncoder bCryptPasswordEncoder =
                new BCryptPasswordEncoder(strength, new SecureRandom());
        return bCryptPasswordEncoder.encode(rawPassword);
    }
}
