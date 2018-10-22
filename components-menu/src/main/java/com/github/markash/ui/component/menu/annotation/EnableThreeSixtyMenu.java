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

import com.github.markash.ui.component.menu.config.MenuConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Add this annotation to your application configuration to enable the {@link com.github.markash.ui.component.menu.ThreeSixtyHybridMenu}
 * component. After that, just inject the side bar into your UIs. The side bar uses {@link com.github.markash.ui.component.i18n.I18N}, so remember
 * {@link com.github.markash.ui.component.i18n.annotation.EnableThreeSixtyI18N enable} it as well, unless you are using auto configuration.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MenuConfig.class)
public @interface EnableThreeSixtyMenu {
    /**
     * Whether the Bread Crumbs are shown in the header
     * @return True whether shown else false
     */
    boolean showBreadCrumbs() default true;
}