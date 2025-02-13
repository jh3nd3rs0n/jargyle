package com.github.jh3nd3rs0n.jargyle.internal.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The documentation of the specification of a name value pair value. This
 * annotation is applied to Spec constants such as constants of type
 * {@code PropertySpec} or type {@code SettingSpec}.
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface NameValuePairValueSpecDoc {

    /**
     * The default value of the name value pair value.
     *
     * @return the default value of the name value pair value
     */
    String defaultValue() default "";

    /**
     * The description of the name value pair value.
     *
     * @return the description of the name value pair value
     */
    String description();

    /**
     * The name of the name value pair value.
     *
     * @return the name of the name value pair value
     */
    String name();

    /**
     * The syntax of the name value pair value.
     *
     * @return the syntax of the name value pair value
     */
    String syntax();

    /**
     * The type of value of the name value pair value.
     *
     * @return the type of value of the name value pair value
     */
    Class<?> valueType();

}
