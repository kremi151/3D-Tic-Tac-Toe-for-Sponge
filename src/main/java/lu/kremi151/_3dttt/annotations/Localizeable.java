/*
 * Michel Kremer
 */
package lu.kremi151._3dttt.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author michm
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Localizeable {
    
    String rootNode() default "normal";
    String node();
    
}
