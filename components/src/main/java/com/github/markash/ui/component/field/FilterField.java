package com.github.markash.ui.component.field;

import com.github.markash.ui.view.ColumnDefinition;
import com.github.markash.ui.view.TableDefinition;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.EventRouter;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.shared.Registration;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.TextField;
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

	private ListDataProvider<T> dataProvider;
    private TableDefinition<T> tableDefinition;
    private FilterModel currentFilterDefinition = null;
    private List<FilterModel> filterDefinitions = new ArrayList<>();
    private List<FilterModel> appliedFilters = new ArrayList<>();
    private EventRouter eventRouter;

    /* Attributes */
    private ComboBox<FilterModel> attributeField = new ComboBox<>();

    /* Attribute text : Used by attributes that are searched by text */
    private TextField textField = new TextField();

    /* Attribute options : Used by attributes that have a fixed set of filter options */
    private List<String> options = new ArrayList<>();
    private ListDataProvider<String> optionsDataProvider = new ListDataProvider<>(options);
    private ComboBox<String> optionsField = new ComboBox<>();

    public FilterField() {
        super();
    }

	public FilterField(
	        final ListDataProvider<T> dataProvider,
            final TableDefinition<T> tableDefinition) {

	    super();

		this.dataProvider = dataProvider;
		this.tableDefinition = tableDefinition;
	}

    @Override
    protected Component initContent() {

        this.attributeField.setStyleName("filter-attribute");
        this.attributeField.addValueChangeListener(this::onAttributeChange);
        this.attributeField.setDataProvider(new ListDataProvider<>(filterDefinitions));
        this.attributeField.setTextInputAllowed(false);

        this.optionsField.setStyleName("filter-options");
        this.optionsField.setDataProvider(optionsDataProvider);

        this.textField.setStyleName("filter-text");

        this.tableDefinition.getFilterableColumns().map(ColumnDefinition::filterDefinition).forEach(filterDefinitions::add);
        if (hasFilterDefinitions()) {
            this.attributeField.setSelectedItem(this.filterDefinitions.get(0));
        }

        this.optionsField.addValueChangeListener(this::onFilterOptionSelected);
        this.optionsField.addShortcutListener(new ClearFilterShortcutListener<>(this));
        this.textField.addShortcutListener(new FilterShortcutListener<>(this));
        this.textField.addShortcutListener(new ClearFilterShortcutListener<>(this));

        return new MHorizontalLayout()
                .withMargin(false)
                .with(attributeField, textField, optionsField);
    }

    public ListDataProvider<T> getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(ListDataProvider<T> dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    protected void doSetValue(
            final TableDefinition<T> tableDefinition) {

        this.tableDefinition = tableDefinition;
    }

    @Override
    public TableDefinition<T> getValue() {

        return this.tableDefinition;
    }

    public FilterField<T> withDataProvider(ListDataProvider<T> dataProvider) {
        this.dataProvider = dataProvider;
        return this;
    }

    public FilterField<T> withDefinition(final TableDefinition<T> definition) {
        this.tableDefinition = definition;
        return this;
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

    void addFilter(
            final FilterModel filter) {

	    /* Ensure that the filter value is not null */
	    if (filter != null && filter.getValue() != null) {
            this.appliedFilters.add(filter);
            this.dataProvider.addFilter(FilterPredicateBuilder.build(filter));
            getEventRouter().fireEvent(FilterChangeEvent.ADD(this, filter));
        }
    }

    void removeFilter(
            final FilterModel filter) {

	    this.dataProvider.clearFilters();
	    this.appliedFilters.remove(filter);
	    this.appliedFilters.forEach(f -> this.dataProvider.addFilter(FilterPredicateBuilder.build(f)));
    }

    void removeFilters() {

	    this.textField.clear();
	    this.options.clear();
        this.appliedFilters.clear();
        this.dataProvider.clearFilters();
    }


    private FilterField<T> withValueChangeListener(
            ValueChangeListener<TableDefinition<T>> listener) {

	    addValueChangeListener(listener);
        return this;
    }

//    private SerializablePredicate<T> buildFilterPredicate(
//            final FilterModel filterModel,
//            final String filterText) {
//
//        return (SerializablePredicate<T>) value ->
//                propertyContainsText(
//                        value,
//                        Optional.ofNullable(this.currentFilterDefinition).map(FilterModel::getProperty).orElse(""),
//                        filterText);
//    }

    public void onFilterChange(
            final FilterChangeEvent event) {

        if (event.getAction() == FilterChangeEvent.FilterAction.CLEAR) {

            removeFilter(event.getFilter());

        } else if (event.getAction() == FilterChangeEvent.FilterAction.CLEAR_ALL) {

            removeFilters();

        }
    }

    private void onAttributeChange(
            final ValueChangeEvent<FilterModel> event) {

	    this.currentFilterDefinition = event.getValue();
	    if (this.currentFilterDefinition != null) {
            boolean multiple = this.currentFilterDefinition.hasAvailableOptions();

            this.options.clear();
            this.options.addAll(this.currentFilterDefinition.getAvailableOptions());
            this.optionsField.setVisible(multiple);
            this.optionsDataProvider.refreshAll();

            this.textField.clear();
            this.textField.setPlaceholder("Filter by " + this.currentFilterDefinition.getHeader() + " ...");
            this.textField.setVisible(!multiple);
        } else {
            removeFilters();
        }
    }

	private void onFilterOptionSelected(
	        final ValueChangeEvent event) {

	    if (this.currentFilterDefinition != null) {
            addFilter(this.currentFilterDefinition.withValue((String) event.getValue()));
        }
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
                FilterModel definition = field.attributeField.getSelectedItem().get();
                if (definition.hasAvailableOptions()) {

                    if (field.optionsField.getSelectedItem().isPresent()) {
                        filterText = field.optionsField.getSelectedItem().get();
                    }

                } else {

                    filterText = field.textField.getValue();

                }

                field.addFilter(definition.withValue(filterText));
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
            field.removeFilters();
        }
    }

    private static class FilterPredicateBuilder<T> {

        public static <T> SerializablePredicate<T> build(
                final FilterModel filter) {

            return (SerializablePredicate<T>) value ->
                    propertyContainsText(
                            value,
                            Optional.ofNullable(filter).map(FilterModel::getProperty).orElse(""),
                            filter.getValue());
        }

	    private static <T> boolean propertyContainsText(
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
    }
}
