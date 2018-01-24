package io.threesixty.ui.component.field;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

public class Toolbar extends CustomComponent {
    private static final int ORDER_CAPTION = 0;
    private static final int ORDER_FILTER = 1;
    private static final int ORDER_ACTION = 2;

    private MLabel caption;
    private MHorizontalLayout header;
    private MHorizontalLayout gutter;
    private MHorizontalLayout action;
    private FilterField<?> filter;
    private ActiveFiltersField activeFilter = new ActiveFiltersField();

    public Toolbar(
            final String caption) {
        super(new MVerticalLayout());

        setStyleName("toolbar");

        this.caption = new MLabel(caption)
                .withUndefinedSize()
                .withStyleName(ValoTheme.LABEL_H2, ValoTheme.LABEL_NO_MARGIN, "toolbar-caption");

        this.action = new MHorizontalLayout()
                .withStyleName("toolbar-action")
                .withSpacing(true)
                .withMargin(false)
                .with(new MLabel(""));

        this.header = new MHorizontalLayout()
                .withStyleName("toolbar-header")
                .withSpacing(true)
                .withMargin(false)
                .with(this.caption, this.action);

        this.gutter = new MHorizontalLayout()
                .withStyleName("toolbar-gutter")
                .withSpacing(true)
                .withMargin(false)
                ;

        ((MVerticalLayout) getCompositionRoot())
                .withSpacing(true)
                .withMargin(false)
                .with(header, gutter);
    }

    public void setFilter(
            final FilterField<?> filter) {

        this.filter = filter;
        this.filter.addFilterChangeListener(activeFilter::onFilterChange);
        this.activeFilter.addFilterChangeListener(filter::onFilterChange);

        if (this.filter.hasFilterDefinitions()) {
            this.header.addComponent(filter, ORDER_FILTER);
            this.gutter.addComponentAsFirst(activeFilter);
        }
    }

    public void addAction(
            final Button button) {

        add(button, ToolbarSection.ACTION);
    }

    public void add(
            final Component component,
            final ToolbarSection section) {

        switch (section) {
            case HEADER:
                this.header.add(component);
                break;
            case ACTION:
                this.action.add(component);
                break;
            case GUTTER:
                this.gutter.add(component);
                break;
        }
    }

    public enum ToolbarSection {
        HEADER,
        ACTION,
        GUTTER
    }
}


