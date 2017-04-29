package io.threesixty.ui.config;

import io.threesixty.ui.component.logo.Logo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
@Configuration
public class ThreeSixtyComponensConfiguration {
    @Bean
    public Logo getLogo(@Value("${threesixty.application.logo:<span class=\"v-icon v-icon-specialist\" style=\"font-family: Vaadin-Icons;\">&#xe617;</span>}") String logo,
                        @Value("${threesixty.application.title:Application}") String title) {
        return new Logo(logo, title);
    }
}
