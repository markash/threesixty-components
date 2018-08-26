package com.github.markash.ui.component.notification;

import com.vaadin.data.HasValue;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.Registration;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import com.github.markash.ui.component.button.ButtonBuilder;
import com.github.markash.ui.component.panel.PanelBuilder;
import org.springframework.util.StringUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;

@SuppressWarnings("unused")
public class NotificationsButton extends Button implements HasValue<NotificationsModel> {
	private static final long serialVersionUID = 1L;
    private static final String ID = "dashboard-notifications";
    private static final String BUTTON_BELL = "Notifications";
    private static final String CAPTION_HEADER = "Notifications";
    private static final String CAPTION_VIEW_ALL = "View All Notifications";

    private ClickListener viewNotificationsClickListener;
    private Window notificationsWindow;

	private NotificationsModel notifications;
	private boolean readOnly;

    @SuppressWarnings("serial")
	private NotificationsButton(
	        final NotificationsModel notifications,
            final String notificationsView) {

        this(
                notifications,
    	        (ClickListener) event -> UI.getCurrent().getNavigator().navigateTo(notificationsView)
        );
    }
    
    private NotificationsButton(
            final NotificationsModel notifications,
            final ClickListener viewNotificationsClickListener) {

        this.notifications = notifications;
        this.viewNotificationsClickListener = viewNotificationsClickListener;
    	this.addClickListener(this::onOpenNotificationsPopup);
    	
//        DashboardEventBus.register(this);
    }

    public static NotificationsButton build(
            final String id,
            final String caption,
            final VaadinIcons icon,
            final NotificationsModel model,
            final ClickListener listener) {

        NotificationsButton button = new NotificationsButton(model, listener);
        if (!StringUtils.isEmpty(id)) {
            button.setId(id);
        }
        if (icon != null) {
            button.setIcon(icon);
        }
        if (!StringUtils.isEmpty(caption)) {
            button.setCaption(caption);
        }
        return button;
    }

    public static NotificationsButton build(
            final String id,
            final String caption,
            final VaadinIcons icon,
            final NotificationsModel model,
            final String notificationsView) {

        NotificationsButton button = new NotificationsButton(model, notificationsView);
        button.setId(id);
        button.setIcon(icon);
        if (!StringUtils.isEmpty(caption)) {
            button.setCaption(caption);
        }
        return button;
    }

    public static NotificationsButton build(
            final String id,
            final String caption,
            final VaadinIcons icon,
            final NotificationsModel model,
            final ClickListener listener,
            final String...styles) {

        NotificationsButton button = build(id, caption, icon, model, listener);
        if (styles != null && styles.length > 0) {
            for(String style : styles) {
                button.addStyleName(style);
            }
        }
        return button;
    }

    public static NotificationsButton build(
            final String id,
            final String caption,
            final VaadinIcons icon,
            final NotificationsModel model,
            final String notificationsView,
            final String...styles) {

        NotificationsButton button = build(id, caption, icon, model, notificationsView);
        if (styles != null && styles.length > 0) {
            for(String style : styles) {
                button.addStyleName(style);
            }
        }
        return button;
    }

    public static NotificationsButton BELL(
            final NotificationsModel model,
            final ClickListener listener) {

        return BELL(ID, model, listener);
    }

    public static NotificationsButton BELL(
            final NotificationsModel model) {

        return BELL(ID, model, (ClickListener) null);
    }

    public static NotificationsButton BELL(
            final String id,
            final NotificationsModel model,
            final String notificationsView) {

        return build(
                id,
                BUTTON_BELL,
                VaadinIcons.BELL,
                model,
                notificationsView,
                "notifications",
                ValoTheme.BUTTON_ICON_ONLY);
    }

    public static NotificationsButton BELL(
            final String id,
            final NotificationsModel model,
            final ClickListener listener) {

        return build(
                id,
                BUTTON_BELL,
                VaadinIcons.BELL,
                model,
                listener,
                "notifications",
                ValoTheme.BUTTON_ICON_ONLY);
    }

	@Override
	public void setValue(final NotificationsModel notifications) { this.notifications = notifications; }

	@Override
	public NotificationsModel getValue() { return this.notifications; }

    @Override
    public void setReadOnly(final boolean readOnly) { this.readOnly = readOnly; }

    @Override
    public boolean isReadOnly() { return this.readOnly; }

    @Override
    public void setRequiredIndicatorVisible(boolean b) { }

    @Override
    public boolean isRequiredIndicatorVisible() { return false; }

	@Override
	public Registration addValueChangeListener(ValueChangeListener<NotificationsModel> valueChangeListener) {
		return null;
	}

	//@Subscribe
//    public void updateNotificationsCount(final NotificationsCountUpdatedEvent event) {
//        setUnreadCount(this.userService.getUnreadNotificationsCount(this.userService.getCurrentUser()));
//    }

    private void setUnreadCount(final int count) {
        setCaption(String.valueOf(count));

        String description = "Notifications";
        if (count > 0) {
            addStyleName(NotificationStyle.UNREAD);
            description += " (" + count + " unread)";
        } else {
            removeStyleName(NotificationStyle.UNREAD);
        }
        setDescription(description);
    }
    

    

	

    

    
//    protected void onReadMore(final ClickEvent event) {
//    	
//    }
    
    private void onClearAllNotifications(final ClickEvent event) {
//    	this.userService.clearNotifications(this.userService.getCurrentUser());
//    	DashboardEventBus.post(new NotificationsCountUpdatedEvent());
    }
    
    private void onOpenNotificationsPopup(
            final ClickEvent event) {

        VerticalLayout notificationsLayout = new VerticalLayout();
        notificationsLayout.setMargin(true);
        notificationsLayout.setSpacing(true);

        
        Label title = new MLabel(CAPTION_HEADER).withStyleName(ValoTheme.LABEL_H3, ValoTheme.LABEL_NO_MARGIN, NotificationStyle.HEADER);
        Button clearAllButton = ButtonBuilder.CLEAR_ALL(this::onClearAllNotifications, ValoTheme.BUTTON_BORDERLESS_COLORED, ValoTheme.BUTTON_SMALL);

        HorizontalLayout titlePanel = PanelBuilder.HORIZONTAL(NotificationStyle.HEADER, title, clearAllButton);
        titlePanel.addStyleName(ValoTheme.WINDOW_TOP_TOOLBAR);
        titlePanel.setExpandRatio(title, 2.0f);
        titlePanel.setExpandRatio(clearAllButton, 1.0f);
    	
        notificationsLayout.addComponent(titlePanel);
        
//        DashboardEventBus.post(new NotificationsCountUpdatedEvent());

        this.notifications
                .stream()
                .map(NotificationField::new)
                .forEach(notificationsLayout::addComponent);

        HorizontalLayout footer = new MHorizontalLayout()
                .withStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR)
                .withWidth(100.0f, Unit.PERCENTAGE);
        notificationsLayout.addComponent(footer);

        if (viewNotificationsClickListener != null) {
            Button showAll = new MButton(CAPTION_VIEW_ALL, viewNotificationsClickListener).withStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED, ValoTheme.BUTTON_SMALL);
            footer.addComponent(showAll);
            footer.setComponentAlignment(showAll, Alignment.TOP_CENTER);
        }

        if (notificationsWindow == null) {
            notificationsWindow = new Window();
            notificationsWindow.setWidth(300.0f, Unit.PIXELS);
            notificationsWindow.addStyleName("notifications");
            notificationsWindow.setClosable(true);
            notificationsWindow.setResizable(true);
            notificationsWindow.setDraggable(true);
            notificationsWindow.addCloseShortcut(KeyCode.ESCAPE);
            notificationsWindow.setContent(notificationsLayout);
        } else {
        	notificationsWindow.setContent(notificationsLayout);
        }

        if (!notificationsWindow.isAttached()) {
            notificationsWindow.setPositionY(event.getClientY() - event.getRelativeY() + 40);
            notificationsWindow.setPositionX(event.getClientX() - event.getRelativeX() - 300);
            getUI().addWindow(notificationsWindow);
            notificationsWindow.focus();
            this.setStyleName(NotificationStyle.BUTTON_HIDE);
        } else {
            notificationsWindow.close();
            this.setStyleName(NotificationStyle.BUTTON_SHOW);
        }
    }

}