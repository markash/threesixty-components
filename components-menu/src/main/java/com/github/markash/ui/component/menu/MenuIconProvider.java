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

import com.github.markash.ui.component.menu.annotation.MenuIcon;
import com.vaadin.server.Resource;
import java.lang.annotation.Annotation;

/**
 * Interface defining a provider that maps a side bar item icon annotation to an actual {@link com.vaadin.server.Resource}.
 * Implementations of this interface should be Spring managed beans (typically singletons).
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @see MenuIcon#value()
 */
public interface MenuIconProvider<A extends Annotation> {

    /**
     * Returns the icon resource that the specified annotation refers to, or {@code null} if not found.
     * @param annotation The annotation that the provider provides icons for
     * @return The icon resource
     */
    Resource getIcon(A annotation);
}