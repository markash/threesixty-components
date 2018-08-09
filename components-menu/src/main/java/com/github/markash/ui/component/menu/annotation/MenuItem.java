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
package com.github.markash.ui.component.menu.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to declare a {@link AccordionSideBar} item that a user can click on.
 * It can be placed on two types of beans:
 * <ol>
 * <li>{@link java.lang.Runnable}s - when the item is clicked, the runnable is executed</li>
 * <li>{@link com.vaadin.navigator.View Views}s that are also annotated with {@link VaadinView VaadinView} - when the item is clicked, the {@link com.vaadin.navigator.Navigator navigator} navigates to the view.
 * </ol>
 * Please note that the annotated class must be a Spring-managed bean - only adding this annotation is not enough.
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @see AccordionSideBar
 * @see SideBarSection
 * @see FontAwesomeIcon
 * @see ThemeIcon
 * @see LocalizedThemeIcon
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MenuItem {

    /**
     * The ID of the side bar section that this item belongs to.
     *
     * @see MenuSection#id()
     */
    String sectionId();

    /**
     * The caption of this item.
     *
     * @see #captionCode()
     */
    String caption() default "";

    /**
     * The code to pass to the {@link com.github.markash.ui.component.i18n.I18N} instance to get the item caption.
     * If this is an empty string, {@link #caption()} is used instead.
     *
     * @see com.github.markash.ui.component.i18n.I18N#get(String, Object...)
     */
    String captionCode() default "";

    /**
     * The order of this item within its section. Items with a lower order are placed higher up in the list.
     */
    int order() default Integer.MAX_VALUE;

    /**
     * Whether this item should be shown in the top menu or just the side menu
     */
    boolean topMenu() default false;
}