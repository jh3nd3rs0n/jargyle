package com.github.jh3nd3rs0n.jargyle.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

public final class ImmutableConfiguration extends Configuration {

	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "configuration", propOrder = { })
	@XmlRootElement(name = "configuration")
	static class ConfigurationXml {
		@XmlElement(name = "settings")
		protected Settings settings;
	}
	
	private static final class CustomSchemaOutputResolver 
		extends SchemaOutputResolver {

		private final Result result;
		
		public CustomSchemaOutputResolver(final Result res) {
			this.result = res;
		}
		
		@Override
		public Result createOutput(
				final String namespaceUri, 
				final String suggestedFileName) throws IOException {
			return this.result;
		}
		
	}
	
	public static void generateXsd(final OutputStream out) throws IOException {
		JAXBContext jaxbContext = null;
		try {
			jaxbContext = JAXBContext.newInstance(ConfigurationXml.class);
		} catch (JAXBException e) {
			throw new AssertionError(e);
		}
		StreamResult result = new StreamResult(out);
		result.setSystemId("");
		jaxbContext.generateSchema(new CustomSchemaOutputResolver(result));
	}
	
	public static ImmutableConfiguration newInstance(
			final Configuration config) {
		return newInstance(config.getSettings());
	}
	
	private static ImmutableConfiguration newInstance(
			final ConfigurationXml configurationXml) {
		return newInstance(configurationXml.settings);
	}
	
	public static ImmutableConfiguration newInstance(final Settings settings) {
		return new ImmutableConfiguration(settings);
	}
	
	public static ImmutableConfiguration newInstanceFromXml(
			final InputStream in) throws IOException {
		JAXBContext jaxbContext = null;
		try {
			jaxbContext = JAXBContext.newInstance(ConfigurationXml.class);
		} catch (JAXBException e) {
			throw new IOException(e);
		}
		Unmarshaller unmarshaller = null;
		try {
			unmarshaller = jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			throw new IOException(e);
		}
		try {
			unmarshaller.setEventHandler(new DefaultValidationEventHandler());
		} catch (JAXBException e) {
			throw new IOException(e);
		}
		ConfigurationXml configurationXml;
		try {
			configurationXml = (ConfigurationXml) unmarshaller.unmarshal(in);
		} catch (JAXBException e) {
			throw new IOException(e);
		}
		return newInstance(configurationXml);
	}
	
	private final Settings settings;
	
	private ImmutableConfiguration(final Settings sttngs) {
		this.settings = sttngs;
	}
	
	@Override
	public Settings getSettings() {
		if (this.settings == null) {
			return Settings.getEmptyInstance();
		}
		return this.settings;
	}
	
	private ConfigurationXml toConfigurationXml() {
		ConfigurationXml configurationXml = new ConfigurationXml();
		configurationXml.settings = this.settings;
		return configurationXml;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [settings=")
			.append(this.getSettings())
			.append("]");
		return builder.toString();
	}

	public void toXml(final OutputStream out) throws IOException {
		JAXBContext jaxbContext = null;
		try {
			jaxbContext = JAXBContext.newInstance(ConfigurationXml.class);
		} catch (JAXBException e) {
			throw new AssertionError(e);
		}
		Marshaller marshaller = null;
		try {
			marshaller = jaxbContext.createMarshaller();
		} catch (JAXBException e) {
			throw new AssertionError(e);
		}
		try {
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		} catch (PropertyException e) {
			throw new AssertionError(e);
		}
		try {
			marshaller.marshal(this.toConfigurationXml(), out);
		} catch (JAXBException e) {
			throw new IOException(e);
		}
	}
}
