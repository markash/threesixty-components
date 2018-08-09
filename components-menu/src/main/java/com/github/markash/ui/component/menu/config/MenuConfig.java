package com.github.markash.ui.component.menu.config;

import com.github.markash.ui.component.i18n.I18N;
import com.github.markash.ui.component.logo.Logo;
import com.github.markash.ui.component.menu.MenuUtils;
import com.github.markash.ui.component.menu.ThreeSixtyHybridMenu;
import com.github.markash.ui.component.menu.VaadinFontIconProvider;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Optional;

@Configuration
public class MenuConfig {

    @Autowired
    I18N i18n;

    @Autowired
    ApplicationContext applicationContext;


    private Optional<Layout> findDisplayView() {

        return applicationContext
                .getBeansWithAnnotation(SpringViewDisplay.class)
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .filter(v -> v instanceof Layout)
                .map(v -> (Layout) v)
                .findFirst();
    }

    private Logo findLogo() {
        return Optional
                .ofNullable(applicationContext.getBean(Logo.class))
                .orElse(new Logo("", "Application"));
    }

    @Bean
    @UIScope
    ThreeSixtyHybridMenu threeSixtyHybridMenu() {

        return new ThreeSixtyHybridMenu(
                menuUtils(),
                findLogo(),
                findDisplayView().orElse(new VerticalLayout()));
    }

    @Bean
    MenuUtils menuUtils() {

        return new MenuUtils(applicationContext, i18n);
    }

    @Bean
    VaadinFontIconProvider vaadinFontIconProvider() {

        return new VaadinFontIconProvider();
    }
}
