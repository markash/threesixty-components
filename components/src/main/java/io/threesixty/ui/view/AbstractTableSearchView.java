package io.threesixty.ui.view;

import com.vaadin.data.Binder;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import io.threesixty.ui.I8n;
import io.threesixty.ui.component.EntityGrid;
import io.threesixty.ui.component.field.FilterField;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.viritin.button.MButton;

import java.io.Serializable;

/**
 * Abstract grid search view
 * @author Mark P Ashworth
 * @version 0.2.0
 */
@SuppressWarnings("unused")
public abstract class AbstractTableSearchView<T extends Serializable, I extends Serializable> extends AbstractDashboardView {
	private static final long serialVersionUID = 1L;

	protected static final String BUTTON_OK = "Save";
	protected static final String BUTTON_RESET = "Reset";
	protected static final String BUTTON_DELETE = "Delete";
	protected static final String BUTTON_ADD = "New";
	protected static final String BUTTON_VIEW = "View";

    private EntityGrid<T> grid;
    //private HeaderButtons headerButtons;
    //private ExtendedFilterTextField<T> filters;
    //private FilterField<T> filter;

    /**
	 * Constructs a search view for a class that is provided by an EntityProvider and that
	 * can be filtered on by an array of properties.
	 * 
	 * @param beanType The class of the entity class bean
	 * @param viewCaption The caption of the view
	 * @param dataProvider The data provider for the class
	 * @param definition Table definition (Not used as yet)
     * @param headerComponents The header components like buttons, search filters, etc
	 */
	protected AbstractTableSearchView(
			final Class<T> beanType,
			final String viewCaption,
			final ListDataProvider<T> dataProvider,
			final TableDefinition<T> definition,
			final Component...headerComponents) {
		
		super(viewCaption);

		this.grid = new EntityGrid<>(beanType).withDataProvider(dataProvider).withDefinition(definition);
        getToolbar().setFilter(new FilterField<>(dataProvider, definition));
        getToolbar().addAction(new MButton(I8n.Button.NEW, this::onCreate));
	}
	
	public abstract void onCreate(ClickEvent event);

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
		VerticalLayout root = new VerticalLayout();
        root.setSpacing(true);
        root.setMargin(false);
        Responsive.makeResponsive(root);
        
        this.grid.setWidth(100f, Unit.PERCENTAGE);
        root.addComponent(this.grid);
        
        return root;
    }

    protected Binder<T> getTableBinder() {
        return this.grid.getEditor().getBinder();
    }

	protected Grid<T> getGrid() { return this.grid; }
}
