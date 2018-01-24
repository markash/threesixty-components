package com.github.markash.ui.view;

import com.vaadin.event.EventRouter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.github.markash.ui.component.field.Toolbar;
import com.github.markash.ui.event.CloseOpenWindowsEvent;
import org.springframework.context.ApplicationEventPublisher;

import javax.annotation.PostConstruct;
import java.util.Optional;

@SuppressWarnings("serial")
public abstract class AbstractDashboardView extends Panel implements View {
    private final VerticalLayout root;
    private final String viewCaption;
    private final Toolbar toolbar;
    private transient ApplicationEventPublisher eventPublisher;
    private EventRouter eventRouter;

    protected static final String STYLE_DASHBOARD_VIEW = "dashboard-view";
    
    public AbstractDashboardView(final String viewCaption) {
    	this.viewCaption = viewCaption;
    	this.toolbar = new Toolbar(viewCaption);
    	this.root = new VerticalLayout();
    	this.eventPublisher = null;
    }
	
	public AbstractDashboardView(final String viewCaption, final ApplicationEventPublisher eventPublisher) {
		this.viewCaption = viewCaption;
        this.toolbar = new Toolbar(viewCaption);
		this.root = new VerticalLayout();
    	this.eventPublisher = eventPublisher;
    }

	protected abstract Component buildContent();
	
	protected String getTitle() { return this.viewCaption; }
	
	protected boolean isBuilt() {
		return root.getStyleName().contains(STYLE_DASHBOARD_VIEW);
	}
	
	@PostConstruct
	public void init() {		
		if (!isBuilt()) {
			addStyleName(ValoTheme.PANEL_BORDERLESS);
	        setSizeFull();
	        
	        root.setSizeFull();
	        root.setMargin(true);
	        root.addStyleName(STYLE_DASHBOARD_VIEW);
	        setContent(root);
	        Responsive.makeResponsive(root);
	
	        root.addComponent(this.toolbar/*buildHeader()*/);
	
	        Component content = buildContent();
	        root.addComponent(content);
	        root.setExpandRatio(content, 1);
	        root.addLayoutClickListener(event -> Optional.ofNullable(eventPublisher).ifPresent(ep -> ep.publishEvent(new CloseOpenWindowsEvent(event))));
		}
	}

    public Toolbar getToolbar() { return toolbar; }

    @Override
    public void enter(
            final ViewChangeEvent event) {
    }
}
