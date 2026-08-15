package com.example.telco_backend;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerarHash {

    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        System.out.println("Admin*123 = " + passwordEncoder.encode("Admin*123"));
        System.out.println("Agente*123 = " + passwordEncoder.encode("Agente*123"));
        System.out.println("Back*123 = " + passwordEncoder.encode("Back*123"));
        System.out.println("Sup*123 = " + passwordEncoder.encode("Sup*123"));
    }

}
