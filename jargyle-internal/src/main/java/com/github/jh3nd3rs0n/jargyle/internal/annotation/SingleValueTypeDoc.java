package com.github.jh3nd3rs0n.jargyle.internal.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The documentation of a single value type.
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface SingleValueTypeDoc {

    /**
     * The description of the single value type.
     *
     * @return the description of the single value type
     */
    String description();

    /**
     * The name of the single value type.
     *
     * @return the name of the single value type
     */
    String name();

    /**
     * The specifications of the single values of the single value type.
     *
     * @return the specifications of the single values of the single value type
     */
    Class<?>[] singleValueSpecs() default {};

    /**
     * The syntax of the single value.
     *
     * @return the syntax of the single value
     */
    String syntax();

    /**
     * The syntax name of the single value to be used in other syntaxes.
     *
     * @return the syntax name of the single value to be used in other syntaxes
     */
    String syntaxName();

}
