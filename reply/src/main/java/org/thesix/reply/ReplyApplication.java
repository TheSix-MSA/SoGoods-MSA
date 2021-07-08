package org.thesix.reply;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
//@EnableDiscoveryClient
public class ReplyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReplyApplication.class, args);
    }

}
