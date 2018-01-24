package com.github.markash.ui.event;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import org.springframework.context.ApplicationEvent;

public class CloseOpenWindowsEvent extends ApplicationEvent {
	private static final long serialVersionUID = -4449190175823864555L;

	public CloseOpenWindowsEvent(LayoutClickEvent event) {
		super(event);
	}
}
