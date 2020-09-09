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
		@XmlElement(name = "value")
		protected String value;
	}
	
	static class UsernamePasswordAuthenticatorXmlAdapter extends 
	XmlAdapter<UsernamePasswordAuthenticatorXml, UsernamePasswordAuthenticator> {

		@Override
		public UsernamePasswordAuthenticatorXml marshal(
				final UsernamePasswordAuthenticator v) throws Exception {
			if (v == null) { return null; }
			UsernamePasswordAuthenticatorXml usernamePasswordAuthenticatorXml =
					new UsernamePasswordAuthenticatorXml();
			usernamePasswordAuthenticatorXml.className = v.getClass().getName();
			usernamePasswordAuthenticatorXml.value = v.value;
			return usernamePasswordAuthenticatorXml;
		}

		@Override
		public UsernamePasswordAuthenticator unmarshal(
				final UsernamePasswordAuthenticatorXml v) throws Exception {
			if (v == null) { return null; }
			return UsernamePasswordAuthenticator.getInstance(
					v.className, v.value);
		}
		
	}
	
	static final UsernamePasswordAuthenticator INSTANCE = 
			new UsernamePasswordAuthenticator(null);
	
	public static UsernamePasswordAuthenticator getInstance(
			final Class<?> cls, final String value) {
		UsernamePasswordAuthenticator usernamePasswordAuthenticator = null;
		if (cls.equals(UsernamePasswordAuthenticator.class)) {
			usernamePasswordAuthenticator = INSTANCE;
		} else if (cls.equals(
				StringSourceUsernamePasswordAuthenticator.class)) {
			usernamePasswordAuthenticator = 
					new StringSourceUsernamePasswordAuthenticator(value);
		} else if (cls.equals(
				XmlFileSourceUsernamePasswordAuthenticator.class)) {
			usernamePasswordAuthenticator = 
					XmlFileSourceUsernamePasswordAuthenticator.newInstance(
							value);
		} else if (UsernamePasswordAuthenticator.class.isAssignableFrom(cls)) {
			Method method = null;
			Constructor<?> constructor = null;
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
			if (method == null) {
				for (Constructor<?> ctor : cls.getDeclaredConstructors()) {
					int modifiers = ctor.getModifiers();
					Class<?>[] parameterTypes = ctor.getParameterTypes();
					boolean isPublic = Modifier.isPublic(modifiers);
					boolean isInstantiatable = !Modifier.isAbstract(modifiers)
							&& !Modifier.isInterface(modifiers);
					boolean isParameterTypeString = parameterTypes.length == 1 
							&& parameterTypes[0].equals(String.class);
					if (isPublic && isInstantiatable && isParameterTypeString) {
						constructor = ctor;
						break;
					}
				}
			}
			if (method != null) {
				method.setAccessible(true);
				try {
					usernamePasswordAuthenticator = 
							(UsernamePasswordAuthenticator) method.invoke(
									null, value);
				} catch (IllegalAccessException e) {
					throw new AssertionError(e);
				} catch (IllegalArgumentException e) {
					throw new AssertionError(e);
				} catch (InvocationTargetException e) {
					throw new AssertionError(e); 
				}
			} else if (constructor != null) {
				constructor.setAccessible(true);
				try {
					usernamePasswordAuthenticator = 
							(UsernamePasswordAuthenticator) constructor.newInstance(
									value);
				} catch (InstantiationException e) {
					throw new AssertionError(e);
				} catch (IllegalAccessException e) {
					throw new AssertionError(e);
				} catch (IllegalArgumentException e) {
					throw new AssertionError(e);
				} catch (InvocationTargetException e) {
					throw new AssertionError(e); 
				}
			} else {
				throw new IllegalArgumentException(String.format(
						"class %1$s does not have either a public static "
						+ "method that has one method parameter of type "
						+ "%1$s and a method return type of the provided "
						+ "type nor a public instantiatable constructor "
						+ "that has one constructor parameter of type %1$s",
						String.class.getName()));
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
		String value = null;
		if (sElements.length == 2) {
			value = sElements[1];
		}
		return getInstance(className, value);
	}
	
	private static UsernamePasswordAuthenticator getInstance(
			final String className, final String value) {
		Class<?> cls = null;
		try {
			cls = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
		return getInstance(cls, value);
	}
	
	private final String value;
	
	protected UsernamePasswordAuthenticator(final String val) {
		this.value = val;
	}
	
	public boolean authenticate(
			final String username, final char[] password) {
		return false;
	}
	
	public final String getValue() {
		return this.value;
	}

	@Override
	public final String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getName());
		if (this.value != null) {
			builder.append(":").append(this.value);
		}
		return builder.toString();
	}
	
}
