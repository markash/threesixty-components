package com.github.markash.ui.component;

import com.github.markash.ui.view.ColumnDefinition;
import com.github.markash.ui.view.TableDefinition;
import com.vaadin.data.Binder;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.Renderer;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.viritin.grid.MGrid;

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

    @SuppressWarnings("unchecked")
    public EntityGrid<T> withDefinition(final TableDefinition<T> definition) {
        Column<T, ?> column;
        Binder<T> binder = getEditor().getBinder();

        /* Remove the columns added by the constructor when evaluating the beanType */
        removeAllColumns();

        /* Redefine the columns based upon the definition */
        for(ColumnDefinition<T> c : definition.getColumns()) {
            if (c.isId()) {
                column = addColumn(row -> buildNavigationLink(row, c, definition.getEntityViewName().orElse("error"), definition.getLinkPrefix()), new HtmlRenderer());
                column.setCaption(c.getHeading());

                /* Add a hidden column for the display */
                if (c.getDisplay() != null) {
                    column = addColumn(c.getDisplay().getProvider());
                    column.setHidden(true);
                }
            } else {
                column = addColumn(c.getProperty().getProvider());
                column.setCaption(c.getHeading());

                Optional<Binder.Binding<T, ?>> binding = Optional.ofNullable(c.getProperty().bind(binder));
                if (binding.isPresent()) {
                    column.setEditorBinding(binding.get());
                    getEditor().setEnabled(true);
                }
            }

            /* Set the renderer is defined */
            if (c.getRenderer().isPresent()) {
                column.setRenderer((Renderer<Object>) c.getRenderer().get());
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
     * @param bean The row object to read the property link value from
     * @param columnDefinition The definition of the column
     * @param entityViewName The name of the view of the entity
     * @param linkPrefix The link prefix to use which differs when using Vaadin Push or Vaadin Classic URLS
     * @return The navigation link
     */
    private String buildNavigationLink(
            final T bean,
            final ColumnDefinition<T> columnDefinition,
            final String entityViewName,
            final String linkPrefix) {

        return "<a href='" +
                linkPrefix +
                buildNavigationState(entityViewName, columnDefinition.renderProperty(bean)) +
                "' target='_top'>" +
                columnDefinition.renderDisplay(bean) + "</a>";
    }
//    /**
//     * Builds the navigation link for the table column that is used to drill down into a single instance of the row
//     * @param value The row object to read the property link value from
//     * @return The navigation link
//     */
//    private String buildNavigationLink(
//            final Object value,
//            final String property,
//            final String entityViewName) {
//        final String linkValue = getPropertyValue(value, property);
//        return "<a href='" + buildNavigationState(entityViewName, linkValue) + "' target='_top'>" + linkValue + "</a>";
//    }
    /**
     * Builds the navigation state for the entity view path, i.e. !#entityViewName/id
     * @param entityViewName The entity view name
     * @param id The id value of the table that was clicked
     * @return The entity view path
     */
    private String buildNavigationState(
            final String entityViewName,
            final String id) {

        return entityViewName + (id != null && StringUtils.isBlank(id) ? "" : "/" + id);
    }
//    /**
//     * Returns the property (i.e. column) value that should be used for the link of getLinkPropertyId()
//     * @param value The object to read the link value from
//     * @return The link value from the object or `Unavailable exception` if an IllegalArgumentException || IllegalAccessException is thrown
//     */
//    @SuppressWarnings({"rawtypes", "unchecked"})
//    private String getPropertyValue(
//            final Object value,
//            final ColumnDefinition columnDefinition) {
//
//        return columnDefinition.hasDisplaySetting() ? columnDefinition.getProperty().render(value) : null;
//    }
//    /**
//     * Returns the property (i.e. column) value that should be used for the link of getLinkPropertyId()
//     * @param value The object to read the link value from
//     * @return The link value from the object or `Unavailable exception` if an IllegalArgumentException || IllegalAccessException is thrown
//     */
//    private String getPropertyValue(final Object value, final String property) {
//        PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(getBeanType(), property);
//        try {
//            return Optional.ofNullable(descriptor.getReadMethod().invoke(value)).orElse("").toString();
//        } catch (Exception e) {
//            return "Unavailable exception";
//        }
//    }
}
