package jargyle.net.socks.transport.v5;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import jargyle.annotation.HelpText;

public enum AuthMethod {

	@HelpText(doc = "No authentication required", usage = "NO_AUTHENTICATION_REQUIRED")
	NO_AUTHENTICATION_REQUIRED(Method.NO_AUTHENTICATION_REQUIRED),
	
	@HelpText(doc = "GSS-API authentication", usage = "GSSAPI")
	GSSAPI(Method.GSSAPI),
	
	@HelpText(doc = "Username password authentication", usage = "USERNAME_PASSWORD")
	USERNAME_PASSWORD(Method.USERNAME_PASSWORD);
	
	public static AuthMethod getInstance(final String s) {
		AuthMethod authMethod = null;
		try {
			authMethod = AuthMethod.valueOf(s);
		} catch (IllegalArgumentException e) {
			StringBuilder sb = new StringBuilder();
			List<AuthMethod> list = Arrays.asList(AuthMethod.values());
			for (Iterator<AuthMethod> iterator = list.iterator();
					iterator.hasNext();) {
				AuthMethod value = iterator.next();
				sb.append(value);
				if (iterator.hasNext()) {
					sb.append(", ");
				}
			}
			throw new IllegalArgumentException(
					String.format(
							"expected authentication method must be one of "
							+ "the following values: %s. actual value is %s",
							sb.toString(),
							s), 
					e);
		}
		return authMethod;
	}

	private final Method methodValue;
	
	private AuthMethod(final Method methValue) {
		this.methodValue = methValue;
	}
	
	public Method methodValue() {
		return this.methodValue;
	}
}
