package com.bajaj.finserv;

import com.bajaj.finserv.service.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BajajFinservApplication {

    @Autowired
    private WebhookService webhookService;

    public static void main(String[] args) {
        SpringApplication.run(BajajFinservApplication.class, args);
    }

    @Bean
    CommandLineRunner runner() {
        return args -> {
            // Run the webhook flow when application starts
            webhookService.processWebhookFlow();
        };
    }
}