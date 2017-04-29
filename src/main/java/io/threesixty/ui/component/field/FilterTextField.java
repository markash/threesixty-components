package io.threesixty.ui.component.field;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.ShortcutListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Arrays;
import java.util.Optional;

public class FilterTextField<T> extends TextField {
	private static final long serialVersionUID = 1L;

	private final ListDataProvider<T> dataProvider;
    private final String[] propertiesToFilterOn;

	public FilterTextField(final ListDataProvider<T> dataProvider, final String[] propertiesToFilterOn) {
		super();

		this.dataProvider = dataProvider;
		this.propertiesToFilterOn = propertiesToFilterOn;

        addValueChangeListener(this::onFilter);

        setIcon(VaadinIcons.SEARCH);
        addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        addShortcutListener(new FilterShortcutListener<>(this));
	}

	private void onFilter(final ValueChangeEvent event) {
	    this.dataProvider.clearFilters();
	    this.dataProvider.addFilter(value ->
                Arrays.stream(propertiesToFilterOn)
                        .anyMatch(property -> propertyContainsText(value, property, (String) event.getValue())));
	}

	private boolean propertyContainsText(final T value, final String property, final String text) {
	    try {
            return Optional.of(BeanUtils.getProperty(value, property)).orElse("").toLowerCase().trim().contains(text);
        } catch (Exception e) { /*IGNORE*/ }
        return false;
    }

	private static class FilterShortcutListener<T> extends ShortcutListener {
		private final FilterTextField<T> textField;

		FilterShortcutListener(final FilterTextField<T> textField) {
			super("Clear", KeyCode.ESCAPE, (int[]) null);
			this.textField = textField;
		}

    	@Override
        public void handleAction(final Object sender, final Object target) {
            textField.setValue("");
            textField.dataProvider.clearFilters();
        }
    }
}
