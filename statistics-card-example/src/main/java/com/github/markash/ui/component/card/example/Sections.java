package com.github.markash.ui.component.card.example;

import org.springframework.stereotype.Component;
import org.vaadin.spring.sidebar.annotation.SideBarSection;
import org.vaadin.spring.sidebar.annotation.SideBarSections;

@SideBarSections({
        @SideBarSection(id = Sections.DEFAULT, order=1),
})
@Component
public class Sections {
	public static final String DEFAULT = "default";
}
