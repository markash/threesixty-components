package com.github.markash.ui.component;

import java.util.List;

@FunctionalInterface
public interface EntitiesSupplier<T> {
	List<T> get();
}
