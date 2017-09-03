package io.threesixty.ui.component.card;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import io.threesixty.ui.view.ErrorView;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.components.ValoSideBar;

/*
 * Dashboard MainView is a simple HorizontalLayout that wraps the menu on the
 * left and creates a simple container for the navigator on the right.
 */
@UIScope
@SpringComponent
public class MainView extends HorizontalLayout implements View {
	private static final long serialVersionUID = 1L;

	@Autowired
    public MainView(
            final SpringViewProvider springViewProvider,
            final ValoSideBar sideBar) {
		
        setSizeFull();
        addComponent(sideBar);

        CssLayout viewContainer = new CssLayout();
        viewContainer.setSizeFull();
        addComponent(viewContainer);
        setExpandRatio(viewContainer, 1f);

        Navigator navigator = new Navigator(UI.getCurrent(), viewContainer);
         navigator.addProvider(springViewProvider);
        navigator.setErrorView(ErrorView.class);
        navigator.navigateTo(navigator.getState());
    }
}