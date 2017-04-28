package za.co.yellowfire.threesixty.ui;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import za.co.yellowfire.threesixty.ui.view.ErrorView;


public abstract class ApplicationUI extends UI {
	private static final long serialVersionUID = 1L;

	@Override
    protected void init(VaadinRequest vaadinRequest) {
		getPage().setTitle("Beam");
        final HorizontalLayout rootLayout = new HorizontalLayout();
        rootLayout.setSizeFull();
        setContent(rootLayout);

        final VerticalLayout viewContainer = new VerticalLayout();
        viewContainer.setSizeFull();
        viewContainer.setMargin(false);

        final Navigator navigator = new Navigator(this, viewContainer);
        navigator.setErrorView(new ErrorView());
        navigator.addProvider(getViewProvider());
        setNavigator(navigator);

        rootLayout.addComponent(getSideBar());
        rootLayout.addComponent(viewContainer);
        rootLayout.setExpandRatio(viewContainer, 1.0f);
    }

    /**
     * The side bar component
     * @return Component
     */
	protected abstract Component getSideBar();

    /**
     * The view provider
     * @return View provider
     */
    protected abstract ViewProvider getViewProvider();
}