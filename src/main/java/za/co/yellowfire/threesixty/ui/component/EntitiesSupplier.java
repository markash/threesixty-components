package za.co.yellowfire.threesixty.ui.component;

import java.util.List;

@FunctionalInterface
public interface EntitiesSupplier<T> {
	List<T> get();
}
