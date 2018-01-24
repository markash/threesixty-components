package com.github.markash.ui.event;

import org.vaadin.spring.events.Event;

public class EventUtils {
    public static Boolean eventFor(final Event event, final Class viewClass) {
        return event != null && event.getSource() != null && viewClass != null && event.getSource().getClass().equals(viewClass);
    }
}
