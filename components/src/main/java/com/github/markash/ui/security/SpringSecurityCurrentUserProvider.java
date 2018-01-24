package com.github.markash.ui.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SpringSecurityCurrentUserProvider<T> implements CurrentUserProvider<T> {

    @Override
    public Optional<UserPrincipal<T>> get() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return Optional.ofNullable((UserPrincipal<T>) authentication.getPrincipal());
        }
        return Optional.empty();
    }

    @Override
    public Optional<T> getUser() {
        return this.get().map(UserPrincipal::getUser);
    }
}
