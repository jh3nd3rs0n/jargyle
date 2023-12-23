package com.github.jh3nd3rs0n.jargyle.internal.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The documentation of a values value type. This annotation is applied to
 * classes that have more than one value of the same type.
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface ValuesValueTypeDoc {

    /**
     * The description of the values value type.
     *
     * @return the description of the values value type
     */
    String description();

    /**
     * The type of element value of the values value type.
     *
     * @return type of element value of the values value type
     */
    Class<?> elementValueType();

    /**
     * The name of the values value type.
     *
     * @return the name of the values value type
     */
    String name();

    /**
     * The syntax of the values value.
     *
     * @return the syntax of the values value
     */
    String syntax();

    /**
     * The syntax name of the values value to be used in other syntaxes.
     *
     * @return the syntax name of the values value to be used in other syntaxes
     */
    String syntaxName();

}
