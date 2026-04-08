package com.ameqran.cuda.inference.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.ameqran.cuda.inference")
public class InferenceBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(InferenceBootApplication.class, args);
    }
}
