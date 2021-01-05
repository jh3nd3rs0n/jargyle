package jargyle.common.net.http;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public enum Method {
	
	CONNECT("CONNECT"),
	
	DELETE("DELETE"),

	GET("GET"),
	
	HEAD("HEAD"),
	
	OPTIONS("OPTIONS"),
	
	POST("POST"),
	
	PUT("PUT"),
	
	TRACE("TRACE");
	
	public static Method getInstance(final String s) {
		for (Method method : Method.values()) {
			if (method.value.equals(s)) {
				return method;
			}
		}
		StringBuilder sb = new StringBuilder();
		List<Method> list = Arrays.asList(Method.values());
		for (Iterator<Method> iterator = list.iterator(); iterator.hasNext();) {
			Method method = iterator.next();
			sb.append(method);
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}
		throw new IllegalArgumentException(String.format(
				"expected method must be one of the following values: %s. "
				+ "actual value is %s",
				sb.toString(),
				s));
	}
	
	private final String value;
	
	private Method(final String val) {
		this.value = val;
	}
	
	@Override
	public String toString() {
		return this.value;
	}
	
}
