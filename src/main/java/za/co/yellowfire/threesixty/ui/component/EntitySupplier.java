package za.co.yellowfire.threesixty.ui.component;

import java.io.Serializable;
import java.util.Optional;

@FunctionalInterface
public interface EntitySupplier<T, I extends Serializable> {
	Optional<T> get(I id);
}
