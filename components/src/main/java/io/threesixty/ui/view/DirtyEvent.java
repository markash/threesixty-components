package io.threesixty.ui.view;

import com.vaadin.data.HasValue;

import java.io.Serializable;
import java.util.Optional;

public interface DirtyEvent extends Serializable {
    Optional<HasValue<?>> getProperty();
    DirtyStatus getStatus();
    Optional<Object> getOldValue();
    Optional<Object> getNewValue();
}
