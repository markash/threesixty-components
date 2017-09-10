package io.threesixty.ui.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SpringSecurityCurrentUserProvider<T> implements CurrentUserProvider<T> {
    /**
     * Get the user
     * @return The user
     */
    @Override
    public Optional<UserPrincipal<T>> get() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return Optional.ofNullable((UserPrincipal<T>) authentication.getPrincipal());
        }
        return Optional.empty();
    }
}
