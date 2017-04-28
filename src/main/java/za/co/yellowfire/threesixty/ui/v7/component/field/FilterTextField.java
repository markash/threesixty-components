package za.co.yellowfire.threesixty.ui.v7.component.field;

import com.vaadin.event.ShortcutListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Container.Filterable;
import com.vaadin.v7.data.Item;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.viritin.v7.fields.MTable;

public class FilterTextField<T> extends TextField {
	private static final long serialVersionUID = 1L;

	private final MTable<T> table;
	private final PropertyTextFilter filter;
	
	public FilterTextField(final MTable<T> table, final String[] propertiesToFilterOn) {
		super();
		
		this.table = table;
		filter = new PropertyTextFilter(propertiesToFilterOn);
		
        addValueChangeListener(this::onFilter);

        setCaption("Filter");
        setIcon(VaadinIcons.SEARCH);
        addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        addShortcutListener(new FilterShortcutListener<>(this));
	}
	
	private void onFilter(final ValueChangeEvent event) {
		filter.setFilter((String) event.getValue());
		
		Filterable data = (Filterable) table.getContainerDataSource();
        data.removeContainerFilter(filter);
        data.addContainerFilter(filter);
	}
		
	@SuppressWarnings("serial")
	public static class PropertyTextFilter implements Container.Filter {
		
		private String filter = null;
		private String[] properties;
		
		PropertyTextFilter(final String[] properties) {
			this.properties = properties;
		}
		
		void setFilter(final String filter) { this.filter = filter; }
		
    	public boolean passesFilter(
    			final Object itemId,
                final Item item) {

            if (!StringUtils.isNoneBlank(this.filter)) {
                return true;
            }

            boolean passes = false;
            for(String property : properties) {
            	passes = passes || filterByProperty(property, item, this.filter);
            }
            return passes;
        }

        @Override
        public boolean appliesToProperty(final Object propertyId) {
        	boolean applies = false;
        	for(String property : properties) {
        		applies = applies || propertyId.equals(property);
        	}
            return applies;
        }
        
        private boolean filterByProperty(final String prop, final Item item,
                final String text) {
            if (item == null || item.getItemProperty(prop) == null
                    || item.getItemProperty(prop).getValue() == null) {
                return false;
            }
            String val = item.getItemProperty(prop).getValue().toString().trim()
                    .toLowerCase();
            return val.contains(text.toLowerCase().trim());
        }
    }
	
	@SuppressWarnings("serial")
	private static class FilterShortcutListener<T> extends ShortcutListener {
		private final FilterTextField<T> textField;
		
		FilterShortcutListener(final FilterTextField<T> textField) {
			super("Clear", KeyCode.ESCAPE, null);
			this.textField = textField;
		}
		
    	@Override
        public void handleAction(final Object sender, final Object target) {
            textField.setValue("");
            ((Filterable) textField.table.getContainerDataSource()).removeAllContainerFilters();
        }
    }
}
