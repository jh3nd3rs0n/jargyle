package jargyle.net.socks.client.v5.userpassauth;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import jargyle.internal.net.socks.transport.v5.userpassauth.UsernamePasswordRequest;
import jargyle.security.EncryptedPassword;

@XmlJavaTypeAdapter(UsernamePassword.UsernamePasswordXmlAdapter.class)
public final class UsernamePassword {

	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "usernamePassword", propOrder = { })
	static class UsernamePasswordXml {
		@XmlElement(name = "encryptedPassword", required = true)
		protected EncryptedPassword encryptedPassword;
		@XmlElement(name = "username", required = true)
		protected String username;
	}
	
	static final class UsernamePasswordXmlAdapter 
		extends XmlAdapter<UsernamePasswordXml, UsernamePassword> {

		@Override
		public UsernamePasswordXml marshal(
				final UsernamePassword v) throws Exception {
			if (v == null) { return null; }
			UsernamePasswordXml usernamePasswordXml = 
					new UsernamePasswordXml();
			usernamePasswordXml.encryptedPassword = v.encryptedPassword;
			usernamePasswordXml.username = v.username;
			return usernamePasswordXml;
		}

		@Override
		public UsernamePassword unmarshal(
				final UsernamePasswordXml v) throws Exception {
			if (v == null) { return null; }
			return new UsernamePassword(v.username, v.encryptedPassword);
		}
		
	}
	
	public static final int MAX_PASSWORD_LENGTH = 
			UsernamePasswordRequest.MAX_PASSWD_LENGTH;

	public static final int MAX_USERNAME_LENGTH = 
			UsernamePasswordRequest.MAX_UNAME_LENGTH;
	
	public static UsernamePassword newInstance(final String s) {
		String[] sElements = s.split(":");
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"username and password must be in the following format: "
					+ "USERNAME:PASSWORD");
		}
		String username = null;
		try {
			username = URLDecoder.decode(sElements[0], "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
		char[] password = null;
		try {
			password = URLDecoder.decode(sElements[1], "UTF-8").toCharArray();
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
		return newInstance(username, password);
	}
	
	public static UsernamePassword newInstance(
			final String username, final char[] password) {
		validateUsername(username);
		validatePassword(password);
		return new UsernamePassword(
				username, EncryptedPassword.newInstance(password));
	}
	
	public static void validatePassword(final char[] password) {
		UsernamePasswordRequest.validatePassword(password);
	}
	
	public static void validateUsername(final String username) {
		UsernamePasswordRequest.validateUsername(username);
	}
	
	private final EncryptedPassword encryptedPassword;
	private final String username;
	
	private UsernamePassword(
			final String usrnm, final EncryptedPassword encryptedPsswrd) {
		this.encryptedPassword = encryptedPsswrd;
		this.username = usrnm;
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
		UsernamePassword other = (UsernamePassword) obj;
		if (this.encryptedPassword == null) {
			if (other.encryptedPassword != null) {
				return false;
			}
		} else if (!this.encryptedPassword.equals(other.encryptedPassword)) {
			return false;
		}
		if (this.username == null) {
			if (other.username != null) {
				return false;
			}
		} else if (!this.username.equals(other.username)) {
			return false;
		}
		return true;
	}
	
	public EncryptedPassword getEncryptedPassword() {
		return this.encryptedPassword;
	}
	
	public String getUsername() {
		return this.username;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.encryptedPassword == null) ? 0 : this.encryptedPassword.hashCode());
		result = prime * result + ((this.username == null) ? 0 : this.username.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [encryptedPassword=")
			.append(this.encryptedPassword)
			.append(", username=")
			.append(this.username)
			.append("]");
		return builder.toString();
	}
	
}
