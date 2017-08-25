package io.threesixty.ui.event;

import org.springframework.context.ApplicationEvent;

public class EnterEntityEditViewEvent<T> extends ApplicationEvent {
    private static final long serialVersionUID = -4449190175823864555L;
    private final T entity;
    /**
     * An event to indicate that an entity edit view was entered and the new entity of that view
     * @param viewName The name of the view entered
     * @param entity The entity that is bound to the view
     */
    public EnterEntityEditViewEvent(final String viewName, final T entity) {
        super(viewName);
        this.entity = entity;
    }

    public T getEntity() {
        return this.entity;
    }
}