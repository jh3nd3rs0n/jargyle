package com.github.jh3nd3rs0n.jargyle.internal.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The documentation of a name value pair value type. This annotation is
 * applied to classes such as {@code Property} and {@code Setting}.
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface NameValuePairValueTypeDoc {

    /**
     * The description of a name value pair value type.
     *
     * @return the description of a name value pair value type
     */
    String description();

    /**
     * The name of the name value pair value type.
     *
     * @return the name of the name value pair value type
     */
    String name();

    /**
     * The specifications of the name value pair values of the name value pair
     * value type.
     *
     * @return the specifications of the name value pair values of the name
     * value pair value type
     */
    Class<?>[] nameValuePairValueSpecs();

    /**
     * The syntax of the name value pair value.
     *
     * @return the syntax of the name value pair value
     */
    String syntax();

    /**
     * The syntax name of the name value pair value to be used in other
     * syntaxes.
     *
     * @return the syntax name of the name value pair value to be used in other
     * syntaxes
     */
    String syntaxName();

}
