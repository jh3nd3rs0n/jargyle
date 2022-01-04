package com.github.jh3nd3rs0n.jargyle.common.text;

import java.util.Arrays;
import java.util.Iterator;

public final class Words {

	public static Words newInstance(final String s) {
		if (s.isEmpty()) {
			return newInstance(new String[] { });
		}
		return newInstance(s.split(" "));
	}
	
	public static Words newInstance(final String[] wrds) {
		return new Words(wrds);
	}
	
	private final String[] words;
	
	private Words(final String[] wrds) {
		for (String wrd : wrds) {
			if (wrd.matches(" ")) {
				throw new IllegalArgumentException(
						"word must not contain any spaces");
			}
		}
		this.words = Arrays.copyOf(wrds, wrds.length);
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
		Words other = (Words) obj;
		if (!Arrays.equals(this.words, other.words)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(this.words);
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Iterator<String> iterator = Arrays.asList(this.words).iterator(); 
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
		return Arrays.copyOf(this.words, this.words.length);
	}

}
