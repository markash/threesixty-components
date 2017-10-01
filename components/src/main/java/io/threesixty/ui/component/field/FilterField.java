package io.threesixty.ui.component.field;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.TextField;
import io.threesixty.ui.view.ColumnDefinition;
import io.threesixty.ui.view.FilterDefinition;
import io.threesixty.ui.view.TableDefinition;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.StringUtils;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import io.threesixty.ui.component.button.ButtonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 * @param <T> The entity type of the grid / container
 */
@SuppressWarnings("unused")
public class FilterField<T> extends CustomField<TableDefinition<T>> {
	private static final long serialVersionUID = 1L;

	private final ListDataProvider<T> dataProvider;
    private TableDefinition<T> tableDefinition;
    private FilterDefinition currentFilterDefinition = null;

    private List<String> options = new ArrayList<>();
    private List<FilterDefinition> filterDefinitions = new ArrayList<>();

    private ListDataProvider<String> optionsDataProvider = new ListDataProvider<>(options);

    private ComboBox<FilterDefinition> attributeField = new ComboBox<>();
    private ComboBox<String> optionsField = new ComboBox<>();
    private TextField textField = new TextField();

	public FilterField(
	        final ListDataProvider<T> dataProvider,
            final TableDefinition<T> tableDefinition) {

	    super();

		this.dataProvider = dataProvider;
		this.tableDefinition = tableDefinition;

		this.attributeField.addValueChangeListener(this::onAttributeChange);
        this.attributeField.setDataProvider(new ListDataProvider<>(filterDefinitions));

        this.optionsField.setDataProvider(optionsDataProvider);

        this.tableDefinition.getFilterableColumns().map(ColumnDefinition::filterDefinition).forEach(filterDefinitions::add);
        if (filterDefinitions.size() > 0) {
            this.attributeField.setSelectedItem(this.filterDefinitions.get(0));
        }

        this.optionsField.addValueChangeListener(this::onFilter);
        this.optionsField.addShortcutListener(new ClearFilterShortcutListener<>(this));
        this.textField.addShortcutListener(new FilterShortcutListener<>(this));
        this.textField.addShortcutListener(new ClearFilterShortcutListener<>(this));
	}

    @Override
    protected Component initContent() {

	    return new MHorizontalLayout()
                .withMargin(false)
                .with(attributeField, textField, optionsField, ButtonBuilder.CLEAR_ALL(this::onClear));
    }

    @Override
    protected void doSetValue(final TableDefinition<T> tableDefinition) {

	    this.tableDefinition = tableDefinition;
    }

    @Override
    public TableDefinition<T> getValue() {

	    return this.tableDefinition;
    }

    public FilterField<T> withDefaultValueChangeListener() {

	    return withValueChangeListener(this::onFilter);
    }

    public FilterField<T> withDefaultShortcutListener() {

	    addShortcutListener(new FilterField.FilterShortcutListener<>(this));
        return this;
    }

    void clearFilter() {

	    this.textField.clear();
	    this.options.clear();
        this.dataProvider.clearFilters();
    }

    private FilterField<T> withValueChangeListener(
            ValueChangeListener<TableDefinition<T>> listener) {

	    addValueChangeListener(listener);
        return this;
    }

    private SerializablePredicate<T> getFilter(
            final ValueChangeEvent event) {

	    return getFilter((String) event.getValue());
    }

    private SerializablePredicate<T> getFilter(
            final String filterText) {

        return (SerializablePredicate<T>) value ->
                propertyContainsText(
                        value,
                        Optional.ofNullable(this.currentFilterDefinition).map(FilterDefinition::getProperty).orElse(""),
                        filterText);
    }

    private void onAttributeChange(
            final ValueChangeEvent<FilterDefinition> event) {

	    this.currentFilterDefinition = event.getValue();
	    if (this.currentFilterDefinition != null) {
            boolean multiple = this.currentFilterDefinition.hasOptions();

            this.options.clear();
            this.options.addAll(this.currentFilterDefinition.getOptions());
            this.optionsField.setVisible(multiple);
            this.optionsDataProvider.refreshAll();

            this.textField.clear();
            this.textField.setPlaceholder("Filter by " + this.currentFilterDefinition.getHeading() + " ...");
            this.textField.setVisible(!multiple);
        } else {
            clearFilter();
        }
    }

	private void onFilter(final ValueChangeEvent event) {
	    onFilter((String) event.getValue());
	}

    private void onFilter(final String filterText) {
        this.dataProvider.clearFilters();
        this.dataProvider.addFilter(getFilter(filterText));
    }

    private void onClear(final Button.ClickEvent event) {
        clearFilter();
    }

	private boolean propertyContainsText(final T value, final String property, final String text) {
	    try {
	    	final String searchText = StringUtils.isEmpty(text) ? "" : text.trim().toLowerCase();
	    	final String propertyText = Optional.of(BeanUtils.getProperty(value, property)).orElse("").trim().toLowerCase();
            return propertyText.contains(searchText);
        } catch (Exception e) { /*IGNORE*/ }
        return false;
    }

    private static class FilterShortcutListener<T> extends ShortcutListener {
        private final FilterField<T> field;

        FilterShortcutListener(final FilterField<T> field) {
            super("Filter", KeyCode.ENTER, (int[]) null);
            this.field = field;
        }

        @Override
        public void handleAction(final Object sender, final Object target) {
            field.onFilter(field.textField.getValue());
        }
    }

	private static class ClearFilterShortcutListener<T> extends ShortcutListener {
		private final FilterField<T> field;

		ClearFilterShortcutListener(final FilterField<T> field) {
			super("Clear", KeyCode.ESCAPE, (int[]) null);
			this.field = field;
		}

    	@Override
        public void handleAction(final Object sender, final Object target) {
            field.clearFilter();
        }
    }
}
