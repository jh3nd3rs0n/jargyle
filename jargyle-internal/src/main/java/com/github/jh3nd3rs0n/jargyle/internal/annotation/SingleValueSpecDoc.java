package com.github.jh3nd3rs0n.jargyle.internal.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The documentation of the specification of a single value.
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface SingleValueSpecDoc {

	/**
	 * The description of the single value.
	 * @return the description of the single value
	 */
	String description();
	
	/**
	 * The name of the single value.
	 * @return the name of the single value
	 */
	String name();
	
	/**
	 * The syntax of the single value
	 * @return the syntax of the single value
	 */
	String syntax();
	
}
