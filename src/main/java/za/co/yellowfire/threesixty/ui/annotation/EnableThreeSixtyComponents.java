package za.co.yellowfire.threesixty.ui.annotation;

import org.springframework.context.annotation.Import;
import za.co.yellowfire.threesixty.ui.config.ThreeSixtyComponensConfiguration;

import java.lang.annotation.*;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ThreeSixtyComponensConfiguration.class)
public @interface EnableThreeSixtyComponents {
}