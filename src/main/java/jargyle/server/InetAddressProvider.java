package jargyle.server;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(InetAddressProvider.InetAddressServiceXmlAdapter.class)
public class InetAddressProvider {
	
	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "inetAddressProvider", propOrder = { })	
	static class InetAddressProviderXml {
		@XmlElement(name = "className", required = true)
		protected String className;
	}
	
	static class InetAddressServiceXmlAdapter extends 
	XmlAdapter<InetAddressProviderXml, InetAddressProvider> {

		@Override
		public InetAddressProviderXml marshal(
				final InetAddressProvider v) throws Exception {
			if (v == null) { return null; }
			InetAddressProviderXml inetAddressProviderXml =
					new InetAddressProviderXml();
			inetAddressProviderXml.className = v.getClass().getName();
			return inetAddressProviderXml;
		}

		@Override
		public InetAddressProvider unmarshal(
				final InetAddressProviderXml v) throws Exception {
			if (v == null) { return null; }
			return InetAddressProvider.getInstance(v.className);
		}
		
	}
	
	private static InetAddressProvider defaultInstance;
	
	private static final InetAddressProvider INSTANCE = 
			new InetAddressProvider();
	
	public static InetAddressProvider getDefault() {
		if (defaultInstance == null) {
			defaultInstance = INSTANCE;
		}
		return defaultInstance;
	}
	
	public static InetAddressProvider getInstance() {
		return INSTANCE;
	} 
	
	public static InetAddressProvider getInstance(final Class<?> cls) {
		InetAddressProvider inetAddressProvider = null;
		if (cls.equals(InetAddressProvider.class)) {
			inetAddressProvider = INSTANCE;
		} else if (InetAddressProvider.class.isAssignableFrom(cls)) {
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
					inetAddressProvider =
							(InetAddressProvider) method.invoke(null);
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
					inetAddressProvider = 
							(InetAddressProvider) constructor.newInstance();
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
					InetAddressProvider.class.getName()));
		}
		return inetAddressProvider;
	}
	
	public static InetAddressProvider getInstance(final String s) {
		Class<?> cls = null;
		try {
			cls = Class.forName(s);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
		return getInstance(cls);		
	}
	
	public static void setDefault(final InetAddressProvider provider) {
		defaultInstance = provider;
	}
	
	private Configuration configuration;
	
	protected InetAddressProvider() {
		this.configuration = null;
	}

	public final Configuration getConfiguration() {
		return this.configuration;
	}
	
	public InetAddress getInetAddress(
			final String host) throws UnknownHostException {
		return InetAddress.getByName(host);
	}

	public final void setConfiguration(final Configuration config) {
		this.configuration = config;
	}
	
	@Override
	public final String toString() {
		return this.getClass().getName();
	}
	
}
