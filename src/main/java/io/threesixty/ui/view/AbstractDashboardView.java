package io.threesixty.ui.view;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.context.ApplicationEventPublisher;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public abstract class AbstractDashboardView extends Panel implements View {
    private final VerticalLayout root;
    private final String viewCaption;
    private transient Optional<ApplicationEventPublisher> eventPublisher;
    
    protected static final String STYLE_DASHBOARD_VIEW = "dashboard-view";
    
    public AbstractDashboardView(final String viewCaption) {
    	this.viewCaption = viewCaption;
    	this.root = new VerticalLayout();
    	this.eventPublisher = Optional.empty();
    }
	
	public AbstractDashboardView(final String viewCaption, final ApplicationEventPublisher eventPublisher) {
		this.viewCaption = viewCaption;
    	this.root = new VerticalLayout();
    	this.eventPublisher = Optional.of(eventPublisher);
    }

	protected abstract Component buildContent();
	
	protected String getTitle() { return this.viewCaption; }
	protected Component getHeaderButtons() { return null; } 
	
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
	
	        root.addComponent(buildHeader());
	
	        Component content = buildContent();
	        root.addComponent(content);
	        root.setExpandRatio(content, 1);
	        root.addLayoutClickListener(event -> eventPublisher.ifPresent(ep -> ep.publishEvent(new CloseOpenWindowsEvent(event))));
		}
	}
	
	protected Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);

        Label titleLabel = new Label(getTitle());
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        Component headerButtons = getHeaderButtons();
        if (headerButtons != null) {
        	header.addComponent(headerButtons);
        }

        return header;
    }
	
	protected Layout buildPanel(final Component...components) {
		VerticalLayout layout = new VerticalLayout();
		//layout.addStyleName(Style.Rating.FIELDS);
		
		for (Component component : components) {
			layout.addComponent(component);
		}
		return layout;
	}
	
	protected Layout buildVerticalPanel(final Component...components) {
		VerticalLayout layout = new VerticalLayout();
		//layout.addStyleName(Style.Rating.FIELDS);
		for (Component component : components) {
			layout.addComponent(component);
		}
		return layout;
	}
	
	protected Layout buildHorizontalPanel(final Component...components) {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth(100.0f, Unit.PERCENTAGE);
		
		for (Component component : components) {
			layout.addComponent(component);
		}
		return layout;
	}
		
	@Override
    public void enter(final ViewChangeEvent event) {
    }
}
