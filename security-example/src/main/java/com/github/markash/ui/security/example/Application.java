package com.github.markash.ui.security.example;

import com.github.markash.ui.annotation.EnableThreeSixtyComponents;
import com.github.markash.ui.component.i18n.annotation.EnableThreeSixtyI18N;
import com.github.markash.ui.component.menu.annotation.EnableThreeSixtyMenu;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.vaadin.spring.security.annotation.EnableVaadinManagedSecurity;

@EnableThreeSixtyMenu
@EnableThreeSixtyI18N
@EnableThreeSixtyComponents
@EnableVaadinManagedSecurity
@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
