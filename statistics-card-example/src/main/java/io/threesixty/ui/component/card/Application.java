package io.threesixty.ui.component.card;

import io.threesixty.ui.annotation.EnableThreeSixtyComponents;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableThreeSixtyComponents
@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
