package io.threesixty.ui.component.notification;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;

public class NotificationField extends CustomField<NotificationModel> {
    private static final String STYLE_ICON = "notification-icon";
    private static final String STYLE_TITLE = "notification-title";
    private static final String STYLE_TIME = "notification-time";
    private static final String STYLE_ITEM = "notification-item";
    private static final String STYLE_CONTENT = "notification-content";

    private NotificationModel notification;

    public NotificationField(final NotificationModel notification) {
        this.notification = notification;
    }

    @Override
    protected Component initContent() {

        Label icon = new MLabel(notification.getIcon().getHtml()).withContentMode(ContentMode.HTML).withStyleName(STYLE_ICON);
        Label title = new MLabel(notification.getTitle()).withContentMode(ContentMode.HTML).withStyleName(STYLE_TITLE);
        Label time = new MLabel("Multi").withStyleName(STYLE_TIME);
        Button readMore = new MButton("Read more"/*, this::onReadMore*/).withStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED, ValoTheme.BUTTON_SMALL, STYLE_CONTENT);

        HorizontalLayout content = new MHorizontalLayout(title, new MLabel(), readMore);
        HorizontalLayout notification =
                new MHorizontalLayout(
                        icon,
                        content,
                        time)
                        .withStyleName(STYLE_ITEM);

        notification.setExpandRatio(icon, 1.0f);
        notification.setExpandRatio(content, 5.0f);
        notification.setExpandRatio(time, 1.0f);

        return notification;
    }

    @Override
    protected void doSetValue(NotificationModel notificationModel) {
        this.notification = notificationModel;
    }

    @Override
    public NotificationModel getValue() {
        return this.notification;
    }
}
