package io.threesixty.ui.spring.boot;

import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.boot.VaadinAutoConfiguration;
import io.threesixty.ui.spring.boot.annotation.EnableUserInterfaceComponents;
import io.threesixty.ui.view.DisplayView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.threesixty.ui.*;

@Configuration
@ConditionalOnClass({ApplicationUI.class})
public class ComponentsAutoConfiguration {
    private static Logger logger = LoggerFactory.getLogger(VaadinAutoConfiguration.class);

    @Configuration
    @EnableUserInterfaceComponents
    static class EnableUserInterfaceConfiguration implements InitializingBean {
        EnableUserInterfaceConfiguration() {
        }

        @Bean
        @SpringViewDisplay
        public DisplayView displayView() {
            return new DisplayView();
        }

        public void afterPropertiesSet() throws Exception {
            ComponentsAutoConfiguration.logger.debug("{} initialized", this.getClass().getName());
        }
    }
}
