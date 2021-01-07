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

@XmlJavaTypeAdapter(DnsResolver.DnsResolverXmlAdapter.class)
public class DnsResolver {
	
	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "dnsResolver", propOrder = { })	
	static class DnsResolverXml {
		@XmlElement(name = "className", required = true)
		protected String className;
	}
	
	static class DnsResolverXmlAdapter extends 
	XmlAdapter<DnsResolverXml, DnsResolver> {

		@Override
		public DnsResolverXml marshal(final DnsResolver v) throws Exception {
			if (v == null) { return null; }
			DnsResolverXml dnsResolverXml =	new DnsResolverXml();
			dnsResolverXml.className = v.getClass().getName();
			return dnsResolverXml;
		}

		@Override
		public DnsResolver unmarshal(final DnsResolverXml v) throws Exception {
			if (v == null) { return null; }
			return DnsResolver.newInstance(v.className);
		}
		
	}
	
	public synchronized static DnsResolver getInstance(
			final Configuration config) {
		Settings settings = config.getSettings();
		DnsResolver dnsResolver = settings.getLastValue(
				SettingSpec.DNS_RESOLVER, DnsResolver.class);
		if (dnsResolver == null) {
			dnsResolver = new DnsResolver();
		}
		if (dnsResolver.getConfiguration() == null) {
			dnsResolver.setConfiguration(config);
		}
		return dnsResolver;
	}
	
	public static DnsResolver newInstance() {
		return new DnsResolver();
	}
	
	public static DnsResolver newInstance(final Class<?> cls) {
		DnsResolver dnsResolver = null;
		if (cls.equals(DnsResolver.class)) {
			dnsResolver = new DnsResolver();
		} else if (DnsResolver.class.isAssignableFrom(cls)) {
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
					dnsResolver = (DnsResolver) method.invoke(null);
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
					dnsResolver = (DnsResolver) constructor.newInstance();
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
					DnsResolver.class.getName()));
		}
		return dnsResolver;
	}
	
	public static DnsResolver newInstance(final String s) {
		Class<?> cls = null;
		try {
			cls = Class.forName(s);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
		return newInstance(cls);		
	}
	
	private Configuration configuration;
	
	protected DnsResolver() {
		this.configuration = null;
	}

	public final Configuration getConfiguration() {
		return this.configuration;
	}
	
	public InetAddress resolve(final String host) throws UnknownHostException {
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
