package jargyle.net.socks.transport.v5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class AuthMethods {
	
	public static AuthMethods newInstance(
			final AuthMethod authMthd, final AuthMethod... authMthds) {
		return newInstance(authMthd, Arrays.asList(authMthds));
	}
	
	public static AuthMethods newInstance(
			final AuthMethod authMthd, final List<AuthMethod> authMthds) {
		List<AuthMethod> list = new ArrayList<AuthMethod>();
		list.add(authMthd);
		list.addAll(authMthds);
		return new AuthMethods(list);
	}
	
	public static AuthMethods newInstance(final String s) {
		List<AuthMethod> authMethods = new ArrayList<AuthMethod>();
		String[] sElements = s.split(" ");
		for (String sElement : sElements) {
			authMethods.add(AuthMethod.valueOfString(sElement));
		}
		return new AuthMethods(authMethods);
	}
	
	private final List<AuthMethod> authMethods;
	
	private AuthMethods(final List<AuthMethod> authMthds) {
		this.authMethods = new ArrayList<AuthMethod>(authMthds);
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AuthMethods)) {
			return false;
		}
		AuthMethods other = (AuthMethods) obj;
		if (this.authMethods == null) {
			if (other.authMethods != null) {
				return false;
			}
		} else if (!this.authMethods.equals(other.authMethods)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.authMethods == null) ? 
				0 : this.authMethods.hashCode());
		return result;
	}

	public List<AuthMethod> toList() {
		return Collections.unmodifiableList(this.authMethods);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Iterator<AuthMethod> iterator = this.authMethods.iterator(); 
				iterator.hasNext();) {
			AuthMethod authMethod = iterator.next();
			builder.append(authMethod.toString());
			if (iterator.hasNext()) {
				builder.append(' ');
			}
		}
		return builder.toString();
	}
}
