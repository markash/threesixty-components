package com.github.markash.ui.event;

import org.springframework.context.ApplicationEvent;

public class LogoutEvent extends ApplicationEvent {
    private static final long serialVersionUID = -4449190175823864555L;

    public LogoutEvent(final Object source) {
        super(source);
    }
}