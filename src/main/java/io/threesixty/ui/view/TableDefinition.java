package io.threesixty.ui.view;

import com.vaadin.server.SerializablePredicate;

import java.util.Optional;

public interface TableDefinition<T> {
    String DEFAULT_LINK_PROPERTY_ID = "id";
    String DEFAULT_LINK_PROPERTY_TEXT = "Id";
	/**
	 * The name of the id propery column
	 * @return The property name of the id column
	 */
	default String getIdPropertyName() { return DEFAULT_LINK_PROPERTY_ID; }
    /**
     * The name of the id property column
     * @return The property name of the id column
     */
    default String getIdPropertyText() { return DEFAULT_LINK_PROPERTY_TEXT; }
	/**
	 * The array of object property names in display order
	 * @return The property names
	 */
	String[] getPropertyNames();
	/**
	 * The array of object properties that should be included in the filter
	 * @return The array of properties
	 */
	String[] getFilterProperties();
	/**
	 * The array of headers in display order
	 * @return The headers
	 */
	String[] getHeaders();
    /**
     * Get the view name that should be navigated to when the user clicks the id column
     * @return The view name of the entity that represents the row
     */
	default String getEntityViewName() { return null; }

	void setFilter(SerializablePredicate<T> filter);
}
