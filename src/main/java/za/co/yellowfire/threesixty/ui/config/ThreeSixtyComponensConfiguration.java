package za.co.yellowfire.threesixty.ui.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import za.co.yellowfire.threesixty.ui.component.logo.Logo;

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
