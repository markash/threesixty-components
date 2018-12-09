package com.github.markash.ui.view;

import com.github.markash.ui.component.EntityFilter;
import com.github.markash.ui.component.EntityGrid;
import com.github.markash.ui.component.field.FilterField;
import com.vaadin.data.Binder;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Abstract grid search view
 * @author Mark P Ashworth
 * @version 0.2.0
 */
@SuppressWarnings("unused")
public abstract class AbstractTableSearchView<T extends Serializable, I extends Serializable> extends AbstractDashboardView {
	private static final long serialVersionUID = 1L;

    private EntityGrid<T> grid;
	private FilterField<T> filterField;
	private final Class<T> beanType;
	/**
	 * Constructs a search view for a class that is provided by an EntityProvider and that
	 * can be filtered on by an array of properties.
	 *
	 * @param beanType The class of the entity class bean
	 * @param viewCaption The caption of the view
	 */
	protected AbstractTableSearchView(
			final Class<T> beanType,
			final String viewCaption) {
		super(viewCaption);

		this.beanType = beanType;
		this.grid = new EntityGrid<>(beanType);
		this.filterField = new FilterField<>();

		getToolbar().setFilter(this.filterField);
	}

    /**
	 * Constructs a search view for a class that is provided by an EntityProvider and that
	 * can be filtered on by an array of properties.
	 * 
	 * @param beanType The class of the entity class bean
	 * @param viewCaption The caption of the view
	 * @param dataProvider The data provider for the class
	 * @param definition Table definition
	 */
	protected AbstractTableSearchView(
			final Class<T> beanType,
			final String viewCaption,
			final ListDataProvider<T> dataProvider,
			final TableDefinition<T> definition) {
		
		super(viewCaption);

		this.beanType = beanType;
		this.grid = new EntityGrid<>(beanType).withDataProvider(dataProvider).withDefinition(definition);
		this.filterField = new FilterField<>();

		getToolbar().setFilter(this.filterField);
	}

	/**
	 * Builds the navigation state for the entity view path, i.e. !#entityViewName/id
	 * @param entityViewName The entity view name
	 * @param id The id value of the grid that was clicked
	 * @return The entity view path
	 */
	protected String buildNavigationState(final String entityViewName, final I id) {

		String entityId;
		if (id instanceof String) {
			entityId = (String) id;
		} else {
			entityId = id != null ? id.toString() : "";
		}

		return entityViewName + (entityId != null && StringUtils.isBlank(entityId) ? "" : "/" + entityId);
	}

	protected Component buildContent() {
		//VerticalLayout root = new VerticalLayout();
        //root.setSpacing(true);
        //root.setMargin(false);
        //Responsive.makeResponsive(root);
        
        //this.grid.setWidth(100f, Unit.PERCENTAGE);
        //root.addComponent(this.grid);

		this.grid.setWidth(100, Unit.PERCENTAGE);
		//addComponent(this.grid);

        return grid;
    }

    protected Binder<T> getTableBinder() {
        return this.grid.getEditor().getBinder();
    }

	protected Grid<T> getGrid() { return this.grid; }

	/**
	 * Get the data provider for the underlying grid
	 * @return The data provider
	 */
	protected DataProvider<T, ?> getDataProvider() {

		return this.grid.getDataProvider();
	}

	/**
	 * Return the class type of the domain bean that the grid will be displaying
	 * @return The class of the domain entity bean
	 */
	protected Class<T> getBeanType() { return this.beanType; }

	public AbstractTableSearchView<T, I> withDataProvider(
			final ListDataProvider<T> dataProvider) {

		this.grid.withDataProvider(dataProvider);
		this.filterField.withDataProvider(dataProvider);
		return this;
	}

	public AbstractTableSearchView<T, I> withDataProvider(
			final DataProvider<T, ? extends EntityFilter<T>> dataProvider) {

		this.grid.withDataProvider(dataProvider);
		//this.filterField.withDataProvider(dataProvider);
		return this;
	}

	public AbstractTableSearchView<T, I> withDefinition(
			final TableDefinition<T> definition) {

		this.grid.withDefinition(definition);
		this.filterField.withDefinition(definition);
		return this;
	}
}
