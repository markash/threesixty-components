package io.threesixty.ui.component;

import com.vaadin.data.Binder;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.ui.renderers.HtmlRenderer;
import io.threesixty.ui.view.ColumnDefinition;
import io.threesixty.ui.view.TableDefinition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.vaadin.viritin.grid.MGrid;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Optional;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public class EntityGrid<T> extends MGrid<T> {

    public EntityGrid(Class<T> beanType) {
        super(beanType);
    }

    public EntityGrid<T> withDataProvider(final DataProvider<T, ?> dataProvider) {
        setDataProvider(dataProvider);
        return this;
    }

    public EntityGrid<T> withDefinition(final TableDefinition<T> definition) {
        Column<T, ?> column;
        Binder<T> binder = getEditor().getBinder();

        /* Remove the columns added by the constructor when evaluating the beanType */
        removeAllColumns();

        /* Redefine the columns based upon the definition */
        for(ColumnDefinition<T, ?> c : definition.getColumns()) {
            if (c.isId()) {
                column = addColumn(row -> buildNavigationLink(row, c, definition.getEntityViewName().orElse("error")), new HtmlRenderer());
                column.setCaption(c.getHeading());
            } else {
                column = addColumn(c.getProperty());
                column.setCaption(c.getHeading());

                Optional<Binder.Binding<T, ?>> binding = Optional.ofNullable(c.bind(binder));
                if (binding.isPresent()) {
                    column.setEditorBinding(binding.get());
                    getEditor().setEnabled(true);
                }
            }
        }
        return this;
    }

    @Override
    public EntityGrid<T> withProperties(String... properties) {
        return (EntityGrid<T>) super.withProperties(properties);
    }

    @Override
    public EntityGrid<T> withColumnHeaders(String... properties) {
        return (EntityGrid<T>) super.withColumnHeaders(properties);
    }

    @Override
    public EntityGrid<T> setRows(List<T> rows) {
        return (EntityGrid<T>) super.setRows(rows);
    }
    /**
     * Builds the navigation link for the table column that is used to drill down into a single instance of the row
     * @param value The row object to read the property link value from
     * @return The navigation link
     */
    private String buildNavigationLink(final Object value, final ColumnDefinition columnDefinition, final String entityViewName) {

        String displayValue = null;
        if (StringUtils.isNotBlank(columnDefinition.getDisplayProperty())) {
            displayValue = getPropertyValue(value, columnDefinition.getDisplayProperty());
        }
        final String linkValue = getPropertyValue(value, columnDefinition.getProperty());
        return "<a href='#!" + buildNavigationState(entityViewName, linkValue) + "' target='_top'>" + Optional.ofNullable(displayValue).orElse(linkValue) + "</a>";
    }
    /**
     * Builds the navigation link for the table column that is used to drill down into a single instance of the row
     * @param value The row object to read the property link value from
     * @return The navigation link
     */
    private String buildNavigationLink(final Object value, final String property, final String entityViewName) {
        final String linkValue = getPropertyValue(value, property);
        return "<a href='#!" + buildNavigationState(entityViewName, linkValue) + "' target='_top'>" + linkValue + "</a>";
    }
    /**
     * Builds the navigation state for the entity view path, i.e. !#entityViewName/id
     * @param entityViewName The entity view name
     * @param id The id value of the table that was clicked
     * @return The entity view path
     */
    private String buildNavigationState(final String entityViewName, final String id) {
        return entityViewName + (id != null && StringUtils.isBlank(id) ? "" : "/" + id);
    }
    /**
     * Returns the property (i.e. column) value that should be used for the link of getLinkPropertyId()
     * @param value The object to read the link value from
     * @return The link value from the object or `Unavailable exception` if an IllegalArgumentException || IllegalAccessException is thrown
     */
    private String getPropertyValue(final Object value, final String property) {
        PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(getBeanType(), property);
        try {
            return Optional.ofNullable(descriptor.getReadMethod().invoke(value)).orElse("").toString();
        } catch (Exception e) {
            return "Unavailable exception";
        }
    }
}
