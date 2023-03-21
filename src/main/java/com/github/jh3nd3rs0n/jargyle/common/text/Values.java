package com.github.jh3nd3rs0n.jargyle.common.text;

import java.util.Arrays;
import java.util.Iterator;

public final class Values {

	public static Values newInstance(final String s) {
		if (s.isEmpty()) {
			return newInstance(new String[] { });
		}
		return newInstance(s.split(" "));
	}
	
	public static Values newInstance(final String[] vals) {
		return new Values(vals);
	}
	
	private final String[] values;
	
	private Values(final String[] vals) {
		for (String val : vals) {
			if (val.matches(" ")) {
				throw new IllegalArgumentException(
						"value must not contain any spaces");
			}
		}
		this.values = Arrays.copyOf(vals, vals.length);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Values other = (Values) obj;
		if (!Arrays.equals(this.values, other.values)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(this.values);
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Iterator<String> iterator = Arrays.asList(this.values).iterator(); 
				iterator.hasNext();) {
			String string = iterator.next();
			builder.append(string);
			if (iterator.hasNext()) {
				builder.append(' ');
			}
		}
		return builder.toString();		
	}

	public String[] toStringArray() {
		return Arrays.copyOf(this.values, this.values.length);
	}

}
