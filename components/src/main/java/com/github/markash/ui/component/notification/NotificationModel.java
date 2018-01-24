package com.github.markash.ui.component.notification;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontIcon;

import java.io.Serializable;

public class NotificationModel implements Serializable {

    private FontIcon icon = VaadinIcons.ENVELOPE_O;
    private String title;
    private String message;
    private boolean read;

    public NotificationModel(final FontIcon icon, String title, String message, boolean read) {
        this.icon = icon;
        this.title = title;
        this.message = message;
        this.read = read;
    }

    public FontIcon getIcon() { return icon; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public boolean isRead() { return read; }
}
