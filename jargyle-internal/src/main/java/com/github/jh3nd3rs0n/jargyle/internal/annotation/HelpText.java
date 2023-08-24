package com.github.jh3nd3rs0n.jargyle.internal.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface HelpText {

	String doc();
	
	String usage();
	
}
