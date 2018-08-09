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

import com.github.markash.ui.component.menu.annotation.VaadinFontIcon;
import com.vaadin.server.Resource;

/**
 * Icon provider for {@link VaadinFontIcon}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class VaadinFontIconProvider implements MenuIconProvider<VaadinFontIcon> {

    @Override
    public Resource getIcon(
            final VaadinFontIcon annotation) {

        return annotation.value();
    }
}