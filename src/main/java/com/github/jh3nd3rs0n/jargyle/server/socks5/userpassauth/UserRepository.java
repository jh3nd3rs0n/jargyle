package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.StringSourceUserRepository;

public abstract class UserRepository {

	public static UserRepository newInstance() {
		return new StringSourceUserRepository("");
	}
	
	public static UserRepository newInstance(
			final Class<?> cls, final String initializationValue) {
		if (cls.equals(UserRepository.class) 
				|| !UserRepository.class.isAssignableFrom(cls)) {
			throw new IllegalArgumentException(String.format(
					"class must extend '%s'", 
					UserRepository.class.getName()));			
		}
		UserRepository userRepository = 
				newInstanceFromStaticStringConversionMethod(
						cls, initializationValue);
		if (userRepository == null) {
			userRepository = newInstanceFromStringConversionConstructor(
					cls, initializationValue);
		}
		if (userRepository == null) {
			throw new IllegalArgumentException(String.format(
					"class %1$s does not have either a static method that has "
					+ "one method parameter of type %2$s and a method return "
					+ "type of the provided type nor an instantiatable "
					+ "constructor that has one constructor parameter of type "
					+ "%2$s",
					cls,
					String.class.getName()));			
		}
		return userRepository;
	}
	
	public static UserRepository newInstance(final String s) {
		String[] sElements = s.split(":", 2);
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"user repository must be in the following format: "
					+ "CLASS_NAME:INITIALIZATION_VALUE");
		}
		String className = sElements[0];
		String initializationValue = sElements[1];
		return newInstance(className, initializationValue);
	}
	
	public static UserRepository newInstance(
			final String className, final String initializationValue) {
		Class<?> cls = null;
		try {
			cls = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
		return newInstance(cls, initializationValue);
	}
	
	private static UserRepository newInstanceFromStaticStringConversionMethod(
			final Class<?> cls, final String initializationValue) {
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
		UserRepository userRepository = null;
		if (method != null) {
			method.setAccessible(true);
			try {
				userRepository = (UserRepository) method.invoke(
						null, initializationValue);
			} catch (IllegalAccessException e) {
				throw new AssertionError(e);
			} catch (IllegalArgumentException e) {
				throw new AssertionError(e);
			} catch (InvocationTargetException e) {
				throw new AssertionError(e); 
			}
		}
		return userRepository;
	}
	
	private static UserRepository newInstanceFromStringConversionConstructor(
			final Class<?> cls, final String initializationValue) {
		Constructor<?> constructor = null;
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
		UserRepository userRepository = null;
		if (constructor != null) {
			constructor.setAccessible(true);
			try {
				userRepository = (UserRepository) constructor.newInstance(
						initializationValue);
			} catch (InstantiationException e) {
				throw new AssertionError(e);
			} catch (IllegalAccessException e) {
				throw new AssertionError(e);
			} catch (IllegalArgumentException e) {
				throw new AssertionError(e);
			} catch (InvocationTargetException e) {
				throw new AssertionError(e); 
			}
		}
		return userRepository;
	}

	private final String initializationValue;
	
	public UserRepository(final String initializationVal) {
		Objects.requireNonNull(
				initializationVal, "initialization value must not be null");
		this.initializationValue = initializationVal;
	}
	
	public abstract User get(final String name);
	
	public abstract Users getAll();
	
	public final String getInitializationValue() {
		return this.initializationValue;
	}
	
	public abstract void put(final User user);
	
	public abstract void putAll(final Users users);
	
	public abstract void remove(final String name);

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getName())
			.append(':')
			.append(this.initializationValue);
		return builder.toString();
	}
	
}
