package com.github.markash.ui.config;

import com.vaadin.spring.annotation.SpringViewDisplay;
import com.github.markash.ui.component.logo.Logo;
import com.github.markash.ui.view.DisplayView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.annotation.PrototypeScope;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
@Configuration
public class ThreeSixtyComponentsConfiguration {
    @Bean @PrototypeScope
    public Logo getLogo(@Value("${threesixty.application.logo:<span class=\"v-icon v-icon-specialist\" style=\"font-family: Vaadin-Icons;\">&#xe617;</span>}") String logo,
                        @Value("${threesixty.application.title:Application}") String title) {
        return new Logo(logo, title);
    }

    @Bean
    @SpringViewDisplay
    public DisplayView displayView() {
        return new DisplayView();
    }
}
