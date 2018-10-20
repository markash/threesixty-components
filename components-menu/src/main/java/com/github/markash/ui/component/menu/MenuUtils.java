/*
 * Copyright 2015 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.markash.ui.component.menu;

import com.github.markash.ui.component.i18n.I18N;
import com.github.markash.ui.component.menu.annotation.MenuItem;
import com.github.markash.ui.component.menu.annotation.MenuSection;
import com.github.markash.ui.component.menu.annotation.MenuSections;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility methods for working with side bars. This class is a Spring managed bean and is mainly
 * intended for internal use.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class MenuUtils {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ApplicationContext applicationContext;

    private final I18N i18n;

    private final List<MenuSectionDescriptor> sections = new ArrayList<>();

    private final List<MenuItemDescriptor> items = new ArrayList<>();

    public MenuUtils(
            final ApplicationContext applicationContext,
            final I18N i18n) {

        this.applicationContext = applicationContext;
        this.i18n = i18n;

        scanForSections();
        Collections.sort(sections);
        scanForItems();
        Collections.sort(items);
    }

    public <T> Optional<T> scanForToolbar(
            final Class<T> beanType) {

        logger.debug("Scanning for toolbar of type {}", beanType);

        Optional<T> toolbar = Optional.empty();
        try {
            toolbar = Optional.ofNullable(applicationContext.getBean(beanType));
            if (toolbar.isPresent()) {
                logger.debug("Bean[{}] found for beanType {}", toolbar.get(), beanType);
            } else {
                logger.debug("No bean found that implements {}", beanType);
            }
        } catch (BeansException exception) {
            logger.warn("Unable to locate bean instance for toolbar", exception);
        }

        return toolbar;
    }

    private void scanForSections() {
        logger.debug("Scanning for side bar sections");
        String[] beanNames = applicationContext.getBeanNamesForAnnotation(MenuSection.class);
        for (String beanName : beanNames) {
            logger.debug("Bean [{}] declares a side bar section", beanName);
            addSectionDescriptors(applicationContext.findAnnotationOnBean(beanName, MenuSection.class));
        }
        beanNames = applicationContext.getBeanNamesForAnnotation(MenuSections.class);
        for (String beanName : beanNames) {
            logger.debug("Bean [{}] declares multiple side bar sections", beanName);
            addSectionDescriptors(applicationContext.findAnnotationOnBean(beanName, MenuSections.class).value());
        }
    }

    private void addSectionDescriptors(
            final MenuSection... sections) {

        for (MenuSection section : sections) {
            logger.debug("Adding side bar section [{}]", section.id());
            this.sections.add(new MenuSectionDescriptor(section, i18n));
        }
    }

    private void scanForItems() {
        logger.debug("Scanning for side bar items");
        String[] beanNames = applicationContext.getBeanNamesForAnnotation(MenuItem.class);
        for (String beanName : beanNames) {
            logger.debug("Bean [{}] declares a side bar item", beanName);
            Class<?> beanType = applicationContext.getType(beanName);
            if (Runnable.class.isAssignableFrom(beanType)) {
                logger.debug("Adding side bar item for action [{}]", beanType);
                this.items.add(new MenuItemDescriptor.ActionItemDescriptor(beanName, applicationContext));
            } else if (View.class.isAssignableFrom(beanType) && beanType.isAnnotationPresent(SpringView.class)) {
                logger.debug("Adding side bar item for view [{}]", beanType);
                this.items.add(new MenuItemDescriptor.ViewItemDescriptor(beanName, applicationContext));
            }
        }
    }

    /**
     * Gets all side bar sections for the specified UI class.
     *
     * @param uiClass the UI class, must not be {@code null}.
     * @return a collection of side bar section descriptors, never {@code null}.
     * @see MenuSection#ui()
     */
    public Collection<MenuSectionDescriptor> getSections(
            final Class<? extends UI> uiClass) {

        List<MenuSectionDescriptor> supportedSections = new ArrayList<>();
        for (MenuSectionDescriptor section : sections) {
            if (section.isAvailableFor(uiClass)) {
                supportedSections.add(section);
            }
        }
        return supportedSections;
    }

    /**
     * Gets all side bar items for the specified side bar section.
     *
     * @param descriptor descriptor the side bar section descriptor, must not be {@code null}.
     * @return a collection of side bar item descriptors, never {@code null}.
     */
    public Collection<MenuItemDescriptor> getItems(
            final MenuSectionDescriptor descriptor) {

        List<MenuItemDescriptor> supportedItems = new ArrayList<>();
        for (MenuItemDescriptor item : items) {
            if (item.isMemberOfSection(descriptor)) {
                supportedItems.add(item);
            }
        }
        return supportedItems;
    }

    public Collection<MenuItemDescriptor> getTopMenuItems() {

        return items.stream().filter(MenuItemDescriptor::isTopMenu).collect(Collectors.toList());
    }
}