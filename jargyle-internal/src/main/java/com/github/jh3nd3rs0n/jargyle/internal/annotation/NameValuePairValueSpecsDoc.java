package com.github.jh3nd3rs0n.jargyle.internal.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The documentation of the specifications of name value pair values.
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface NameValuePairValueSpecsDoc {

	/**
	 * The description of the name value pair values.
	 * @return the description of the name value pair values
	 */
	String description();
	
	/**
	 * The name of the name value pair values.
	 * @return the name of the name value pair values
	 */
	String name();
	
}
