package io.threesixty.ui.component.field;

import com.vaadin.event.EventRouter;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.themes.ValoTheme;
import io.threesixty.ui.I8n;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.*;

@SuppressWarnings("unused")
public class ActiveFiltersField extends CustomField<ActiveFiltersField.ActiveFiltersModel> {

    private ActiveFiltersModel filters = new ActiveFiltersModel();
    private MHorizontalLayout layout = new MHorizontalLayout();
    private MButton clearAllButton = new MButton(I8n.Button.CLEAR_ALL_FILTERS, this::onClearAll).withStyleName(ValoTheme.BUTTON_LINK);
    private EventRouter eventRouter;

    public Registration addFilterChangeListener(
            final FilterChangeListener listener) {

        return getEventRouter()
                .addListener(
                        FilterChangeEvent.class,
                        listener,
                        FilterChangeListener.class.getDeclaredMethods()[0]);
    }

    @Override
    public ActiveFiltersModel getValue() {

        return this.filters;
    }

    @Override
    protected Component initContent() {

        return new MHorizontalLayout()
                .withMargin(false)
                .with(
                        new MLabel("Active Filters: ").withStyleName("active-filters-text"),
                        layout,
                        clearAllButton
                );
    }

    @Override
    protected void doSetValue(
            final ActiveFiltersModel activeFilterModel) {

        this.filters = activeFilterModel;
    }

    public void onFilterChange(
            final FilterChangeEvent event) {

        if (event.getAction() == FilterChangeEvent.FilterAction.ADD) {

            addFilter(event.getFilter());

        } else if (event.getAction() == FilterChangeEvent.FilterAction.CLEAR) {

            removeFilter(event.getFilter());
            getEventRouter().fireEvent(event);

        } else if (event.getAction() == FilterChangeEvent.FilterAction.CLEAR_ALL) {

            removeFilters();
        }
    }

    private void onClearAll(
            final Button.ClickEvent event) {

        removeFilters();
        getEventRouter().fireEvent(FilterChangeEvent.CLEAR_ALL(this));
    }

    private void removeFilters() {

        Set<FilterModel> filterModels = this.filters.filters.keySet();
        removeFilter(filterModels.toArray(new FilterModel[filterModels.size()]));
    }

    private void removeFilter(
            final FilterModel[] filters) {

        Arrays.stream(filters).forEach(this::removeFilter);
    }

    private void removeFilter(
            final FilterModel filter) {

        Optional<ActiveFilterReference> reference = this.filters.remove(filter);
        if (reference.isPresent()) {
            this.layout.removeComponent(reference.get().getComponent());
            reference.get().getRegistration().remove();
        }
    }

    private void addFilter(
            final FilterModel filter) {

        ActiveFilterField filterField = new ActiveFilterField(filter);
        Registration registration = filterField.addFilterChangeListener(this::onFilterChange);
        this.filters.add(filter, filterField, registration);
        this.layout.add(filterField);
    }

    private EventRouter getEventRouter() {

        if (eventRouter == null) {
            eventRouter = new EventRouter();
        }
        return eventRouter;
    }

    public static class ActiveFiltersModel {
        Map<FilterModel, ActiveFilterReference> filters = new HashMap<>();

        public void add(
                final FilterModel filter,
                final ActiveFilterField filterField,
                final Registration registration) {

            this.filters.put(filter, new ActiveFilterReference(filterField, registration));
        }

        public Optional<ActiveFilterReference> remove(
                final FilterModel filter) {

            return this.remove(
                    filter.getHeader(),
                    filter.getProperty(),
                    filter.getValue());
        }

        public Optional<ActiveFilterReference> remove(
                final String header,
                final String property,
                final String value) {

             Optional<Map.Entry<FilterModel, ActiveFilterReference>> item =
                     this.filters.entrySet().stream()
                             .filter(entry -> entry.getKey().getProperty().equals(property) && entry.getKey().getValue().equals(value))
                             .findFirst();

             item.ifPresent(i -> this.filters.remove(i.getKey()));
             return item.map(Map.Entry::getValue);
        }
    }

    static class ActiveFilterReference {
        private Component component;
        private Registration registration;

        ActiveFilterReference(
                final Component component,
                final Registration registration) {

            this.component = component;
            this.registration = registration;
        }

        Component getComponent() { return component; }

        Registration getRegistration() { return registration; }
    }
}
