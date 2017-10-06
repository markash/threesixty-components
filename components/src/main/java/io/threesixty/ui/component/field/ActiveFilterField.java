package io.threesixty.ui.component.field;

import com.vaadin.event.EventRouter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;

public class ActiveFilterField extends CustomField<FilterModel> {

    private FilterModel filter;
    private EventRouter eventRouter;

    public ActiveFilterField(
            final FilterModel filter) {

        this.filter = filter;
    }

    public Registration addFilterChangeListener(
            final FilterChangeListener listener) {

        return getEventRouter()
                .addListener(
                        FilterChangeEvent.class,
                        listener,
                        FilterChangeListener.class.getDeclaredMethods()[0]);
    }

    @Override
    protected Component initContent() {
        return new MHorizontalLayout()
                .withMargin(false)
                .withStyleName("filter-pill")
                .with(
                        new MLabel(getText()).withStyleName("filter-pill-text"),
                        new MButton(VaadinIcons.CLOSE_SMALL, this::onClear)
                                .withStyleName(ValoTheme.BUTTON_LINK, "filter-close-button")
                );
    }

    @Override
    protected void doSetValue(
            final FilterModel filterModel) {

        this.filter = filterModel;
    }

    @Override
    public FilterModel getValue() {

        return this.filter;
    }

    private void onClear(
            final Button.ClickEvent event) {

        getEventRouter()
                .fireEvent(
                        FilterChangeEvent.CLEAR(
                                this,
                                this.filter.getHeader(),
                                this.filter.getProperty(),
                                this.filter.getValue()));
    }

    private String getText() {

        return this.filter.getHeader() + ": " + this.filter.getValue();
    }

    private EventRouter getEventRouter() {
        if (eventRouter == null) {
            eventRouter = new EventRouter();
        }
        return eventRouter;
    }
}
