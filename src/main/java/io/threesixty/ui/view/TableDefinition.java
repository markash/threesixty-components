package io.threesixty.ui.view;

import com.vaadin.server.SerializablePredicate;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @param <T> The type of the entity class of the table
 */
public class TableDefinition<T> {
    String DEFAULT_LINK_PROPERTY_ID = "id";
    String DEFAULT_LINK_PROPERTY_TEXT = "Id";

    private Optional<String> entityViewName;
    private List<ColumnDefinition<T, ?>> columns = new ArrayList<>();

    public TableDefinition() {
    }

    public TableDefinition(List<ColumnDefinition<T, ?>> columns) {
        this((String) null, columns);
    }

    public TableDefinition(final String entityViewName, final List<ColumnDefinition<T, ?>> columns) {
        this.entityViewName = Optional.ofNullable(entityViewName);
        this.columns = columns;
    }

    public List<ColumnDefinition<T, ?>> getColumns() { return columns; }
    public void setColumns(final List<ColumnDefinition<T, ?>> columns) { this.columns.addAll(columns); }

    public TableDefinition<T> column(final ColumnDefinition<T, ?> columnDefinition) {
        this.columns.add(columnDefinition);
        return this;
    }

    public <FIELD> ColumnDefinition<T, FIELD> withColumn(final Class<FIELD> fieldType) {
        ColumnDefinition<T, FIELD> columnDefinition = new ColumnDefinition<>();
        this.columns.add(columnDefinition);
        return columnDefinition;
    }

    /**
	 * The name of the id property column
	 * @return The property name of the id column
	 */
	public String getIdPropertyName() { return DEFAULT_LINK_PROPERTY_ID; }
    /**
     * The name of the id property column
     * @return The property name of the id column
     */
    public String getIdPropertyText() { return DEFAULT_LINK_PROPERTY_TEXT; }
	/**
	 * The array of object property names in display order
	 * @return The property names
	 */
	public String[] getPropertyNames() {
	    return this.getColumns()
                .stream()
                .map(ColumnDefinition::getProperty)
                .collect(Collectors.toList())
                .toArray(new String[this.getColumns().size()]);
    }
	/**
	 * The array of object properties that should be included in the filter
	 * @return The array of properties
	 */
	public String[] getFilterProperties() {
        return this.getColumns()
                .stream()
                .filter(ColumnDefinition::isSearchable)
                .map(ColumnDefinition::getProperty)
                .collect(Collectors.toList())
                .toArray(new String[this.getColumns().size()]);
    }
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
     * Get the view name that should be navigated to when the user clicks the id column
     * @return The view name of the entity that represents the row
     */
	public Optional<String> getEntityViewName() { return entityViewName; }

	public void setFilter(SerializablePredicate<T> filter) {}
}
