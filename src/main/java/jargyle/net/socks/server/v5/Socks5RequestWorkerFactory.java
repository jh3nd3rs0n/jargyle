package jargyle.net.socks.server.v5;

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

@XmlJavaTypeAdapter(Socks5RequestWorkerFactory.Socks5RequestWorkerFactoryXmlAdapter.class)
public class Socks5RequestWorkerFactory {

	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "socks5RequestWorkerFactory", propOrder = { })
	static class Socks5RequestWorkerFactoryXml {
		@XmlElement(name = "className", required = true)
		protected String className;
		@XmlElement(name = "value")
		protected String value;
	}
	
	static class Socks5RequestWorkerFactoryXmlAdapter extends 
		XmlAdapter<Socks5RequestWorkerFactoryXml, Socks5RequestWorkerFactory> {

		@Override
		public Socks5RequestWorkerFactoryXml marshal(
				final Socks5RequestWorkerFactory v) throws Exception {
			if (v == null) { return null; }
			Socks5RequestWorkerFactoryXml socks5RequestWorkerFactoryXml =
					new Socks5RequestWorkerFactoryXml();
			socks5RequestWorkerFactoryXml.className = v.getClass().getName();
			socks5RequestWorkerFactoryXml.value = v.value;
			return socks5RequestWorkerFactoryXml;
		}

		@Override
		public Socks5RequestWorkerFactory unmarshal(
				final Socks5RequestWorkerFactoryXml v) throws Exception {
			if (v == null) { return null; }
			return Socks5RequestWorkerFactory.newInstance(
					v.className, v.value);
		}
		
	}
	
	public static Socks5RequestWorkerFactory newInstance() {
		return new Socks5RequestWorkerFactory(null);
	}
	
	public static Socks5RequestWorkerFactory newInstance(
			final Class<?> cls, final String value) {
		Socks5RequestWorkerFactory socks5RequestWorkerFactory = null;
		if (cls.equals(Socks5RequestWorkerFactory.class)) {
			socks5RequestWorkerFactory = new Socks5RequestWorkerFactory(value);
		} else if (Socks5RequestWorkerFactory.class.isAssignableFrom(cls)) {
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
					socks5RequestWorkerFactory = 
							(Socks5RequestWorkerFactory) method.invoke(
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
					socks5RequestWorkerFactory = 
							(Socks5RequestWorkerFactory) constructor.newInstance(
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
						"class %1$s does not have either a static method that "
						+ "has one method parameter of type %2$s and a method "
						+ "return type of the provided type nor an "
						+ "instantiatable constructor that has one constructor "
						+ "parameter of type %2$s",
						cls,
						String.class.getName()));
			}
		} else {
			throw new IllegalArgumentException(String.format(
					"class must be or must extend '%s'", 
					Socks5RequestWorkerFactory.class.getName()));
		}
		return socks5RequestWorkerFactory;
	}
	
	public static Socks5RequestWorkerFactory newInstance(final String s) {
		String[] sElements = s.split(":", 2);
		String className = sElements[0];
		String value = null;
		if (sElements.length == 2) {
			value = sElements[1];
		}
		return newInstance(className, value);
	}
	
	private static Socks5RequestWorkerFactory newInstance(
			final String className, final String value) {
		Class<?> cls = null;
		try {
			cls = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
		return newInstance(cls, value);
	}

	private final String value;
	
	protected Socks5RequestWorkerFactory(final String val) {
		this.value = val;
	}
	
	public final String getValue() {
		return this.value;
	}
	
	public Socks5RequestWorker newSocks5RequestWorker(
			final Socks5RequestWorkerContext context) {
		return new Socks5RequestWorker(context, this.value);
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
