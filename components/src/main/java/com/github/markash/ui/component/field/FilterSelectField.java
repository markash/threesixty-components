package com.github.markash.ui.component.field;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.ui.ComboBox;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 * @param <T> The entity type of the grid / container
 * @param <S> The combo box type
 */
public class FilterSelectField<T,S> extends ComboBox<S> {
	private static final long serialVersionUID = 1L;

	private final ListDataProvider<T> dataProvider;
    private final String propertyToFilterOn;

	public FilterSelectField(
	        final ListDataProvider<T> dataProvider,
            final Collection<S> options,
            final String propertyToFilterOn) {
		super(null, options);

		this.dataProvider = dataProvider;
		this.propertyToFilterOn = propertyToFilterOn;
        this.setEmptySelectionAllowed(true);

        addValueChangeListener(this::onFilter);
        addShortcutListener(new FilterShortcutListener<>(this));
	}

    public FilterSelectField<T,S> withDefaultValueChangeListener() {
        return withValueChangeListener(this::onFilter);
    }

    public FilterSelectField<T,S> withDefaultShortcutListener() {
        addShortcutListener(new FilterSelectField.FilterShortcutListener<>(this));
        return this;
    }

    public FilterSelectField<T,S> withValueChangeListener(ValueChangeListener<S> listener) {
        addValueChangeListener(listener);
        return this;
    }

    public SerializablePredicate<T> getFilter(final ValueChangeEvent event) {
        return (SerializablePredicate<T>) value -> Stream.of(propertyToFilterOn)
                .anyMatch(property -> propertyContainsText(value, property, (String) event.getValue()));
    }

	private void onFilter(final ValueChangeEvent event) {
	    this.dataProvider.clearFilters();
	    this.dataProvider.addFilter(getFilter(event));
	}

	private boolean propertyContainsText(final T value, final String property, final String text) {
	    try {
	    	final String searchText = StringUtils.isEmpty(text) ? "" : text.trim().toLowerCase();
	    	final String propertyText = Optional.of(BeanUtils.getProperty(value, property)).orElse("").trim().toLowerCase();
            return propertyText.contains(searchText);
        } catch (Exception e) { /*IGNORE*/ }
        return false;
    }

	private static class FilterShortcutListener<T,S> extends ShortcutListener {
		private final FilterSelectField<T,S> field;

		FilterShortcutListener(final FilterSelectField<T,S> field) {
			super("Clear", KeyCode.ESCAPE, (int[]) null);
			this.field = field;
		}

    	@Override
        public void handleAction(final Object sender, final Object target) {
            field.setSelectedItem(null);
            field.dataProvider.clearFilters();
        }
    }
}
