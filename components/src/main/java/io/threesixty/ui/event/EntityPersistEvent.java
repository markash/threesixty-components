package io.threesixty.ui.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

public class EntityPersistEvent<T extends Persistable<Serializable>> extends ApplicationEvent {
    private static final long serialVersionUID = -4449190175823864555L;

    private final T entity;

    private EntityPersistEvent(final Object source, final T entity) {
        super(source);
        this.entity = entity;
    }

    public T getEntity() { return entity; }

    public static <T extends Persistable<Serializable>> EntityPersistEvent build(final Object source, final T entity) {
        return new EntityPersistEvent<>(source, entity);
    }
}