package io.threesixty.ui.component.card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class ApplicationTitle {
    private String logo;
    private String title;

    @Autowired
    public ApplicationTitle(@Value("${threesixty.application.logo}") final String logo,
                            @Value("${threesixty.application.title}") final String title) {
        this.title = title;
        this.logo = logo;
    }

    public String getLogo() { return logo; }
    public String getTitle() { return title; }
}
