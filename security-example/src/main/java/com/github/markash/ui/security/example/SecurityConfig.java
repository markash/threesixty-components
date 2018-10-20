package com.github.markash.ui.security.example;

import com.github.markash.ui.component.field.HeaderToolbar;
import com.github.markash.ui.component.field.Toolbar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.vaadin.spring.security.config.AuthenticationManagerConfigurer;

@Configuration
public class SecurityConfig implements AuthenticationManagerConfigurer {

    @Bean
    public Toolbar toolbar() {

        return new HeaderToolbar();
    }

    @Override
    public void configure(
            final AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("user").password("user").roles("USER")
                .and()
                .withUser("admin").password("password").roles("ADMIN");
    }
}
