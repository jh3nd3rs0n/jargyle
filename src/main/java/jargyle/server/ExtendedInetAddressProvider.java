package jargyle.server;

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

import jargyle.common.net.InetAddressProvider;

@XmlJavaTypeAdapter(ExtendedInetAddressProvider.ExtendedInetAddressServiceXmlAdapter.class)
public class ExtendedInetAddressProvider extends InetAddressProvider {
	
	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "extendedInetAddressProvider", propOrder = { })	
	static class ExtendedInetAddressProviderXml {
		@XmlElement(name = "className", required = true)
		protected String className;
	}
	
	static class ExtendedInetAddressServiceXmlAdapter extends 
	XmlAdapter<ExtendedInetAddressProviderXml, ExtendedInetAddressProvider> {

		@Override
		public ExtendedInetAddressProviderXml marshal(
				final ExtendedInetAddressProvider v) throws Exception {
			if (v == null) { return null; }
			ExtendedInetAddressProviderXml extendedInetAddressProviderXml =
					new ExtendedInetAddressProviderXml();
			extendedInetAddressProviderXml.className = v.getClass().getName();
			return extendedInetAddressProviderXml;
		}

		@Override
		public ExtendedInetAddressProvider unmarshal(
				final ExtendedInetAddressProviderXml v) throws Exception {
			if (v == null) { return null; }
			return ExtendedInetAddressProvider.getInstance(v.className);
		}
		
	}
	
	private static final ExtendedInetAddressProvider INSTANCE = 
			new ExtendedInetAddressProvider(); 
	
	public static ExtendedInetAddressProvider getInstance() {
		return INSTANCE;
	}
	
	public static ExtendedInetAddressProvider getInstance(final Class<?> cls) {
		ExtendedInetAddressProvider extendedInetAddressProvider = null;
		if (cls.equals(ExtendedInetAddressProvider.class)) {
			extendedInetAddressProvider = INSTANCE;
		} else if (ExtendedInetAddressProvider.class.isAssignableFrom(cls)) {
			Method method = null;
			Constructor<?> constructor = null;
			for (Method meth : cls.getDeclaredMethods()) {
				int modifiers = meth.getModifiers();
				Class<?> returnType = meth.getReturnType();
				Class<?>[] parameterTypes = meth.getParameterTypes();
				boolean isPublic = Modifier.isPublic(modifiers);
				boolean isStatic = Modifier.isStatic(modifiers);
				boolean isReturnTypeClass = returnType.equals(cls);
				boolean hasNoParameterTypes = parameterTypes.length == 0;
				if (isPublic 
						&& isStatic 
						&& isReturnTypeClass 
						&& hasNoParameterTypes) {
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
					boolean hasNoParameterTypes = parameterTypes.length == 0;
					if (isPublic && isInstantiatable && hasNoParameterTypes) {
						constructor = ctor;
						break;
					}
				}
			}
			if (method != null) {
				method.setAccessible(true);
				try {
					extendedInetAddressProvider = 
							(ExtendedInetAddressProvider) method.invoke(null);
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
					extendedInetAddressProvider = 
							(ExtendedInetAddressProvider) constructor.newInstance();
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
						"class %s does not have either a static method that "
						+ "has no method parameters and a method return type "
						+ "of the provided type nor an instantiatable "
						+ "constructor that has no constructor parameters",
						cls));
			}			
		} else {
			throw new IllegalArgumentException(String.format(
					"class must be or must extend '%s'", 
					ExtendedInetAddressProvider.class.getName()));
		}
		return extendedInetAddressProvider;
	}
	
	public static ExtendedInetAddressProvider getInstance(final String s) {
		Class<?> cls = null;
		try {
			cls = Class.forName(s);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
		return getInstance(cls);		
	}
	
	private Configuration configuration;
	
	protected ExtendedInetAddressProvider() {
		this.configuration = null;
	}

	public final Configuration getConfiguration() {
		return this.configuration;
	}

	public final void setConfiguration(final Configuration config) {
		this.configuration = config;
	}
	
	@Override
	public final String toString() {
		return this.getClass().getName();
	}
	
}
