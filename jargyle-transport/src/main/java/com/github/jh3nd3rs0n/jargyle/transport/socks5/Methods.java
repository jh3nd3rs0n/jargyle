package com.github.jh3nd3rs0n.jargyle.transport.socks5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
	
	public static Methods newInstance(final String s) {
		List<Method> methods = new ArrayList<Method>();
		if (s.isEmpty()) {
			return new Methods(methods);
		}
		String[] sElements = s.split(" ");
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
		StringBuilder builder = new StringBuilder();
		for (Iterator<Method> iterator = this.methods.iterator(); 
				iterator.hasNext();) {
			Method method = iterator.next();
			builder.append(method.toString());
			if (iterator.hasNext()) {
				builder.append(' ');
			}
		}
		return builder.toString();
	}

}
