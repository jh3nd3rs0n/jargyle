package com.github.jh3nd3rs0n.jargyle.internal.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The documentation of the specification of a the name value pair value.
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface NameValuePairValueSpecDoc {

	/**
	 * The description of the name value pair value.
	 * @return the description of the name value pair value
	 */
	String description();
	
	/**
	 * The name of the name value pair value.
	 * @return the name of the name value pair value
	 */
	String name();
	
	/**
	 * The syntax of the name value pair value.
	 * @return the syntax of the name value pair value
	 */
	String syntax();
	
	/**
	 * The type of value of the name value pair value. 
	 * @return the type of value of the name value pair value
	 */
	Class<?> valueType();
	
}
