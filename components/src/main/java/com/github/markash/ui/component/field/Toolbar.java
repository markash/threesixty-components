package com.github.markash.ui.component.field;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;

/**
 * A toolbar
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public interface Toolbar extends HasComponents {

    /**
     * Set the caption of the toolbar
     * @param value The value of the caption
     */
    void setCaption(
            final String value);

    /**
     * Set the filter component used by the toolbar
     * @param filter The filter field component
     */
    void setFilter(
            final FilterField<?> filter);
    /**
     * Returns the filter field
     * @return The field field
     */
    FilterField<?> getFilter();
    /**
     * Add a button to the action section of the toolbar
     * @param button The button to add
     */
    @SuppressWarnings("unused")
    void addAction(
            final Button button);

    /**
     * Add a component to the specified section of the toolbar
     * @param component The component to add
     * @param section The section to add the component to
     */
    void add(
            final Component component,
            final ToolbarSection section);

    /**
     * Removes all the components from the toolbar
     */
    void removeAll();
    /**
     *            |--------------------------------|------------------------|
     *  HEADER    |    CAPTION                     |        ACTION          |
     *            |--------------------------------|------------------------|
     *  GUTTER    |                                                         |
     *            |---------------------------------------------------------|
     */
    enum ToolbarSection {
        HEADER,
        ACTION,
        GUTTER
    }
}
