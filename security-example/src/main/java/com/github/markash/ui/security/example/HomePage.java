package com.github.markash.ui.security.example;

import com.github.markash.ui.component.menu.annotation.MenuItem;
import com.github.markash.ui.component.menu.annotation.VaadinFontIcon;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@MenuItem(sectionId = Sections.DEFAULT, caption = HomePage.TITLE, order = 2)
@VaadinFontIcon(VaadinIcons.HOME)
@SpringView(name = HomePage.VIEW_NAME)
public class HomePage extends VerticalLayout implements View {
    private static final long serialVersionUID = 1L;

    static final String TITLE = "Operations";
    static final String VIEW_NAME = "operations";

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Label title = new Label();

        title.setCaption("Home");
        title.setValue("Home view");

        addComponent(title);
    }
}
