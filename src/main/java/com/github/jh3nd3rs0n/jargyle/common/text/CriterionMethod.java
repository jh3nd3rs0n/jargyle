package com.github.jh3nd3rs0n.jargyle.common.text;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public enum CriterionMethod {
	
	EQUALS("equals") {

		@Override
		public boolean evaluatesTrue(
				final String arg0, final String arg1) {
			return arg0.equals(arg1);
		}
		
	},
	
	MATCHES("matches") {

		@Override
		public boolean evaluatesTrue(
				final String arg0, final String arg1) {
			return arg0.matches(arg1);
		}
		
	};

	public static CriterionMethod valueOfString(final String s) {
		for (CriterionMethod value : CriterionMethod.values()) {
			if (value.toString().equals(s)) {
				return value;
			}
		}
		StringBuilder sb = new StringBuilder();
		List<CriterionMethod> list = Arrays.asList(CriterionMethod.values());
		for (Iterator<CriterionMethod> iterator = list.iterator();
				iterator.hasNext();) {
			CriterionMethod value = iterator.next();
			sb.append(value);
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}
		throw new IllegalArgumentException(String.format(
				"expected criterion method must be one of the following "
				+ "values: %s. actual value is %s",
				sb.toString(),
				s));
	}
	
	private final String string;
	
	private CriterionMethod(final String str) {
		this.string = str;
	}
	
	public abstract boolean evaluatesTrue(
			final String arg0, final String arg1);
	
	@Override
	public String toString() {
		return this.string;
	}
	
}