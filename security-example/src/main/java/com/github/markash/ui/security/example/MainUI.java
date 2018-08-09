package com.github.markash.ui.security.example;

import com.github.markash.ui.component.logo.Logo;
import com.github.markash.ui.component.menu.ThreeSixtyHybridMenu;
import com.github.markash.ui.component.notification.NotificationBuilder;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.server.ClientConnector;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.util.StringUtils;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.util.SecurityExceptionUtils;
import org.vaadin.spring.security.util.SuccessfulLoginEvent;

@SuppressWarnings("serial")
@Theme("security")
@SpringUI
@PreserveOnRefresh
@PushStateNavigation
public class MainUI extends UI implements ClientConnector.DetachListener, ApplicationListener<SuccessfulLoginEvent> {
	private static final long serialVersionUID = 1L;

    @Value("${threesixty.application.title:Application}")
    private String title;

    @Autowired
    private Logo logo;

    @Autowired
    private ThreeSixtyHybridMenu menu;

    @Autowired
    private VaadinSecurity vaadinSecurity;

    @Autowired
    ApplicationContext applicationContext;

    public MainUI(
            final SpringNavigator navigator) {

        setNavigator(navigator);
    }

    @Override
    protected void init(
            final VaadinRequest request) {

        getPage().setTitle(StringUtils.isEmpty(this.title) ?  "Application" : this.title);

        // Let's register a custom error handler to make the 'access denied' messages a bit friendlier.
        setErrorHandler(new DefaultErrorHandler() {
            @Override
            public void error(com.vaadin.server.ErrorEvent event) {
                if (SecurityExceptionUtils.isAccessDeniedException(event.getThrowable())) {
                    NotificationBuilder.showNotification(
                            "Authentication",
                            "Sorry, you don't have access to do that.");
                } else {
                    super.error(event);
                }
            }
        });
        if (vaadinSecurity.isAuthenticated()) {
            showMainScreen();
        } else {
            setContent(applicationContext.getBean(LoginView.class));
        }
    }

    @Override
    public void onApplicationEvent(
            final SuccessfulLoginEvent event) {

        if (event.getSource().equals(this)) {
            access(this::showMainScreen);
        } else {
            // We cannot inject the Main Screen if the event was fired from another UI, since that UI's scope would be active
            // and the main screen for that UI would be injected. Instead, we just reload the page and let the init(...) method
            // do the work for us.
            getPage().reload();
        }
    }

    @Override
    public void detach(
            final DetachEvent event) {

        getUI().close();
    }

    private void showMainScreen() {
        setContent(menu);
        getNavigator().navigateTo(DashboardView.VIEW_NAME);
    }
}