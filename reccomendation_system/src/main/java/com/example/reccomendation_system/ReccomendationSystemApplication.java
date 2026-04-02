package com.example.reccomendation_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileNotFoundException;

@SpringBootApplication
public class ReccomendationSystemApplication {

    public static void main(String[] args) throws FileNotFoundException {
        SpringApplication.run(ReccomendationSystemApplication.class, args);
    }
}
