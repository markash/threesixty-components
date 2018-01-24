package com.github.markash.ui.component.notification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class NotificationsModel implements Serializable {
    private List<NotificationModel> notifications = new ArrayList<>();

    public NotificationsModel() {}

    public NotificationsModel(final List<NotificationModel> notifications) {
        setNotifications(notifications);
    }

    public List<NotificationModel> getNotifications() {
        return Collections.unmodifiableList(this.notifications);
    }

    protected void setNotifications(final Collection<NotificationModel> notifications) {
        this.notifications.clear();
        this.notifications.addAll(notifications);
    }

    public Stream<NotificationModel> stream() {
        return this.notifications.stream();
    }
}
