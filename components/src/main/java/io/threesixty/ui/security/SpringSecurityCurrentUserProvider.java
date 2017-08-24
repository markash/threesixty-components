package io.threesixty.ui.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityCurrentUserProvider<T> implements CurrentUserProvider<T> {
    /**
     * Get the user
     * @return The user
     */
    @Override
    public UserPrincipal<T> get() {
        return (UserPrincipal<T>) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
