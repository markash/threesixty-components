package io.threesixty.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringViewDisplay
public class DisplayView extends VerticalLayout implements ViewDisplay {

    public DisplayView() {
        setStyleName(ValoTheme.PANEL_BORDERLESS);
    }

    @Override
    public void showView(
            final View view) {

        this.removeAllComponents();
        this.addComponent(view.getViewComponent());
    }
}