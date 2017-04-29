package io.threesixty.ui.view;

import org.springframework.context.ApplicationEvent;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;

public class CloseOpenWindowsEvent extends ApplicationEvent {
	private static final long serialVersionUID = -4449190175823864555L;

	public CloseOpenWindowsEvent(LayoutClickEvent event) {
		super(event);
	}
}
