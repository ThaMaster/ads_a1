package se.umu.cs.ads.a1.backend.rest;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestApp {
    public static void main(String[] args) {
        SpringApplication.run(RestApp.class, args);
    }

    @Bean
    public CommandLineRunner CommandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("Rest Application is live!");
        };
    }

}
