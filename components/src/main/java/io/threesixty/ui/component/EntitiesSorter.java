package io.threesixty.ui.component;

import java.util.List;

@FunctionalInterface
public interface EntitiesSorter<T> {
	List<T> sort(final List<T> rows, final boolean sortAscending, final String property);
}
