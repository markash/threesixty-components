package io.threesixty.ui.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.vaadin.spring.events.EventBus;

public class EventBusPublisherWrapper implements ApplicationEventPublisher {

    private final EventBus eventBus;
    private final Object sender ;

    public EventBusPublisherWrapper(final Object sender , final EventBus eventBus) {
        this.sender = sender;
        this.eventBus = eventBus;
    }

    @Override
    public void publishEvent(final ApplicationEvent payload) {
        eventBus.publish(this.sender, payload);
    }

    @Override
    public void publishEvent(final Object payload) {
        eventBus.publish(this.sender, payload);
    }
}
