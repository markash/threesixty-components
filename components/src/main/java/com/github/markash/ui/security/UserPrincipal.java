package com.github.markash.ui.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public interface UserPrincipal<T> extends UserDetails {
    /**
     * Returns the underlying user of the principal
     * @return The user
     */
    T getUser();

    @Override
    default Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    default String getPassword() {
        return null;
    }

    @Override
    default String getUsername() {
        return null;
    }

    @Override
    default boolean isAccountNonExpired() {
        return true;
    }

    @Override
    default boolean isAccountNonLocked() {
        return true;
    }

    @Override
    default boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    default boolean isEnabled() {
        return true;
    }
}
