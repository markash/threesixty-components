package io.threesixty.ui.component.field;

import com.vaadin.data.HasValue;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import io.threesixty.ui.component.button.ButtonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public class ExtendedFilterTextField<T> extends HorizontalLayout {

    private final ListDataProvider<T> dataProvider;
    private final FilterTextField<T> filterTextField;
    private final List<FilterSelectField<T, ?>> filterSelectFields = new ArrayList<>();

    public ExtendedFilterTextField(final ListDataProvider<T> dataProvider, final String[] propertiesToFilterOn) {
        this.dataProvider = dataProvider;

        this.filterTextField =
                new FilterTextField<>(dataProvider, propertiesToFilterOn)
                        .withValueChangeListener(this::onFilter)
                        .withDefaultShortcutListener();

//        this.filterSelectFields = new ArrayList<>();

        Collection<String> options = new ArrayList<>(Arrays.asList("Aries 1", "Aries 3"));
        this.filterSelectFields.add(new FilterSelectField<>(dataProvider, options, "project").withValueChangeListener(this::onFilter).withDefaultShortcutListener());

        this.addComponent(this.filterTextField);
        this.addComponent(this.filterSelectFields.get(0));
        this.addComponent(ButtonBuilder.CLEAR_ALL(this::onClear));

        this.setMargin(false);
        this.setSpacing(true);
    }

    public void addFilterSelection(
            final String propertyToFilterOn,
            final Collection<String> options) {

        FilterSelectField<T, ?> field = new FilterSelectField<>(this.dataProvider, options, propertyToFilterOn)
                .withValueChangeListener(this::onFilter)
                .withDefaultShortcutListener();

        this.filterSelectFields.add(field);
        this.addComponent(field);
    }

    private void onClear(final Button.ClickEvent event) {

    }

    private void onFilter(final HasValue.ValueChangeEvent event) {
        SerializablePredicate<T> filter = this.filterTextField.getFilter(event);
        for(FilterSelectField<T, ?> field : this.filterSelectFields) {
            filter.and(field.getFilter(event));
        }
        this.dataProvider.clearFilters();
        this.dataProvider.addFilter(filter);
    }
}
