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
import com.github.markash.ui.component.menu.annotation.MenuIcon;
import com.vaadin.server.Resource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;

/**
 * This is a class that describes a side bar item that has been declared using a {@link MenuItem} annotation.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public abstract class MenuItemDescriptor implements Comparable<MenuItemDescriptor> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MenuItemDescriptor.class);
    public static final String ITEM_ID_PREFIX = "menu_item_";

    private final MenuItem item;
    private final I18N i18n;
    private final ApplicationContext applicationContext;
    private final String beanName;
    private final Annotation iconAnnotation;
    private final MenuIconProvider<Annotation> iconProvider;

    protected MenuItemDescriptor(
            final String beanName,
            final ApplicationContext applicationContext) {

        this.item = applicationContext.findAnnotationOnBean(beanName, MenuItem.class);
        LOGGER.debug("Item annotation of bean [{}] is [{}]", beanName, item);

        this.i18n = applicationContext.getBean(I18N.class);
        this.applicationContext = applicationContext;
        this.beanName = beanName;
        this.iconAnnotation = findIconAnnotation();
        LOGGER.debug("Icon annotation of bean [{}] is [{}]", beanName, iconAnnotation);

        this.iconProvider = findIconProvider();
        LOGGER.debug("Icon provider of bean [{}] is [{}]", beanName, iconProvider);
    }

    /**
     * Attempts to find and return an annotation of the specified type declared on this side bar item.
     * @param <A> The annotation type
     * @param annotationType the type of the annotation to look for.
     * @return the annotation, or {@code null} if not found.
     */
    public <A extends Annotation> A findAnnotationOnBean(
            final Class<A> annotationType) {

        return applicationContext.findAnnotationOnBean(beanName, annotationType);
    }

    private Annotation findIconAnnotation() {
        Class<?> type = applicationContext.getType(beanName);
        while (type != null) {
            LOGGER.trace("Checking class [{}] for icon annotations", type.getName());
            Annotation[] annotations = type.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                LOGGER.trace("Checking annotation [{}] for icon annotations", annotation);
                if (annotation.annotationType().isAnnotationPresent(MenuIcon.class)) {
                    LOGGER.trace("Found icon annotation on [{}]", annotation);
                    return annotation;
                }
            }
            type = type.getSuperclass();
        }
        LOGGER.trace("Found no icon annotation");
        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private MenuIconProvider<Annotation> findIconProvider() {
        if (iconAnnotation != null) {
            final Class<? extends MenuIconProvider> iconProviderClass = iconAnnotation.annotationType().getAnnotation(MenuIcon.class).value();
            return applicationContext.getBean(iconProviderClass);
        } else {
            return null;
        }
    }

    /**
     * Returns the application context
     * @return The application context
     */
    protected ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     *
     * @return The bean name
     */
    protected String getBeanName() {
        return beanName;
    }

    /**
     * Returns the caption of this side bar item. If the caption was specified using {@link MenuItem#captionCode()},
     * this method will fetch the string from {@link I18N}.
     *
     * @return a string, never {@code null}.
     */
    public String getCaption() {
        if (item.captionCode().isEmpty()) {
            return item.caption();
        } else {
            return i18n.get(item.captionCode());
        }
    }

    /**
     * Returns the icon of the side bar item.
     *
     * @return an icon resource, or {@code null} if the item has no icon.
     */
    public Resource getIcon() {
        if (iconProvider != null) {
            return iconProvider.getIcon(iconAnnotation);
        } else {
            return null;
        }
    }

    /**
     * Returns the generated item id. Can f.e. be used to reference the ItemButton in selenium tests.
     *
     * @return The item id
     */
    public String getItemId() {
        return ITEM_ID_PREFIX + beanName.toLowerCase();
    }

    /**
     * @return the order of this side bar item within the section.
     */
    public int getOrder() {
        return item.order();
    }

    /**
     * @return whether this item should be shown in the top menu as well as the left menu
     */
    public boolean isTopMenu() {
        return item.topMenu();
    }

    /**
     * Checks if this item is a member of the specified side bar section.
     *
     * @param section the side bar section, must not be {@code null}.
     * @return true if the item is a member, false otherwise.
     */
    public boolean isMemberOfSection(
            final MenuSectionDescriptor section) {

        return item.sectionId().equals(section.getId());
    }

    @Override
    public int compareTo(
            final MenuItemDescriptor o) {

        return getOrder() - o.getOrder();
    }

    /**
     * This method must be called when the user clicks the item in the UI.
     *
     * @param ui the UI in which the item was invoked, must not be {@code null}.
     */
    public abstract void itemInvoked(UI ui);

    /**
     * Menu item descriptor for action items. When invoked, the descriptor will execute the operation.
     */
    public static class ActionItemDescriptor extends MenuItemDescriptor {

        /**
         * You should never need to create instances of this class directly.
         *
         * @param beanName           the name of the bean that implements the {@link Runnable} to run, must not be {@code null}.
         * @param applicationContext the application context to use when looking up beans, must not be {@code null}.
         */
        public ActionItemDescriptor(String beanName, ApplicationContext applicationContext) {
            super(beanName, applicationContext);
        }

        @Override
        public void itemInvoked(UI ui) {
            final Runnable operation = getApplicationContext().getBean(getBeanName(), Runnable.class);
            operation.run();
        }
    }

    /**
     * Side bar item descriptor for view items. When invoked, the descriptor will navigate to the
     * view.
     */
    public static class ViewItemDescriptor extends MenuItemDescriptor {

        private final SpringView vaadinView;

        /**
         * You should never need to create instances of this class directly.
         *
         * @param beanName           the name of the bean that implements the view to go to, must not be {@code null}.
         * @param applicationContext the application context to use when looking up beans, must not be {@code null}.
         */
        public ViewItemDescriptor(String beanName, ApplicationContext applicationContext) {
            super(beanName, applicationContext);
            this.vaadinView = applicationContext.findAnnotationOnBean(beanName, SpringView.class);
        }

        /**
         * Gets the name of the view to navigate to.
         *
         * @return a string, never {@code null}.
         */
        public String getViewName() {
            return vaadinView.name();
        }

        @Override
        public void itemInvoked(UI ui) {
            ui.getNavigator().navigateTo(vaadinView.name());
        }
    }
}