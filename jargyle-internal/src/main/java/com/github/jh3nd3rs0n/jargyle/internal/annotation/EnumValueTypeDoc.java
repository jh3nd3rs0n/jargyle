package com.github.jh3nd3rs0n.jargyle.internal.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The documentation of an enum value type. This annotation is applied to
 * enum classes.
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface EnumValueTypeDoc {

    /**
     * The description of the enum value type.
     *
     * @return the description of the enum value type
     */
    String description();

    /**
     * The name of the enum value type.
     *
     * @return the name of the enum value type
     */
    String name();

    /**
     * The syntax of the enum value.
     *
     * @return the syntax of the enum value
     */
    String syntax();

    /**
     * The syntax name of the enum value to be used in other syntaxes.
     *
     * @return the syntax name of the enum value to be used in other syntaxes
     */
    String syntaxName();

}
