package com.github.markash.ui.view;

import com.vaadin.data.BeanPropertySet;
import com.vaadin.data.PropertySet;
import com.vaadin.server.SerializablePredicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @param <T> The type of the entity class of the table
 */
public class TableDefinition<T> {
    private static final String URL_PREFIX_CLASSIC = "#!";
    private static final String URL_PREFIX_PUSH = "";
//    private static String DEFAULT_LINK_PROPERTY_ID = "id";
//    private static String DEFAULT_LINK_PROPERTY_TEXT = "Id";

//    private String linkPropertyId = DEFAULT_LINK_PROPERTY_ID;
//    private String linkPropertyText = DEFAULT_LINK_PROPERTY_TEXT;
    private String entityViewName;
    private List<ColumnDefinition<T>> columns = new ArrayList<>();
    private String linkPrefix = URL_PREFIX_CLASSIC;

    private final Class<T> entity;
    private final PropertySet<T> propertySet;

    public TableDefinition(
            final Class<T> entity,
            final String entityViewName) {

        this.entity = entity;
        this.propertySet = BeanPropertySet.get(entity);
        this.entityViewName = entityViewName;
    }

//    /**
//     * @deprecated
//     * @param columns The columns of the table definition
//     */
//    public TableDefinition(final List<ColumnDefinition<T, ?>> columns) {
//        this(null, columns);
//    }
//
//    /**
//     * @deprecated
//     * @param entityViewName The name of the entity view
//     * @param columns The columns of the table definition
//     */
//    public TableDefinition(final String entityViewName, final List<ColumnDefinition<T, ?>> columns) {
//        this.entityViewName = entityViewName;
//        this.columns = columns;
//        this.propertySet = null;
//    }

    @SuppressWarnings("unused")
    public static <BEAN> TableDefinition<BEAN> forEntity(
            final Class<BEAN> entity,
            final String entityViewName) {

        return new TableDefinition<>(entity, entityViewName);
    }

    public List<ColumnDefinition<T>> getColumns() { return columns; }
//    public void setColumns(final List<ColumnDefinition<T, ?>> columns) { this.columns.addAll(columns); }
    PropertySet<T> getPropertySet() { return propertySet; }

    //    public TableDefinition<T> withColumn(final ColumnDefinition<T, ?> columnDefinition) {
//        this.columns.add(columnDefinition);
//        return this;
//    }
//
//    public TableDefinition<T> withColumns(final List<ColumnDefinition<T, ?>> columnDefinition) {
//        this.columns.addAll(columnDefinition);
//        return this;
//    }
//
//    @SafeVarargs
//    public final TableDefinition<T> withColumns(final ColumnDefinition<T, ?>...columnDefinition) {
//        this.columns.addAll(Arrays.asList(columnDefinition));
//        return this;
//    }

    @SuppressWarnings("unused")
    public final TableDefinition<T> withClassicUrls() {
        this.linkPrefix = URL_PREFIX_CLASSIC;
        return this;
    }

    @SuppressWarnings("unused")
    public final TableDefinition<T> withPushUrls() {
        this.linkPrefix = URL_PREFIX_PUSH;
        return this;
    }

    public ColumnDefinition<T> column() {
        return column(false);
    }

    public ColumnDefinition<T> column(
            final boolean isIdentity) {

        ColumnDefinition<T> columnDefinition = new ColumnDefinition<>(this);

        if (isIdentity) {
            columnDefinition.identity();
        }

        this.columns.add(columnDefinition);
        return columnDefinition;
    }
//    /**
//	 * The name of the id property column
//	 * @return The property name of the id column
//	 */
//	public String getIdPropertyName() { return this.linkPropertyId; }
//    /**
//     * The name of the id property column
//     * @return The property name of the id column
//     */
//    public String getIdPropertyText() { return this.linkPropertyText; }
//	/**
//	 * The array of object property names in display order
//	 * @return The property names
//	 */
//	public String[] getPropertyNames() {
//	    return this.getColumns()
//                .stream()
//                .map(ColumnDefinition::getProperty)
//                .collect(Collectors.toList())
//                .toArray(new String[this.getColumns().size()]);
//    }
    public Stream<ColumnDefinition<T>> getFilterableColumns() {
        return this.getColumns()
                .stream()
                .filter(ColumnDefinition::isSearchable);
    }
    public String getLinkPrefix() {
        return linkPrefix;
    }
//    /**
//	 * The array of object properties that should be included in the filter
//	 * @return The array of properties
//	 */
//	String[] getFilterProperties() {
//        return this.getColumns()
//                .stream()
//                .filter(ColumnDefinition::isSearchable)
//                .map(ColumnDefinition::getProperty)
//                .collect(Collectors.toList())
//                .toArray(new String[this.getColumns().size()]);
//    }
	/**
	 * The array of headers in display order
	 * @return The headers
	 */
	public String[] getHeaders() {
        return this.getColumns()
                .stream()
                .map(ColumnDefinition::getHeading)
                .collect(Collectors.toList())
                .toArray(new String[this.getColumns().size()]);
    }

    /**
     * Get the entity class of the table definition
     * @return The entity class
     */
    public Class<T> getEntity() { return entity; }
    /**
     * Get the view name that should be navigated to when the user clicks the id column
     * @return The view name of the entity that represents the row
     */
	public Optional<String> getEntityViewName() { return Optional.ofNullable(entityViewName); }

	@SuppressWarnings("unused")
	public void setFilter(SerializablePredicate<T> filter) {}
}
