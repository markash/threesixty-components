package za.co.yellowfire.threesixty.ui.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.viritin.v7.SortableLazyList;
import org.vaadin.viritin.v7.fields.FilterableTable;
import org.vaadin.viritin.v7.fields.MTable;

import com.vaadin.v7.data.Container.Filter;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import za.co.yellowfire.threesixty.ui.component.EntitiesSupplierPagingProvider;
import za.co.yellowfire.threesixty.ui.component.button.ButtonBuilder;
import za.co.yellowfire.threesixty.ui.component.button.HeaderButtons;
import za.co.yellowfire.threesixty.ui.component.field.FilterTextField;

/**
 * Abstract table search view
 * @author Mark P Ashworth
 * @version 0.1.0
 */
public abstract class AbstractTableSearchView<T extends Serializable, I extends Serializable> extends AbstractDashboardView {
	private static final long serialVersionUID = 1L;

	protected static final String BUTTON_OK = "Save";
	protected static final String BUTTON_RESET = "Reset";
	protected static final String BUTTON_DELETE = "Delete";
	protected static final String BUTTON_ADD = "New";
	protected static final String BUTTON_VIEW = "View";
	protected static final String DEFAULT_LINK_PROPERTY_ID = "id";
	protected static final String DEFAULT_LINK_PROPERTY_TEXT = "id";
	
	private final Class<T> beanType;
    private EntitiesSupplierPagingProvider<T> entityProvider;
    private MTable<T> table;
    private List<Component> headerComponents;
    private HeaderButtons headerButtons;
    private Optional<String> entityViewName;
    private Button newButton = ButtonBuilder.NEW(this::onCreate);
    
    private TableDefinition definition;
    /**
	 * Constructs a search view for a class that is provided by an EntityProvider and that
	 * can be filtered on by an array of properties.
	 * 
	 * @param beanType The class of the entity class bean
	 * @param viewCaption The caption of the view
     * @param entityViewName The name of the entity view that should be navigated to on id click
	 * @param entityProvider The entity provider for the class
	 * @param definition Table definition (Not used as yet)
     * @param headerComponents The header components like buttons, search filters, etc
	 */
	protected AbstractTableSearchView(
			final Class<T> beanType,
			final String viewCaption,
			final String entityViewName,
			final EntitiesSupplierPagingProvider<T> entityProvider,
			final TableDefinition definition,
			final Component...headerComponents) {
		
		super(viewCaption);
		
		this.beanType = beanType;
		this.entityProvider = entityProvider;
		this.entityViewName = Optional.of(entityViewName);
		this.definition = definition;
		this.table = buildTable();

		this.headerComponents = new ArrayList<>(Arrays.asList(headerComponents));
		this.headerComponents.add(0, newButton);
	}
	
	protected abstract void onCreate(ClickEvent event);
	protected List<Component> getTableButtons() { return this.headerComponents; }
	
	/**
	 * Returns the property (i.e. column) that should be made into a link to navigate to the detail and the actual id
	 * value is determined by the getLinkPropertyValue() property
	 * @return The table property name which is by default "id"
	 */
	protected String getLinkPropertyId() { return DEFAULT_LINK_PROPERTY_ID; }
	/**
	 * Returns the property that should be the href value of the link
	 * @return The table property name which is by default "id"
	 */
	protected String getLinkPropertyText() { return DEFAULT_LINK_PROPERTY_TEXT; }
	
	/**
	 * Builds the navigation state for the entity view path, i.e. !#entityViewName/id
	 * @param entityViewName The entity view name
	 * @param id The id value of the table that was clicked
	 * @return The entity view path
	 */
	protected String buildNavigationState(final String entityViewName, final I id) {
		return entityViewName + (id != null && StringUtils.isBlank(id.toString()) ? "" : "/" + id);
	}
	
	/**
	 * Provides the default implementation to navigate to the entity view specified in the entityViewName
	 * @param event The click event
	 * @param id The id value that was clicked
	 */
	protected void onTableIdClick(ClickEvent event, I id) {
		entityViewName.ifPresent(name -> UI.getCurrent().getNavigator().navigateTo(buildNavigationState(name, id)));
	}
	
	protected void navigateToView(I id) {
		entityViewName.ifPresent(name -> UI.getCurrent().getNavigator().navigateTo(buildNavigationState(name, id)));
	}
	
	@Override
	protected Component getHeaderButtons() {
		if (definition.getFilterProperties() != null) {
			this.headerButtons = new HeaderButtons(HeaderButtons.combine(new FilterTextField<T>(this.table, definition.getFilterProperties()), getTableButtons()));
		} else {
			this.headerButtons = new HeaderButtons(getTableButtons());
		}
		return this.headerButtons;
	}
	
	protected void addHeaderComponent(final Component component) {
		this.headerButtons.addComponent(component, 1);
	}
	
	protected Component buildContent() {
		VerticalLayout root = new VerticalLayout();
        root.setSpacing(true);
        root.setMargin(false);
        Responsive.makeResponsive(root);
        
        this.table.setWidth(100f, Unit.PERCENTAGE);
        root.addComponent(this.table);
        
        return root;
    }
	
	@SuppressWarnings({"unchecked", "serial"})
	private MTable<T> buildTable() {
			
		MTable<T> table = new FilterableTable<T>(beanType)
				.setBeans(new SortableLazyList<T>(entityProvider, entityProvider, 100))
                .withProperties(definition.getPropertyNames())
                .withColumnHeaders(definition.getHeaders())
                ;
		
		
		
		table.addGeneratedColumn(getLinkPropertyText(), new Table.ColumnGenerator() {	
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				Item x = source.getItem(itemId);
				
				Property<I> linkId = x.getItemProperty(getLinkPropertyId());
				Property<?> linkText = x.getItemProperty(getLinkPropertyText());
				Converter<String, Object> linkTextConverter = table.getConverter(getLinkPropertyText());
				
				String text;
				if (linkText.getValue() != null) {
					if (linkTextConverter != null) {
						text = linkTextConverter.convertToPresentation(linkText.getValue(), String.class, Locale.getDefault());
					} else {
						text = linkText.getValue().toString();
					}
				} else {
					text = "null";
				}
				
				Button button = new Button(text);
				button.setStyleName("link");
				button.addClickListener(event -> onTableIdClick(event, linkId.getValue()));
				return button;
			}
		});

		return table;
	}

	protected MTable<T> getTable() { return this.table; }
	protected void addFilter(final Filter filter) { ((FilterableTable<T>)this.table).addFilter(filter); }
}
