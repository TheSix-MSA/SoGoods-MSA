package org.thesix.attach;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AttachApplication {

    public static void main(String[] args) {
        SpringApplication.run(AttachApplication.class, args);
    }

}
