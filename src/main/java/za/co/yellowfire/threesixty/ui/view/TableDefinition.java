package za.co.yellowfire.threesixty.ui.view;

public interface TableDefinition {
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
}
