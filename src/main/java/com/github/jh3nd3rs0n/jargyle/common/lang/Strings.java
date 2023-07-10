package com.github.jh3nd3rs0n.jargyle.common.lang;

import java.util.Arrays;
import java.util.Iterator;

public final class Strings {

	public static Strings newInstance(final String s) {
		if (s.isEmpty()) {
			return newInstance(new String[] { });
		}
		return newInstance(s.split(" "));
	}
	
	public static Strings newInstance(final String[] strs) {
		return new Strings(strs);
	}
	
	private final String[] strings;
	
	private Strings(final String[] strs) {
		this.strings = Arrays.copyOf(strs, strs.length);
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
		Strings other = (Strings) obj;
		if (!Arrays.equals(this.strings, other.strings)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(this.strings);
		return result;
	}

	public String[] toArray() {
		return Arrays.copyOf(this.strings, this.strings.length);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Iterator<String> iterator = Arrays.asList(this.strings).iterator(); 
				iterator.hasNext();) {
			String string = iterator.next();
			builder.append(string);
			if (iterator.hasNext()) {
				builder.append(' ');
			}
		}
		return builder.toString();		
	}

}
