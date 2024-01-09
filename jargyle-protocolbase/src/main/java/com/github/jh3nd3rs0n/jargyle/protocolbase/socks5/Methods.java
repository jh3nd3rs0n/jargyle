package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.ValuesValueTypeDoc;

@ValuesValueTypeDoc(
		description = "",
		elementValueType = Method.class,
		name = "SOCKS5 Methods",
		syntax = "[SOCKS5_METHOD1[,SOCKS5_METHOD2[...]]]",
		syntaxName = "SOCKS5_METHODS"
)
public final class Methods {
	
	private static final Methods DEFAULT_INSTANCE = new Methods(
			Arrays.asList(Method.NO_AUTHENTICATION_REQUIRED));
	
	public static Methods getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static Methods newInstance(final List<Method> meths) {
		return new Methods(meths);
	}
	
	public static Methods newInstance(final Method... meths) {
		return newInstance(Arrays.asList(meths));
	}
	
	public static Methods newInstanceOf(final String s) {
		List<Method> methods = new ArrayList<Method>();
		if (s.isEmpty()) {
			return new Methods(methods);
		}
		String[] sElements = s.split(",");
		for (String sElement : sElements) {
			methods.add(Method.valueOfString(sElement));
		}
		return new Methods(methods);
	}
	
	private final List<Method> methods;
	
	private Methods(final List<Method> meths) {
		this.methods = new ArrayList<Method>(meths);
	}
	
	public boolean contains(final Object obj) {
		return this.methods.contains(obj);
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Methods other = (Methods) obj;
		if (this.methods == null) {
			if (other.methods != null) {
				return false;
			}
		} else if (!this.methods.equals(other.methods)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.methods == null) ? 
				0 : this.methods.hashCode());
		return result;
	}

	public List<Method> toList() {
		return Collections.unmodifiableList(this.methods);
	}

	@Override
	public String toString() {
		return this.methods.stream()
				.map(Method::toString)
				.collect(Collectors.joining(","));
	}

}
