package io.threesixty.ui.security;

import java.util.function.Supplier;

/**
 * Provides the current user of the application
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public interface CurrentUserProvider<T> extends Supplier<UserPrincipal<T>> {
    /**
     * Get the user
     * @return The user
     */
    UserPrincipal<T> get();
}
