package io.threesixty.ui.component;

import java.util.function.Function;

/**
 * Persists the entity and returns the result
 * @param <T> The type of the entity to persist
 * @author Map P Ashworth (mp.ashworth@gmail.com)
 */
public interface EntityPersistFunction<T> extends Function<T, T> {
}
