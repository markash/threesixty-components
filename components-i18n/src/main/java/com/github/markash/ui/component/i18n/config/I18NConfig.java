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
package com.github.markash.ui.component.i18n.config;

import com.github.markash.ui.component.i18n.CompositeMessageSource;
import com.github.markash.ui.component.i18n.I18N;
import com.github.markash.ui.component.i18n.MessageProviderCacheCleanupExecutor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


/**
 * Configuration class used by {@literal @}EnableThreeSixtyI18N
 *
 * Spring configuration for the {@link CompositeMessageSource}. Please remember to
 * define {@link com.github.markash.ui.component.i18n.MessageProvider} beans that can serve the message source with messages.
 *
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 * @author Petter Holmström (petter@vaadin.com)
 * @see I18N
 * @see CompositeMessageSource
 */
@Configuration
public class I18NConfig {

    @Bean
    I18N i18n(
            final ApplicationContext context) {

        return new I18N(context);
    }

    @Bean
    CompositeMessageSource messageSource(
            final ApplicationContext context) {

        return new CompositeMessageSource(context);
    }

    @Bean
    MessageProviderCacheCleanupExecutor messageProviderCacheCleanupExecutor(
            Environment environment,
            CompositeMessageSource messageSource) {

        return new MessageProviderCacheCleanupExecutor(environment, messageSource);
    }
}
