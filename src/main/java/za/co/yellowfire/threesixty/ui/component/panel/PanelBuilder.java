package za.co.yellowfire.threesixty.ui.component.panel;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

public class PanelBuilder {

	private PanelBuilder() {}
	
	public static Layout FORM(final Component...components) {
		VerticalLayout layout = new VerticalLayout();
		//layout.addStyleName(Style.Rating.FIELDS);
		
		for (Component component : components) {
			if (component != null) {
				component.setWidth(100.0f, Unit.PERCENTAGE);
				layout.addComponent(component);
			}
		}
		return layout;
	}
	
	public static VerticalLayout VERTICAL(final Component...components) {
		VerticalLayout layout = new VerticalLayout();
		//layout.addStyleName(Style.Rating.FIELDS);
		for (Component component : components) {
			if (component != null) {
				layout.addComponent(component);
			}
		}
		return layout;
	}
	
	public static VerticalLayout VERTICAL(final boolean margin, final Component...components) {
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(margin);
		layout.setWidth(100.0f, Unit.PERCENTAGE);
		
		for (Component component : components) {
			if (component != null) {
				component.setWidth(100.0f, Unit.PERCENTAGE);
				layout.addComponent(component);
			}
		}
		return layout;
	}
	
	public static VerticalLayout VERTICAL(final String style, final Component...components) {
		VerticalLayout layout = new VerticalLayout();
		if (style != null) {
			layout.addStyleName(style);
		}
		for (Component component : components) {
			if (component != null) {
				layout.addComponent(component);
			}
		}
		return layout;
	}
	
	public static VerticalLayout VERTICAL(final String style, final Component component, final Component...components) {
		VerticalLayout layout = new VerticalLayout();
		if (style != null) {
			layout.addStyleName(style);
		}
		
		if (component != null) {
			layout.addComponent(component);
		}
		
		for (Component c : components) {
			if (component != null) {
				layout.addComponent(c);
			}
		}
		return layout;
	}
	
	public static Layout VERTICAL_CENTERED(final Component...components) {
		VerticalLayout layout = new VerticalLayout();
		//layout.addStyleName(Style.Rating.FIELDS);
		for (Component component : components) {
			if (component != null) {
				layout.addComponent(component);
				layout.setComponentAlignment(component, Alignment.MIDDLE_CENTER);
			}
		}
		return layout;
	}
	
	public static HorizontalLayout HORIZONTAL(final Component...components) {
		return HORIZONTAL(null, false, components);
	}
	
	public static HorizontalLayout HORIZONTAL(final boolean margin, final Component...components) {
		return HORIZONTAL(null, margin, components);
	}
	
	public static HorizontalLayout HORIZONTAL(final String style, final Component...components) {
		return HORIZONTAL(style, false, components);
	}
	
	public static HorizontalLayout HORIZONTAL(final String style, final boolean margin, final Component...components) {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth(100.0f, Unit.PERCENTAGE);
		layout.setSpacing(margin);
		
		if (style != null) {
			layout.setStyleName(style);
		}
		for (Component component : components) {
			if (component != null) {
				component.setWidth(100.0f, Unit.PERCENTAGE);
				layout.addComponent(component);
			}
		}
		return layout;
	}
}
