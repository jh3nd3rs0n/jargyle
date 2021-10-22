package com.github.jh3nd3rs0n.jargyle.server;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public enum ConditionPredicateMethod {
	
	EQUALS("equals") {

		@Override
		public boolean evaluate(final String arg0, final String arg1) {
			return arg0.equals(arg1);
		}
		
	},
	
	MATCHES("matches") {

		@Override
		public boolean evaluate(final String arg0, final String arg1) {
			return arg0.matches(arg1);
		}
		
	};

	public static ConditionPredicateMethod valueOfString(final String s) {
		for (ConditionPredicateMethod value : ConditionPredicateMethod.values()) {
			if (value.toString().equals(s)) {
				return value;
			}
		}
		StringBuilder sb = new StringBuilder();
		List<ConditionPredicateMethod> list = Arrays.asList(
				ConditionPredicateMethod.values());
		for (Iterator<ConditionPredicateMethod> iterator = list.iterator();
				iterator.hasNext();) {
			ConditionPredicateMethod value = iterator.next();
			sb.append(value);
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}
		throw new IllegalArgumentException(String.format(
				"expected condition predicate method must be one of the "
				+ "following values: %s. actual value is %s",
				sb.toString(),
				s));
	}
	
	private final String string;
	
	private ConditionPredicateMethod(final String str) {
		this.string = str;
	}
	
	public abstract boolean evaluate(final String arg0, final String arg1);
	
	@Override
	public String toString() {
		return this.string;
	}

}
