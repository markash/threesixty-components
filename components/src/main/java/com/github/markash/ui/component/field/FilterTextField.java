package com.github.markash.ui.component.field;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.ShortcutListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 * @param <T> The entity type of the grid / container
 */
public class FilterTextField<T> extends TextField {
	private static final long serialVersionUID = 1L;

	private final ListDataProvider<T> dataProvider;
    private final List<String> propertiesToFilterOn = new ArrayList<>();

    public FilterTextField(
            final ListDataProvider<T> dataProvider,
            final String[] propertiesToFilterOn) {

        this.dataProvider = dataProvider;
        Optional.ofNullable(propertiesToFilterOn).map(Arrays::asList).ifPresent(this.propertiesToFilterOn::addAll);
        setIcon(VaadinIcons.SEARCH);
        addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
    }

    public FilterTextField<T> withDefaultValueChangeListener() {

        return withValueChangeListener(this::onFilter);
    }

    public FilterTextField<T> withDefaultShortcutListener() {

        addShortcutListener(new FilterShortcutListener<>(this));
        return this;
    }

    public FilterTextField<T> withValueChangeListener(ValueChangeListener<String> listener) {

        addValueChangeListener(listener);
        return this;
    }

    public SerializablePredicate<T> getFilter(
            final ValueChangeEvent event) {

        return (SerializablePredicate<T>)
                value -> propertiesToFilterOn
                        .stream()
                        .anyMatch(property -> propertyContainsText(value, property, (String) event.getValue()));
    }

	private void onFilter(
	        final ValueChangeEvent event) {

        this.dataProvider.clearFilters();
        this.dataProvider.addFilter(getFilter(event));
	}

	private boolean propertyContainsText(
	        final T value,
            final String property,
            final String text) {

        try {
	    	final String searchText = StringUtils.isEmpty(text) ? "" : text.trim().toLowerCase();
	    	final String propertyText = Optional.of(BeanUtils.getProperty(value, property)).orElse("").trim().toLowerCase();
            return propertyText.contains(searchText);
        } catch (Exception e) { /*IGNORE*/ }
        return false;
    }

	private static class FilterShortcutListener<T> extends ShortcutListener {
		private final FilterTextField<T> textField;

		FilterShortcutListener(
		        final FilterTextField<T> textField) {

		    super("Clear", KeyCode.ESCAPE, (int[]) null);
			this.textField = textField;
		}

    	@Override
        public void handleAction(
                final Object sender,
                final Object target) {

		    textField.setValue("");
            textField.dataProvider.clearFilters();
        }
    }
}
