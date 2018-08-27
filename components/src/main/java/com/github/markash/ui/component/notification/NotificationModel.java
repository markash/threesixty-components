package com.github.markash.ui.component.notification;

import com.vaadin.server.AbstractErrorMessage;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.FontIcon;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class NotificationModel implements Serializable {

    private FontIcon icon;
    private String title;
    private String message;
    private boolean read;
    private long created;

    public NotificationModel() {

        created = System.currentTimeMillis();
    }

    public FontIcon getIcon() { return icon; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public boolean isRead() { return read; }
    public long getCreated() { return created; }

    public NotificationModel withIcon(final FontIcon icon) {
        this.icon = icon;
        return this;
    }

    public NotificationModel withTitle(final String title) {
        this.title = title;
        return this;
    }

    public NotificationModel withMessage(final String message) {
        this.message = message;
        return this;
    }

    public NotificationModel withMessage(final Throwable throwable) {
        this.message = AbstractErrorMessage.getErrorMessageForException(throwable).getFormattedHtmlMessage();
        return this;
    }

    public NotificationModel withRead(final boolean read) {
        this.read = read;
        return this;
    }

    public NotificationModel markRead() {
        this.read = true;
        return this;
    }

    public NotificationModel withCreated(final long created) {
        this.created = created;
        return this;
    }

    public NotificationModel withCreated(final Date created) {
        return withCreated(created != null ? created.getTime() : System.currentTimeMillis());
    }

    public NotificationModel withCreated(final LocalDate created) {
        return withCreated(created != null ? created.toEpochDay() : System.currentTimeMillis());
    }

    public NotificationModel withCreated(final LocalDateTime created) {
        return withCreated(created != null ? created.toEpochSecond(ZoneOffset.UTC) : System.currentTimeMillis());
    }

    public NotificationModel withCreated(final Instant created) {
        return withCreated(created != null ? created.toEpochMilli() : System.currentTimeMillis());
    }
}
