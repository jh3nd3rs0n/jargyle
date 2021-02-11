package jargyle.net.socks;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

	public static final class Builder {
		
		private Settings settings;
		
		public Builder() {
			this.settings = null;
		}
		
		public ImmutableConfiguration build() {
			return new ImmutableConfiguration(this);
		}
		
		public Builder settings(final Settings sttngs) {
			this.settings = sttngs;
			return this;
		}
		
	}
	
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
	
	public static byte[] getXsd() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		JAXBContext jaxbContext = null;
		try {
			jaxbContext = JAXBContext.newInstance(
					ConfigurationXml.class);
		} catch (JAXBException e) {
			throw new AssertionError(e);
		}
		StreamResult result = new StreamResult(out);
		result.setSystemId("");
		try {
			jaxbContext.generateSchema(new CustomSchemaOutputResolver(result));
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		return out.toByteArray();
	}
	
	public static ImmutableConfiguration newInstance(
			final Configuration config) {
		Builder builder = new Builder();
		if (!config.getSettings().toList().isEmpty()) {
			builder.settings(config.getSettings());
		}
		return builder.build();
	}
	
	private static ImmutableConfiguration newInstance(
			final ConfigurationXml configurationXml) {
		Builder builder = new Builder();
		if (configurationXml.settings != null) {
			builder.settings(configurationXml.settings);
		}
		return builder.build();
	}
	
	public static ImmutableConfiguration newInstanceFrom(
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
	
	private ImmutableConfiguration(final Builder builder) {
		Settings sttngs = builder.settings;
		this.settings = sttngs;
	}
	
	@Override
	public Settings getSettings() {
		if (this.settings == null) {
			return Settings.EMPTY_INSTANCE;
		}
		return this.settings;
	}
	
	private ConfigurationXml toConfigurationXml() {
		ConfigurationXml configurationXml = new ConfigurationXml();
		if (this.settings != null) {
			configurationXml.settings = this.settings;
		}
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

	public byte[] toXml() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
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
			throw new AssertionError(e);
		}
		return out.toByteArray();
	}
}
