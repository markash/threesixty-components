package com.github.markash.ui.view;

import com.github.markash.ui.component.field.FilterModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @param <BEAN> The type of the bean

 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public class ColumnDefinition<BEAN> {
    /* */
    private boolean id;
    private boolean searchable;
    private final TableDefinition<BEAN> tableDefinition;
    /* Heading */
    private String heading;
    /* Filtering */
    private List<String> filterOptions;


    /* Property */
//    private String property;
//    private HasValue<FIELD> editor;
//    private ValueProvider<BEAN, FIELD> getter;
//    private Setter<BEAN, FIELD> setter;
//    private Binder.Binding<BEAN, FIELD> binding;
//    private Renderer<?> renderer;

    private ValueDefinition<BEAN, ?> property;

    /* Display Property: The property to display instead of the property */
    private com.github.markash.ui.view.ValueDefinition<BEAN, ?> display;

    @SuppressWarnings("unused")
    ColumnDefinition(
            final TableDefinition<BEAN> tableDefinition) {

        this.tableDefinition = tableDefinition;
    }

    public boolean isId() { return id; }
    public String getHeading() { return heading; }
    public ValueDefinition<BEAN, ?> getProperty() { return property; }
    public ValueDefinition<BEAN, ?> getDisplay() { return display; }
    boolean isSearchable() { return searchable; }

//    public Optional<Renderer<?>> getRenderer() { return Optional.ofNullable(this.renderer); }

//    public void setProperty(final String property) { this.property = property; }
//    public void setHeading(final String heading) { this.heading = heading; }
//    public void setSearchable(final boolean searchable) { this.searchable = searchable; }
//    public void setBinding(final Binder.Binding<BEAN, FIELD> binding) { this.binding = binding; }
//    public void setGetter(final ValueProvider<BEAN, FIELD> getter) { this.getter = getter; }
//    public void setSetter(final Setter<BEAN, FIELD> setter) { this.setter = setter; }


    /**
     * Whether the display property or display value provider is set
     * @return True if these are set else false
     */
    public boolean hasDisplaySetting() { return this.display != null; }
    /**
     * Sets the column definition as an identity for the record
     * @return The column definition
     */
    @SuppressWarnings("unused")
    public ColumnDefinition<BEAN> identity() {
        this.id = true;
        return this;
    }
    /**
     * Define the heading of the column
     * @param heading The heading value
     * @return The column definition
     */
    @SuppressWarnings("unused")
    public ColumnDefinition<BEAN> withHeading(final String heading) {
        this.heading = heading;
        return this;
    }
    /**
     * Enable the column to be made available in the text search
     * @return The column definition
     */
    @SuppressWarnings("unused")
    public ColumnDefinition<BEAN> enableTextSearch() {
        this.searchable = true;
        return this;
    }
    /**
     * Enable the column to be made available in the text search
     * @param options The search options
     * @return The column definition
     */
    @SuppressWarnings("unused")
    public ColumnDefinition<BEAN> enableTextSearch(final Collection<?> options) {
        this.searchable = true;
        this.filterOptions = new ArrayList<>();
        options.stream().map(Object::toString).forEach(this.filterOptions::add);
        return this;
    }
    /**
     * Disable text search on the column
     * @return The columns definition
     */
    @SuppressWarnings("unused")
    public ColumnDefinition<BEAN> disableTextSearch() {
        this.searchable = false;
        this.filterOptions = null;
        return this;
    }
    /**
     * The value definition of the column
     * @param builder The value definition builder
     * @return The column definition
     */
    @SuppressWarnings("unused")
    public ColumnDefinition<BEAN> withValue(
            final ValueBuilder<?> builder) {

//        com.vaadin.data.PropertyDefinition<BEAN, ?> definition = getProperty(property);
//        this.property = new ValueDefinition<>(this, definition.getName(), definition.getGetter());
//        return this.property;

        this.property = builder.build(this.tableDefinition);
        return this;
    }
    /**
     * The display value definition of the column
     * @param builder The value definition builder
     * @return The column definition
     */
    public ColumnDefinition<BEAN> withDisplay(
            final ValueBuilder<?> builder) {

        this.display = builder.build(this.tableDefinition);
        return this;
    }
    /**
     * End the definition of the column
     * @return The table definition
     */
    public TableDefinition<BEAN> end() {
        return this.tableDefinition;
    }

    public String renderDisplay(
            final BEAN bean) {

        return getDisplay() !=  null ? getDisplay().render(bean) : getProperty().render(bean);
    }

    public String renderProperty(
            final BEAN bean) {

        return getProperty().render(bean);
    }

//    @SuppressWarnings({"unused", "unchecked"})
//    public ValueDefinition forProperty(
//            final String propertyName,
//            final ValueProvider<BEAN, FIELD> provider) {
//
//
//        this.property = new ValueDefinition<>(this, propertyName, provider);
//        return this.property;
//    }



//    public com.github.markash.ui.view.ValueDefinition display(
//            final String propertyName,
//            final ValueProvider<BEAN, ?> provider) {
//
//        this.display = new com.github.markash.ui.view.ValueDefinition<>(propertyName, provider);
//        return this.display;
//    }

    public FilterModel filterDefinition() {

        return new FilterModel(
                this.heading,
                this.display != null ? this.display.getPropertyName() : this.property.getPropertyName(),
                this.filterOptions);
    }

//    /**
//     * Get the value provider for the property on the table definition entity class
//     * @param property The property
//     * @return The value provider for the getter on the entity
//     */
//    private com.vaadin.data.PropertyDefinition<BEAN, ?> getProperty(
//            final String property) {
//
//        return this.tableDefinition
//                        .getPropertySet()
//                        .getProperty(property).orElseThrow(() -> new RuntimeException("The property " + property + " does not exist on bean"));
//    }
}
