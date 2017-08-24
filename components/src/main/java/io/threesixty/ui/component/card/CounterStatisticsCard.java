package io.threesixty.ui.component.card;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;

public class CounterStatisticsCard extends CustomComponent {
	private static final long serialVersionUID = 1L;

	private static final String STYLE = "card";
	private static final String STYLE_CONTAINER = STYLE + "-container";
	private static final String STYLE_TITLE = STYLE + "-title";
	private static final String STYLE_DESCRIPTION = STYLE + "-description";
	private static final String STYLE_FOOTER = STYLE + "-footer";
	private static final String STYLE_LINK = STYLE + "-link";

	private String title;
	private VaadinIcons icon;
	private String description;
	private String viewName;
	private CounterStatisticModel statistic;

	public CounterStatisticsCard(
			final String title, 
			final VaadinIcons icon,
			final String description, 
			final CounterStatisticModel statistic,
			final String viewName) {
		super();
		
		this.title = title;
		this.icon = icon;
		this.description = description;
		this.statistic = statistic;
		this.viewName = viewName;
		
		setPrimaryStyleName(STYLE);
		setCompositionRoot(buildContent());
		setSizeUndefined();
	}
	
	private CssLayout buildContent() {
		final CssLayout content = new CssLayout();
		
		final CssLayout container = new CssLayout();
		container.setPrimaryStyleName(STYLE_CONTAINER);
		
		Label image = new Label(icon.getHtml(), ContentMode.HTML);
        Label text = new Label("&nbsp;&nbsp;<strong>" + this.statistic.getFormattedValue() + " </strong> " + this.title, ContentMode.HTML);
        HorizontalLayout title = new HorizontalLayout(image, text);
        title.setPrimaryStyleName(STYLE_TITLE);
        
        Label description = new Label(this.description);
        description.setPrimaryStyleName(STYLE_DESCRIPTION);
        
        final CssLayout footer = new CssLayout();
		footer.setPrimaryStyleName(STYLE_FOOTER);
		
        MButton button = new MButton(VaadinIcons.ARROW_CIRCLE_RIGHT_O);
        button.setPrimaryStyleName(STYLE_LINK);
        button.addClickListener(event ->  UI.getCurrent().getNavigator().navigateTo(viewName));
        
        container.addComponent(title);
        container.addComponent(description);
        footer.addComponent(button);
        
        content.addComponent(container);
        content.addComponent(footer);
		return content;
	}
}
