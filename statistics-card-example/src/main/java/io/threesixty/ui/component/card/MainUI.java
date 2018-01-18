package io.threesixty.ui.component.card;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.ui.Component;
import io.threesixty.ui.ApplicationUI;
import io.threesixty.ui.component.logo.Logo;
import io.threesixty.ui.view.DisplayView;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.components.ValoSideBar;

@SuppressWarnings("serial")
@Theme("dashboard")
@SpringUI
@PreserveOnRefresh
@PushStateNavigation
public class MainUI extends ApplicationUI {
	private static final long serialVersionUID = 1L;

    @Autowired
    private Logo logo;
    @Autowired
    private ValoSideBar sideBar;

    public MainUI(
            final SpringNavigator navigator,
            final DisplayView displayView) {

        super(navigator, displayView);
    }

    @Override
    protected Component getSideBar() {
        sideBar.setLogo(logo);
        return sideBar;
    }

    @Override
    protected void init(VaadinRequest request) {
        super.init(request);

        getNavigator().navigateTo(DashboardView.VIEW_NAME);
    }
}