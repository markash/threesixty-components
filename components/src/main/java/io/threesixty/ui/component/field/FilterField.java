package io.threesixty.ui.component.field;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.EventRouter;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.shared.Registration;
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

    private EventRouter eventRouter;

	public FilterField(
	        final ListDataProvider<T> dataProvider,
            final TableDefinition<T> tableDefinition) {

	    super();

		this.dataProvider = dataProvider;
		this.tableDefinition = tableDefinition;

		this.attributeField.addValueChangeListener(this::onAttributeChange);
        this.attributeField.setDataProvider(new ListDataProvider<>(filterDefinitions));
        this.attributeField.setTextInputAllowed(false);

        this.optionsField.setDataProvider(optionsDataProvider);

        this.tableDefinition.getFilterableColumns().map(ColumnDefinition::filterDefinition).forEach(filterDefinitions::add);
        if (hasFilterDefinitions()) {
            this.attributeField.setSelectedItem(this.filterDefinitions.get(0));
        }

        this.optionsField.addValueChangeListener(this::onFilterOptionSelected);
        this.optionsField.addShortcutListener(new ClearFilterShortcutListener<>(this));
        this.textField.addShortcutListener(new FilterShortcutListener<>(this));
        this.textField.addShortcutListener(new ClearFilterShortcutListener<>(this));
	}

    @Override
    public TableDefinition<T> getValue() {

        return this.tableDefinition;
    }

    public boolean hasFilterDefinitions() {

	    return this.filterDefinitions.size() > 0;
    }

    public Registration addFilterChangeListener(
            final FilterChangeListener listener) {

        return getEventRouter()
                .addListener(
                        FilterChangeEvent.class,
                        listener,
                        FilterChangeListener.class.getDeclaredMethods()[0]);
    }

    public void onFilterChange(
            final FilterChangeEvent event) {

	    if (event.getAction() == FilterChangeEvent.FilterAction.CLEAR) {

            clearFilter();

        } else if (event.getAction() == FilterChangeEvent.FilterAction.CLEAR_ALL) {

	        clearFilter();

        }
    }

    @Override
    protected Component initContent() {

        return new MHorizontalLayout()
                .withMargin(false)
                .with(attributeField, textField, optionsField);
    }

    @Override
    protected void doSetValue(final TableDefinition<T> tableDefinition) {

        this.tableDefinition = tableDefinition;
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

	private void onFilterOptionSelected(
	        final ValueChangeEvent event) {

	    if (this.currentFilterDefinition != null) {
            onFilter(this.currentFilterDefinition.getHeading(), this.currentFilterDefinition.getProperty(), (String) event.getValue());
        }
	}

    private void onFilter(
            final String heading,
            final String property,
            final String filterText) {

        this.dataProvider.clearFilters();
        this.dataProvider.addFilter(getFilter(filterText));
        getEventRouter().fireEvent(FilterChangeEvent.ADD(this, heading, property, filterText));
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

    private EventRouter getEventRouter() {

	    if (eventRouter == null) {
            eventRouter = new EventRouter();
        }
        return eventRouter;
    }

    private static class FilterShortcutListener<T> extends ShortcutListener {
        private final FilterField<T> field;

        FilterShortcutListener(final FilterField<T> field) {
            super("Filter", KeyCode.ENTER, (int[]) null);
            this.field = field;
        }

        @Override
        public void handleAction(
                final Object sender,
                final Object target) {

            if (field.attributeField.getSelectedItem().isPresent()) {

                String filterText = "";
                FilterDefinition definition = field.attributeField.getSelectedItem().get();
                if (definition.hasOptions()) {

                    if (field.optionsField.getSelectedItem().isPresent()) {
                        filterText = field.optionsField.getSelectedItem().get();
                    }

                } else {

                    filterText = field.textField.getValue();

                }

                field.onFilter(definition.getHeading(), definition.getProperty(), filterText);
            }
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
