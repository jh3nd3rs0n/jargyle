package jargyle.server.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import jargyle.common.net.socks5.usernamepasswordauth.UsernamePasswordRequest;
import jargyle.common.net.socks5.usernamepasswordauth.UsernamePasswordResponse;
import jargyle.server.Configuration;

@XmlJavaTypeAdapter(UsernamePasswordAuthenticator.UsernamePasswordAuthenticatorXmlAdapter.class)
public class UsernamePasswordAuthenticator implements Authenticator {

	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "usernamePasswordAuthenticator", propOrder = { })
	static class UsernamePasswordAuthenticatorXml {
		@XmlElement(name = "className", required = true)
		protected String className;
		@XmlElement(name = "parameterString")
		protected String parameterString;
	}
	
	static class UsernamePasswordAuthenticatorXmlAdapter extends 
	XmlAdapter<UsernamePasswordAuthenticatorXml, UsernamePasswordAuthenticator> {

		@Override
		public UsernamePasswordAuthenticatorXml marshal(
				final UsernamePasswordAuthenticator v) throws Exception {
			if (v == null) { return null; }
			UsernamePasswordAuthenticatorXml usernamePasswordAuthenticatorXml =
					new UsernamePasswordAuthenticatorXml();
			usernamePasswordAuthenticatorXml.className = 
					v.getClass().getName();
			usernamePasswordAuthenticatorXml.parameterString = 
					v.parameterString;
			return usernamePasswordAuthenticatorXml;
		}

		@Override
		public UsernamePasswordAuthenticator unmarshal(
				final UsernamePasswordAuthenticatorXml v) throws Exception {
			if (v == null) { return null; }
			return UsernamePasswordAuthenticator.getInstance(
					v.className, v.parameterString);
		}
		
	}
	
	static final UsernamePasswordAuthenticator INSTANCE = 
			new UsernamePasswordAuthenticator(null);
	
	public static UsernamePasswordAuthenticator getInstance(
			final Class<?> cls, final String parameterString) {
		UsernamePasswordAuthenticator usernamePasswordAuthenticator = null;
		if (cls.equals(UsernamePasswordAuthenticator.class)) {
			usernamePasswordAuthenticator = INSTANCE;
		} else if (cls.equals(
				StringSourceUsernamePasswordAuthenticator.class)) {
			usernamePasswordAuthenticator = 
					new StringSourceUsernamePasswordAuthenticator(
							parameterString);
		} else if (cls.equals(
				XmlFileSourceUsernamePasswordAuthenticator.class)) {
			usernamePasswordAuthenticator = 
					new XmlFileSourceUsernamePasswordAuthenticator(
							parameterString);
		} else if (UsernamePasswordAuthenticator.class.isAssignableFrom(cls)) {
			Constructor<?> ctor = null;
			try {
				ctor = cls.getConstructor(String.class);
			} catch (NoSuchMethodException e) {
				throw new IllegalArgumentException(e);
			} catch (SecurityException e) {
				throw new AssertionError(e);
			}
			try {
				usernamePasswordAuthenticator = 
						(UsernamePasswordAuthenticator) ctor.newInstance(
								parameterString);
			} catch (InstantiationException e) {
				throw new AssertionError(e);
			} catch (IllegalAccessException e) {
				throw new AssertionError(e);
			} catch (IllegalArgumentException e) {
				throw new AssertionError(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e.getCause());
			}
		} else {
			throw new IllegalArgumentException(String.format(
					"class must be or must extend '%s'", 
					UsernamePasswordAuthenticator.class.getName()));
		}
		return usernamePasswordAuthenticator;
	}
	
	public static UsernamePasswordAuthenticator getInstance(final String s) {
		String[] sElements = s.split(":", 2);
		String className = sElements[0];
		String parameterString = null;
		if (sElements.length == 2) {
			parameterString = sElements[1];
		}
		return getInstance(className, parameterString);
	}
	
	private static UsernamePasswordAuthenticator getInstance(
			final String className, final String parameterString) {
		Class<?> cls = null;
		try {
			cls = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
		return getInstance(cls, parameterString);
	}
	
	private final String parameterString;
	
	protected UsernamePasswordAuthenticator(final String paramString) {
		this.parameterString = paramString;
	}
	
	@Override
	public final Socket authenticate(
			final Socket socket, 
			final Configuration configuration) throws IOException {
		InputStream inputStream = socket.getInputStream();
		OutputStream outputStream = socket.getOutputStream();
		UsernamePasswordRequest usernamePasswordReq = 
				UsernamePasswordRequest.newInstanceFrom(inputStream);
		UsernamePasswordResponse usernamePasswordResp = null;
		String username = usernamePasswordReq.getUsername();
		char[] password = usernamePasswordReq.getPassword();
		UsernamePasswordAuthenticator authenticator = 
				configuration.getSocks5UsernamePasswordAuthenticator();
		if (authenticator == null) { authenticator = this; }
		if (!authenticator.authenticate(username, password)) {
			usernamePasswordResp = UsernamePasswordResponse.newInstance(
					(byte) 0x01);
			outputStream.write(usernamePasswordResp.toByteArray());
			outputStream.flush();
			return null;
		}
		usernamePasswordResp = UsernamePasswordResponse.newInstance(
				UsernamePasswordResponse.STATUS_SUCCESS);
		outputStream.write(usernamePasswordResp.toByteArray());
		outputStream.flush();
		return socket;
	}
	
	protected boolean authenticate(
			final String username, final char[] password) {
		return false;
	}
	
	public final String getParameterString() {
		return this.parameterString;
	}

	@Override
	public final String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getName());
		if (this.parameterString != null) {
			builder.append(":").append(this.parameterString);
		}
		return builder.toString();
	}
	
}
