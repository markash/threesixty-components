package com.github.markash.ui.event;

import org.springframework.context.ApplicationEvent;

public class UserPasswordChangeEvent<T> extends ApplicationEvent {
    private static final long serialVersionUID = -4449190175823864555L;
    private final T entity;
    /**
     * An event to indicate that the user password has changed
     * @param viewName The name of the view publishing the event
     * @param entity The entity that is bound to the view
     */
    public UserPasswordChangeEvent(final String viewName, final T entity) {
        super(viewName);
        this.entity = entity;
    }

    public T getEntity() {
        return this.entity;
    }

    public static <T> UserPasswordChangeEvent build(final String viewName, final T entity) {
        return new UserPasswordChangeEvent<>(viewName, entity);
    }
}