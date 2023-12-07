package com.github.jh3nd3rs0n.jargyle.common.string;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.ValuesValueTypeDoc;

@ValuesValueTypeDoc(
		description = "",
		elementValueType = String.class,		
		name = "Comma Separated Values",
		syntax = "[VALUE1[,VALUE2[...]]]",
		syntaxName = "COMMA_SEPARATED_VALUES"
)
public final class CommaSeparatedValues {

	public static CommaSeparatedValues newInstance(final String s) {
		if (s.isEmpty()) {
			return newInstance(new String[] { });
		}
		return newInstance(s.split(","));
	}
	
	public static CommaSeparatedValues newInstance(final String... vals) {
		return new CommaSeparatedValues(vals);
	}
	
	private final String[] values;
	
	private CommaSeparatedValues(final String[] vals) {
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
		CommaSeparatedValues other = (CommaSeparatedValues) obj;
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

	public String[] toArray() {
		return Arrays.copyOf(this.values, this.values.length);
	}

	@Override
	public String toString() {
		return Arrays.stream(this.values).collect(Collectors.joining(","));
	}

}
