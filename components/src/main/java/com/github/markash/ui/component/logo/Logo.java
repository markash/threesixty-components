package com.github.markash.ui.component.logo;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Mark P Ashworth
 */
public class Logo extends HorizontalLayout {

    private final String logo;
    private final String title;

    @Autowired
    public Logo(@Value("${threesixty.application.logo}") final String logo,
                @Value("${threesixty.application.title}") final String title) {

        this.logo = logo;
        this.title = title;

        setWidth("100%");

        Label logoField = new Label(logo, ContentMode.HTML);
        Label nameField = new Label("&nbsp;&nbsp;" + title, ContentMode.HTML);

        addComponent(logoField);
        addComponent(nameField);

        setComponentAlignment(logoField, Alignment.TOP_CENTER);
        addStyleName("valo-menu-title");
    }

    public String getLogo() {
        return logo;
    }

    public String getTitle() {
        return title;
    }
}
