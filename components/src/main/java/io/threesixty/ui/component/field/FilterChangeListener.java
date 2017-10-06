package io.threesixty.ui.component.field;

import com.vaadin.event.SerializableEventListener;

@FunctionalInterface
public interface FilterChangeListener extends SerializableEventListener {
    void filterChange(FilterChangeEvent event);
}