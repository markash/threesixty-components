package com.github.markash.ui.security;

public class DefaultUserPrincipal<T> implements UserPrincipal<T> {

    private T user;

    protected DefaultUserPrincipal(final T user) {
        this.user = user;
    }

    public static <T> UserPrincipal<T> wrap(final T user) {
        return new DefaultUserPrincipal<>(user);
    }

    /**
     * Returns the underlying user of the principal
     * @return The user
     */
    @Override
    public T getUser() {
        return this.user;
    }
}
