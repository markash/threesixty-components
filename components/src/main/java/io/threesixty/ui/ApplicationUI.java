package io.threesixty.ui;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import io.threesixty.ui.view.DisplayView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

public abstract class ApplicationUI extends UI {
	private static final long serialVersionUID = 1L;

    @Value("${threesixty.application.title:Application}")
	private String title;

    private DisplayView displayView;
    /**
     * Constructs the application user interface
     * @param navigator The navigator used used by the application to navigate to views
     * @param displayView The display view is used by the navigator to display the view when a menu items is clicked or navigated to
     */
    public ApplicationUI(
            final Navigator navigator,
            final DisplayView displayView) {

        setNavigator(navigator);
        this.displayView = displayView;
    }

	@Override
    protected void init(VaadinRequest vaadinRequest) {
		getPage().setTitle(StringUtils.isEmpty(this.title) ?  "Application" : this.title);
        final HorizontalLayout rootLayout = new HorizontalLayout();
        rootLayout.setSizeFull();
        setContent(rootLayout);

        rootLayout.addComponent(getSideBar());
        rootLayout.addComponent(getDisplayView());
        rootLayout.setExpandRatio(getDisplayView(), 1.0f);
    }

    /**
     * The side bar component
     * @return Component
     */
	protected abstract Component getSideBar();

    /**
     * The display view is used by the navigator to display the view when a menu items is clicked or navigated to
     * @return The display view
     */
    private DisplayView getDisplayView() { return this.displayView; }
}