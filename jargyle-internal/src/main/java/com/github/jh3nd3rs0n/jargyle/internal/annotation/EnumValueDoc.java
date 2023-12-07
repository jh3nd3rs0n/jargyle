package com.github.jh3nd3rs0n.jargyle.internal.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The documentation of a enum value.
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface EnumValueDoc {

	/**
	 * The description of the enum value.
	 * @return the description of the enum value
	 */
	String description();
	
	/**
	 * The {@code String} representation of the enum value.
	 * @return the {@code String} representation of the enum value
	 */
	String value();
	
}
