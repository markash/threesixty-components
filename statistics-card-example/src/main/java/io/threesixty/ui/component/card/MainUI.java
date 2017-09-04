package io.threesixty.ui.component.card;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Component;
import io.threesixty.ui.ApplicationUI;
import io.threesixty.ui.component.logo.Logo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.sidebar.components.ValoSideBar;

@SuppressWarnings("serial")
@Theme("dashboard")
@SpringUI
@PreserveOnRefresh
public class MainUI extends ApplicationUI {
	private static final long serialVersionUID = 1L;

    @Autowired
    private SpringViewProvider viewProvider;
    @Autowired
    private EventBus.SessionEventBus eventBus;
    @Autowired
    private Logo logo;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ValoSideBar sideBar;

    @Override
    protected Component getSideBar() {
        sideBar.setLogo(logo);
        return sideBar;
    }

    @Override
    protected ViewProvider getViewProvider() {
        return viewProvider;
    }

    @Override
    public void attach() {
        super.attach();
        eventBus.subscribe(this);
    }

    @Override
    public void detach() {
        eventBus.unsubscribe(this);
        super.detach();
    }

    @Override
    protected void init(VaadinRequest request) {
        super.init(request);
        showMainScreen();
    }

    private void showMainScreen() {
        setContent(applicationContext.getBean(MainView.class));
    }
}