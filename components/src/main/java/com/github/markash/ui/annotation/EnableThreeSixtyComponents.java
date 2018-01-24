package com.github.markash.ui.annotation;

import com.github.markash.ui.config.ThreeSixtyComponentsConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ThreeSixtyComponentsConfiguration.class)
public @interface EnableThreeSixtyComponents {
}