package io.threesixty.ui.component.card;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class CounterStatisticsCard extends CustomComponent {
    private static final long serialVersionUID = 1L;

    private static final String STYLE = "card";
    private static final String STYLE_CONTAINER = STYLE + "-container";
    private static final String STYLE_TITLE = STYLE + "-title";
    private static final String STYLE_DESCRIPTION = STYLE + "-description";
    private static final String STYLE_FOOTER = STYLE + "-footer";
    private static final String STYLE_LINK = STYLE + "-link";

    private final MLabel imageField;
    private final MLabel textField;
    private final MLabel descriptionField;
    private final MButton button;
    private final TextSupplier textSupplier;

    private Supplier<CounterStatisticModel> statisticSupplier;

    public CounterStatisticsCard(
            final String title,
            final VaadinIcons icon,
            final String description,
            final String viewName) {

        this(title, icon, description, null, viewName);
    }

	public CounterStatisticsCard(
			final String title, 
			final VaadinIcons icon,
			final String description, 
			final Supplier<CounterStatisticModel> statisticSupplier,
			final String viewName) {
		super();

		this.textSupplier = new TextSupplier(title, statisticSupplier);

        this.imageField = new MLabel(icon.getHtml())
                .withContentMode(ContentMode.HTML);
        this.textField = new MLabel()
                .withContentMode(ContentMode.HTML);
        this.descriptionField = new MLabel(description)
                .withPrimaryStyleName(STYLE_DESCRIPTION);
        this.button = new MButton(VaadinIcons.ARROW_CIRCLE_RIGHT_O)
                .withPrimaryStyleName(STYLE_LINK);
        this.button.addClickListener(event ->  UI.getCurrent().getNavigator().navigateTo(viewName));

        setPrimaryStyleName(STYLE);
		setCompositionRoot(buildContent());
		setSizeUndefined();
	}

	public void setStatisticSupplier(final Supplier<CounterStatisticModel> statisticSupplier) {
	    this.textSupplier.setStatisticSupplier(statisticSupplier);
	    this.textField.setValue(textSupplier.get());
    }

	private CssLayout buildContent() {
		final MCssLayout content = new MCssLayout();

		final MCssLayout container = new MCssLayout()
                .withPrimaryStyleName(STYLE_CONTAINER)
                .withComponents(
                        new MHorizontalLayout(imageField, textField).withPrimaryStyleName(STYLE_TITLE),
                        descriptionField);

        final MCssLayout footer = new MCssLayout()
                .withPrimaryStyleName(STYLE_FOOTER)
                .withComponents(button);

        return content.withComponents(container, footer);
	}

	private static class TextSupplier implements Supplier<String> {

	    private String title;
	    private Supplier<CounterStatisticModel> statisticSupplier;

	    TextSupplier(
	            final String title,
	            final Supplier<CounterStatisticModel> statisticSupplier) {
	        this.title = title;
	        this.statisticSupplier = statisticSupplier;
        }

        void setStatisticSupplier(final Supplier<CounterStatisticModel> statisticSupplier) {
	        this.statisticSupplier = statisticSupplier;
        }

        @Override
        public String get() {
            return "&nbsp;&nbsp;<strong>" +
                    Optional.ofNullable(this.statisticSupplier.get()).orElse(new CounterStatisticModel("Unknown", 0)).getFormattedValue() +
                    " </strong> " +
                    this.title;
        }
    }
}
