package com.github.jh3nd3rs0n.jargyle.internal.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The documentation of an enum value. This annotation is applied to enum
 * constants.
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface EnumValueDoc {

    /**
     * The description of the enum value.
     *
     * @return the description of the enum value
     */
    String description();

    /**
     * The {@code String} representation of the enum value.
     *
     * @return the {@code String} representation of the enum value
     */
    String value();

}
