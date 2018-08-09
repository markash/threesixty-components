package com.github.markash.ui.security.example;

import com.github.markash.ui.component.menu.annotation.MenuSection;
import com.github.markash.ui.component.menu.annotation.MenuSections;
import org.springframework.stereotype.Component;

@MenuSections({
        @MenuSection(id = Sections.DEFAULT, order=1),
        @MenuSection(id = Sections.SETTINGS, caption = "Settings", order=2),
})
@Component
public class Sections {
	public static final String DEFAULT = "default";
	public static final String SETTINGS = "settings";
}
