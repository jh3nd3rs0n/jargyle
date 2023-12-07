package com.github.jh3nd3rs0n.jargyle.internal.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The documentation of the specifications of single values.
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface SingleValueSpecsDoc {

	/**
	 * The description of the single values.
	 * @return the description of the single values
	 */
	String description();
	
	/**
	 * The name of the single values.
	 * @return the name of the single values
	 */
	String name();
	
}
