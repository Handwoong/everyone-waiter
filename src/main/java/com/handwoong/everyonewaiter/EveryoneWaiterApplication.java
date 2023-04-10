package com.handwoong.everyonewaiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class EveryoneWaiterApplication {

    public static void main(String[] args) {
        SpringApplication.run(EveryoneWaiterApplication.class, args);
    }

}
