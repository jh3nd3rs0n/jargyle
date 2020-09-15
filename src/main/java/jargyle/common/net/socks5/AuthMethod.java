package jargyle.common.net.socks5;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import jargyle.common.cli.HelpTextParams;

public enum AuthMethod implements HelpTextParams {

	NO_AUTHENTICATION_REQUIRED(Method.NO_AUTHENTICATION_REQUIRED) {
		
		private static final String DOC = "No authentication required";
		
		@Override
		public String getDoc() {
			return DOC;
		}
		
	},
	
	GSSAPI(Method.GSSAPI) {
		
		private static final String DOC = "GSS-API authentication";
		
		@Override
		public String getDoc() {
			return DOC;
		}
		
	},
	
	USERNAME_PASSWORD(Method.USERNAME_PASSWORD) {
		
		private static final String DOC = "Username password authentication";
		
		@Override
		public String getDoc() {
			return DOC;
		}
		
	};
	
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

	@Override
	public final String getUsage() {
		return this.toString();
	}
	
	@Override
	public boolean isDisplayable() {
		return true;
	}
	
	public Method methodValue() {
		return this.methodValue;
	}
}
