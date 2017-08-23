package io.threesixty.ui.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityCurrentUserProvider<T> implements CurrentUserProvider<T> {
    /**
     * Get the user
     * @return The user
     */
    @Override
    public UserPrincipal<T> get() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (UserPrincipal<T>) DefaultUserPrincipal.wrap(principal);
    }
}
