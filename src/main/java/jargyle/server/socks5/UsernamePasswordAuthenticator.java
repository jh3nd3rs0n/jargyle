package jargyle.server.socks5;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(UsernamePasswordAuthenticator.UsernamePasswordAuthenticatorXmlAdapter.class)
public class UsernamePasswordAuthenticator {

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
					XmlFileSourceUsernamePasswordAuthenticator.newInstance(
							parameterString);
		} else if (UsernamePasswordAuthenticator.class.isAssignableFrom(cls)) {
			Method method = null;
			for (Method meth : cls.getDeclaredMethods()) {
				int modifiers = meth.getModifiers();
				Class<?> returnType = meth.getReturnType();
				Class<?>[] parameterTypes = meth.getParameterTypes();
				boolean isPublic = Modifier.isPublic(modifiers);
				boolean isStatic = Modifier.isStatic(modifiers);
				boolean isReturnTypeClass = returnType.equals(cls);
				boolean isParameterTypeString = parameterTypes.length == 1 
						&& parameterTypes[0].equals(String.class);
				if (isPublic 
						&& isStatic 
						&& isReturnTypeClass 
						&& isParameterTypeString) {
					method = meth;
					break;
				}
			}
			if (method != null) {
				try {
					usernamePasswordAuthenticator = 
							(UsernamePasswordAuthenticator) method.invoke(
									null, parameterString);
				} catch (IllegalAccessException e) {
					throw new IllegalArgumentException(e);
				} catch (IllegalArgumentException e) {
					throw new IllegalArgumentException(e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e.getCause()); 
				}
			} else {
				Constructor<?> ctor = null;
				try {
					ctor = cls.getConstructor(String.class);
				} catch (NoSuchMethodException e) {
					throw new IllegalArgumentException(e);
				} catch (SecurityException e) {
					throw new IllegalArgumentException(e);
				}
				try {
					usernamePasswordAuthenticator = 
							(UsernamePasswordAuthenticator) ctor.newInstance(
									parameterString);
				} catch (InstantiationException e) {
					throw new IllegalArgumentException(e);
				} catch (IllegalAccessException e) {
					throw new IllegalArgumentException(e);
				} catch (IllegalArgumentException e) {
					throw new IllegalArgumentException(e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e.getCause());
				}
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
	
	public boolean authenticate(
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
