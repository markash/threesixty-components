package com.github.markash.ui.view;

import com.vaadin.data.HasValue;

import java.util.Optional;

public class FormDirtyEvent implements DirtyEvent {
    private final HasValue<?> property;
    private final DirtyStatus status;
    private final Object oldValue;
    private final Object newValue;

    public FormDirtyEvent(final DirtyStatus status) {
        this.status = status;
        this.property = null;
        this.oldValue = null;
        this.newValue = null;
    }

    public FormDirtyEvent(
            final HasValue<?> property,
            final Object oldValue,
            final Object newValue) {
        this(property, oldValue, newValue, DirtyStatus.DIRTY);
    }

    public FormDirtyEvent(
            final HasValue<?> property,
            final Object oldValue,
            final Object newValue,
            final DirtyStatus status) {
        this.property = property;
        this.status = status;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public Optional<HasValue<?>> getProperty() { return Optional.ofNullable(this.property); }
    @Override
    public DirtyStatus getStatus() { return this.status; }
    @Override
    public Optional<Object> getOldValue() { return Optional.ofNullable(oldValue); }
    @Override
    public Optional<Object> getNewValue() { return Optional.ofNullable(newValue); }
}
